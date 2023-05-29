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

import java.util.function.Function;

public class TransformedObjectNode<K, T, R> extends AbstractObjectNode<K, R> {

    private ObjectNode<K, T> parent;
    private final Function<T, R> mapper;
    private final Function<R, T> unmapper;

    public TransformedObjectNode(ObjectNode<K, T> parent, Class<R> type, Function<T, R> mapper, Function<R, T> unmapper) {
        super(parent.getHolder(), parent.getKey(), type);
        this.parent = parent;
        this.mapper = mapper;
        this.unmapper = unmapper;
    }

    @Override
    public R getRaw() {
        if (mapper == null) {
            throw new UnsupportedOperationException("Cannot get value: no mapping function present");
        }
        T value = parent.or(null);
        return value == null ? null : mapper.apply(value);
    }

    @Override
    public void set(R value) {
        if (unmapper == null) {
            throw new UnsupportedOperationException("Cannot set value: no mapping function present");
        }
        if (value != null && !type.isAssignableFrom(value.getClass())) {
            throw new IllegalArgumentException("Cannot map type " + value.getClass().getName());
        }

        parent.set(unmapper.apply(value));
    }

}
