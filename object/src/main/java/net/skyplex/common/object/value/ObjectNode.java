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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public interface ObjectNode<K, V> {

    @NotNull ObjectHolder<K> getHolder();

    @NotNull K getKey();

    @NotNull Class<V> getType();

    @NotNull ObjectNode<K, ?> sibling(K key);

    /**
     * Checks whenever any value is present
     */
    boolean isPresent();

    /**
     * Checks whenever no value is present
     */
    boolean isEmpty();

    <T> @NotNull ObjectNode<K, T> as(Class<T> type);

    V get() throws NullPointerException;

    V or(V defaultValue);

    V getOr(@NotNull Supplier<V> supplier);

    V getOrThrow() throws NullPointerException;

    <E extends Throwable> V getOrThrow(@NotNull ThrowingSupplier<V, E> supplier) throws E;

    <E extends Throwable> V orThrow(@NotNull Supplier<E> supplier) throws E;

    V getOrSet(V defaultValue);

    V getOrSet(@NotNull Supplier<V> supplier);

    <E extends Throwable> V getOrSetThrow(@NotNull ThrowingSupplier<V, E> supplier) throws E;

    void set(V value);

    <R> @NotNull ObjectNode<K, R> mapped(@NotNull Class<R> type, @Nullable Function<V, R> mapper);

    <R> @NotNull ObjectNode<K, R> mapped(@NotNull Class<R> type, @Nullable Function<V, R> mapper, @Nullable Function<R, V> unmapper);

    void ifPresent(@NotNull Consumer<V> action);

    interface ThrowingSupplier<T, E extends Throwable> {
        T get() throws E;
    }

}
