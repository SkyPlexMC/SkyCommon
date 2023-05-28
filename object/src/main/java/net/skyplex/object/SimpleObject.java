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

import net.skyplex.object.value.MapObjectNode;
import net.skyplex.object.value.ObjectNode;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class SimpleObject extends AbstractObject<String> {

    /**
     * An immutable empty object.
     */
    public static final SimpleObject EMPTY = new SimpleObject(Collections.emptyMap());

    private Map<String, Object> content;

    public SimpleObject() {
        this(new HashMap<>());
    }

    public SimpleObject(Map<String, Object> content) {
        this.content = content;
    }

    @Override
    public ObjectNode<String, Object> node(String key) {
        return new MapObjectNode<>(this, key, content);
    }

    @Override
    public Map<String, Object> getContent() {
        return content;
    }

    public int length() {
        return content.size();
    }

    @Override
    public Stream<String> streamKeys() {
        return content.keySet().stream();
    }

    @Override
    public Stream<Object> streamValues() {
        return content.values().stream().map(AbstractObject::wrap);
    }

    @Override
    public Stream<Entry<String, Object>> stream() {
        return content.entrySet()
                .stream()
                .map(entry -> new Entry<>(entry.getKey(), AbstractObject.wrap(entry.getValue())));
    }

    @Override
    public String toString() {
        return "SimpleObject" + content;
    }

    public static SimpleObject wrap(Map<String, Object> map) {
        return new SimpleObject(map);
    }

}
