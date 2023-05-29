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

import java.util.List;

public class ListObjectNode extends AbstractObjectNode<Integer, Object> {

    private List<Object> values;

    public ListObjectNode(ObjectHolder<Integer> holder, Integer key, List<Object> values) {
        super(holder, key, Object.class);
        this.values = values;
    }

    @Override
    public Object getRaw() {
        return key >= values.size() ? null : values.get(key);
    }

    @Override
    public void set(Object value) {
        if (value == null) {
            values.remove(key.intValue());
        } else {
            while (values.size() <= key) {
                values.add(null);
            }
            values.set(key, value);
        }
    }

}
