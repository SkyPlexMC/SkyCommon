/*
 * Copyright 2023 SkyPlex (https://github.com/SkyPlexMC)
 *
 * Licensed under the BSD 4-Clause License.
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://spdx.org/licenses/BSD-4-Clause.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.skyplex.common.i18n.message;

import net.skyplex.common.i18n.language.Language;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;
import org.redisson.api.RBucket;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class RedissonCachedMessageProvider implements MessageProvider {

    private final RedissonClient redisson;
    private final MessageProvider delegate;
    private final String format;
    private final LockingOptions options;

    public RedissonCachedMessageProvider(@NotNull RedissonClient redisson, @NotNull MessageProvider delegate) {
        this(redisson, delegate, "cache:%s:%s", new LockingOptions(true, 1, TimeUnit.MINUTES, LockFailureAction.EXCEPTION));
    }

    public RedissonCachedMessageProvider(@NotNull RedissonClient redisson, @NotNull MessageProvider delegate,
                                         @NotNull String format, @NotNull LockingOptions options) {
        this.redisson = Objects.requireNonNull(redisson, "redisson");
        this.delegate = Objects.requireNonNull(delegate, "delegate");
        this.format = Objects.requireNonNull(format, "format");
        this.options = Objects.requireNonNull(options, "options");
    }

    @Override
    public @NotNull Map<String, Map<String, String>> loadMessages(@NotNull String bundle, @NotNull @UnmodifiableView Collection<Language> languages) {
        return lock(bundle, () -> {
            RBucket<Map<String, Map<String, String>>> bucket = getBucket(bundle);

            var messages = bucket.get();

            if (messages == null) {
                messages = delegate.loadMessages(bundle, languages);

                bucket.set(messages, 1, TimeUnit.DAYS);
            }

            return messages;
        });
    }

    public void deleteCache(String bundle) {
        lock(bundle, () -> getBucket(bundle).delete());
    }

    private <T> T lock(String bundle, Supplier<T> action) {
        RLock lock = redisson.getLock(format.formatted(bundle, "lock"));
        boolean locked = false;
        try {
            if (options.lock() && !(locked = lock.tryLock(1, TimeUnit.MINUTES))) {
                if (options.failureAction() == LockFailureAction.EXCEPTION) {
                    throw new RuntimeException("Couldn't acquire I18N lock in 1 minute");
                }
            }
            return action.get();
        } catch (InterruptedException exception) {
            throw new RuntimeException("Thread was interrupted while trying to acquire I18N lock", exception);
        } finally {
            if (locked) {
                lock.unlock();
            }
        }
    }

    private RBucket<Map<String, Map<String, String>>> getBucket(String bundle) {
        return redisson.getBucket(format.formatted(bundle, "messages"));
    }

    public record LockingOptions(boolean lock, int attemptDuration, @NotNull TimeUnit unit,
                                 @NotNull LockFailureAction failureAction) {

        public LockingOptions {
            Objects.requireNonNull(unit, "unit");
            Objects.requireNonNull(failureAction, "failureAction");
        }

    }

    public enum LockFailureAction {

        /**
         * Ignores the lock and continues loading messages anyway
         */
        IGNORE_LOCK,
        /**
         * Interrupts the loading process by throwing an exception
         */
        EXCEPTION

    }

}
