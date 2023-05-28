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
package net.skyplex.object;

import net.skyplex.object.value.ObjectNode;

import java.util.stream.Stream;

public interface ObjectHolder<K> {

    ObjectNode<K, Object> node(K key);

    ObjectNode<?, Object> node(K key, Object... keys);

    Object getContent();

    Stream<K> streamKeys();

    /**
     * Stream the values of this object's content, objects and arrays will be wrapped in the corresponding {@link ObjectHolder}
     */
    Stream<Object> streamValues();

    <T> Stream<T> streamValues(Class<T> type);

    <T> Stream<T> streamValues(Class<T> type, boolean ignoreIncorrect);

    /**
     * Stream the entries of this object's content, values objects and arrays will be wrapped in the corresponding {@link SimpleObject}
     */
    Stream<Entry<K, Object>> stream();

    <T> Stream<Entry<K, T>> stream(Class<T> type);

    <T> Stream<Entry<K, T>> stream(Class<T> type, boolean ignoreIncorrect);

}
