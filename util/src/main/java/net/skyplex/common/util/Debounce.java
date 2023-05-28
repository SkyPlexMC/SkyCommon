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
package net.skyplex.common.util;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class Debounce implements Runnable {

    private static final ScheduledExecutorService SCHEDULER = Executors.newSingleThreadScheduledExecutor(runnable -> {
        Thread thread = new Thread(runnable, "Debounce");
        thread.setDaemon(true);
        return thread;
    });

    public static Runnable debounce(Runnable runnable, long delay) {
        return new Debounce(runnable, delay);
    }

    private Runnable delegate;
    private float delay;
    private long lastCall;
    private ScheduledFuture<?> task;

    public Debounce(Runnable delegate, long delay) {
        this.delegate = delegate;
        this.delay = delay;
    }

    @Override
    public void run() {
        long now = System.currentTimeMillis();
        long remainingBeforeCall = now - lastCall;

        if (remainingBeforeCall < delay) {
            if (task != null) {
                task.cancel(false);
            }
            task = SCHEDULER.schedule(this, remainingBeforeCall + 1, TimeUnit.MILLISECONDS);
            return;
        }

        lastCall = now;
        delegate.run();
    }

    @Override
    public String toString() {
        return "Debounce{delegate=" + delegate + ", delay=" + delay + ", lastCall=" + lastCall + '}';
    }

}
