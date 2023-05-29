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
import net.skyplex.common.object.value.ListObjectNode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class SimpleArray extends AbstractObject<Integer> {

    public static final SimpleArray EMPTY = new SimpleArray(Collections.emptyList());

    private List<Object> content;

    public SimpleArray() {
        this(new ArrayList<>());
    }

    public SimpleArray(List<Object> content) {
        this.content = content;
    }

    @Override
    public ObjectNode<Integer, Object> node(Integer key) {
        return new ListObjectNode(this, key, content);
    }

    @Override
    public List<Object> getContent() {
        return content;
    }

    public int length() {
        return content.size();
    }

    @Override
    public Stream<Integer> streamKeys() {
        return IntStream.range(0, content.size()).boxed();
    }

    @Override
    public Stream<Object> streamValues() {
        return content.stream().map(AbstractObject::wrap);
    }

    @Override
    public Stream<Entry<Integer, Object>> stream() {
        return IntStream.range(0, content.size())
                .mapToObj(i -> new Entry<>(i, wrap(content.get(i))));
    }

    @Override
    public String toString() {
        return "SimpleArray" + content;
    }

    public static SimpleArray wrap(Object[] values) {
        return wrap(new ArrayList<>(Arrays.asList(values)));
    }

    public static SimpleArray wrap(List<Object> list) {
        return new SimpleArray(list);
    }

}
