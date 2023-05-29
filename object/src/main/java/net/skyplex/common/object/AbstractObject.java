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
package net.skyplex.common.object;

import net.skyplex.common.object.value.ObjectNode;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

public abstract class AbstractObject<K> implements ObjectHolder<K> {

    @Override
    public ObjectNode<?, Object> node(K rootKey, Object... keys) {
        ObjectNode<?, Object> holder = node(rootKey);

        for (Object key : keys) {
            if (key instanceof String string) {
                holder = holder.as(SimpleObject.class).getOrSet(SimpleObject::new).node(string);
            } else if (key instanceof Integer integer) {
                holder = holder.as(SimpleArray.class).getOrSet(SimpleArray::new).node(integer);
            } else {
                throw new IllegalStateException("Unknown key object type " + key + " (" + key.getClass() + ")");
            }
        }

        return holder;
    }

    @Override
    public abstract Stream<K> streamKeys();

    @Override
    public abstract Stream<Object> streamValues();

    @Override
    public <T> Stream<T> streamValues(Class<T> type) {
        return streamValues(type, false);
    }

    @Override
    public <T> Stream<T> streamValues(Class<T> type, boolean ignoreIncorrect) {
        return streamValues()
                .map(value -> filterValue(value, type, ignoreIncorrect))
                .filter(Objects::nonNull);
    }

    /**
     * Stream the entries of this object's content, values objects and arrays will be wrapped in the corresponding {@link SimpleObject}
     */
    @Override
    public abstract Stream<Entry<K, Object>> stream();

    @Override
    public <T> Stream<Entry<K, T>> stream(Class<T> type) {
        return stream(type, false);
    }

    @Override
    public <T> Stream<Entry<K, T>> stream(Class<T> type, boolean ignoreIncorrect) {
        return stream()
                .map(entry -> filterEntry(entry, type, ignoreIncorrect))
                .filter(Objects::nonNull); // filter the incorrect objects
    }

    private <T> T filterValue(Object value, Class<T> type, boolean ignoreIncorrect) {
        Object converted = ObjectTypeConverter.converter((Class<Object>) value.getClass(), type).apply(value);
        if (converted != null) {
            return type.cast(converted);
        }
        if (ignoreIncorrect) {
            return null;
        }
        throw new NullPointerException("Could not get value %s with type %s".formatted(value, type));
    }

    private <T> Entry<K, T> filterEntry(Entry<K, Object> entry, Class<T> type, boolean ignoreIncorrect) {
        T value = filterValue(entry.getValue(), type, ignoreIncorrect);
        return value == null ? null : new Entry<>(entry.getKey(), value);
    }

    /**
     * Tries to wrap the object in a {@link SimpleArray} or {@link SimpleObject} if possible or else returns null
     */
    public static ObjectHolder<?> tryWrap(Object object) {
        return wrap(object) instanceof ObjectHolder<?> obj ? obj : null;
    }

    /**
     * Tries to wrap the object in a {@link SimpleArray} or {@link SimpleObject} if possible or else returns the object itself
     */
    @SuppressWarnings("unchecked")
    public static Object wrap(Object object) {
        if (object instanceof List<?> list) {
            return SimpleArray.wrap((List<Object>) list);
        }
        if (object instanceof Map) {
            return SimpleObject.wrap((Map<String, Object>) object);
        }
        return object;
    }

}
