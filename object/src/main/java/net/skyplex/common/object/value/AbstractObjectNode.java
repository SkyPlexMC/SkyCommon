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
package net.skyplex.common.object.value;

import net.skyplex.common.object.ObjectHolder;
import net.skyplex.common.object.ObjectTypeConverter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class AbstractObjectNode<K, V> implements ObjectNode<K, V> {

    protected final ObjectHolder<K> holder;
    protected final K key;
    protected final Class<V> type;

    public AbstractObjectNode(ObjectHolder<K> holder, K key, Class<V> type) {
        this.holder = holder;
        this.key = key;
        this.type = type;
    }

    protected abstract V getRaw();

    @Override
    public @NotNull ObjectHolder<K> getHolder() {
        return holder;
    }

    @Override
    public @NotNull K getKey() {
        return key;
    }

    @Override
    public @NotNull Class<V> getType() {
        return type;
    }

    @Override
    public @NotNull ObjectNode<K, ?> sibling(K key) {
        return holder.node(key);
    }

    @Override
    public boolean isPresent() {
        return getRaw() != null;
    }

    @Override
    public boolean isEmpty() {
        return !isPresent();
    }

    @Override
    public @NotNull <T> ObjectNode<K, T> as(Class<T> type) {
        // TODO: Support for writing
        return mapped(type, ObjectTypeConverter.converter(this.type, type));
    }

    @Override
    public V get() throws NullPointerException {
        if (isPresent()) {
            return getRaw();
        }
        throw new NullPointerException("No value of type " + type + " available with key " + key);
    }

    @Override
    public V or(V defaultValue) {
        return getOrThrow(() -> defaultValue);
    }

    @Override
    public V getOr(@NotNull Supplier<V> supplier) {
        return getOrThrow(supplier::get);
    }

    @Override
    public V getOrThrow() throws NullPointerException {
        return orThrow(() -> new NoSuchElementException("No value"));
    }

    @Override
    public <E extends Throwable> V getOrThrow(@NotNull ThrowingSupplier<V, E> supplier) throws E {
        return isPresent() ? getRaw() : supplier.get();
    }

    @Override
    public <E extends Throwable> V orThrow(@NotNull Supplier<E> supplier) throws E {
        if (isPresent()) {
            return getRaw();
        }
        throw supplier.get();
    }

    @Override
    public V getOrSet(V defaultValue) {
        return getOrSetThrow(() -> defaultValue);
    }

    @Override
    public V getOrSet(@NotNull Supplier<V> supplier) {
        return getOrSetThrow(supplier::get);
    }

    @Override
    public <E extends Throwable> V getOrSetThrow(@NotNull ThrowingSupplier<V, E> supplier) throws E {
        if (isPresent()) {
            return getRaw();
        }
        V value = supplier.get();
        set(value);
        return value;
    }

    @Override
    public <R> @NotNull ObjectNode<K, R> mapped(@NotNull Class<R> type, @Nullable Function<V, R> mapper) {
        return mapped(type, mapper, null);
    }

    @Override
    public <R> @NotNull ObjectNode<K, R> mapped(@NotNull Class<R> type, @Nullable Function<V, R> mapper, @Nullable Function<R, V> unmapper) {
        return new TransformedObjectNode<>(this, type, mapper, unmapper);
    }

    @Override
    public void ifPresent(@NotNull Consumer<V> action) {
        if (isPresent()) {
            action.accept(getRaw());
        }
    }

}
