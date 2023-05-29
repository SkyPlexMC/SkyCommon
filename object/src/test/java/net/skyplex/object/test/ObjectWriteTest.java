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
package net.skyplex.object.test;

import net.skyplex.common.object.SimpleArray;
import net.skyplex.common.object.SimpleObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ObjectWriteTest {

    @Test
    public void writeSimpleObject() {
        SimpleObject object = new SimpleObject();
        object.node("first").set("value");

        Assertions.assertEquals("value", object.node("first").as(String.class).get());
    }

    @Test
    public void writeDeepObject() {
        SimpleObject object = new SimpleObject();
        object.node("first", "second").set("value");

        Assertions.assertEquals("value", object.node("first", "second").as(String.class).get());
    }

    @Test
    public void writeSimpleArray() {
        SimpleArray array = new SimpleArray();
        array.node(0).set("value1");
        array.node(1).set("value2");
        array.node(3).set("value4");

        Assertions.assertEquals("value1", array.node(0).as(String.class).get());
        Assertions.assertEquals("value2", array.node(1).as(String.class).get());
        Assertions.assertThrows(NullPointerException.class, () -> array.node(2).as(String.class).get());
        Assertions.assertEquals("value4", array.node(3).as(String.class).get());
    }

}
