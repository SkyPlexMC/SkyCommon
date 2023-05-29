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

import net.skyplex.common.object.parser.Parser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ObjectReadTest {

    @Test
    public void testObjectAccess() {
        String json = """
                {"foo": 5}
                """;
        var object = Parser.JSON.parseObject(json);
        Assertions.assertEquals(5, object.node("foo").as(Integer.class).or(5));
    }

    @Test
    public void testArrayAccess() {
        String json = """
                [0, 5, 4]
                """;
        var object = Parser.JSON.parseArray(json);
        Assertions.assertEquals(5, object.node(1).as(Integer.class).or(0));
    }

    @Test
    public void testComplexAccess() {
        var json = """
                {
                    "foo": [
                        {
                            "bar": 69
                        }
                    ]
                }
                """;
        var object = Parser.JSON.parseObject(json);
        Assertions.assertEquals(69, object.node("foo", 0, "bar").as(Integer.class).or(0));
    }

    @Test
    public void testEscapedObject() {
        var json = """
                {
                    "fi.rst": true,
                    "first.second": {
                        "third": true,
                        "th.ird": {
                            "fourth": true
                        }
                    },
                    "first.second.third": {
                        "fourth": true
                    }
                }
                """;
        var object = Parser.JSON.parseObject(json);

        Assertions.assertTrue(object.node("fi.rst").as(Boolean.class).or(false));
        Assertions.assertTrue(object.node("first.second", "third").as(Boolean.class).or(false));
        Assertions.assertTrue(object.node("first.second", "th.ird", "fourth").as(Boolean.class).or(false));
        Assertions.assertTrue(object.node("first.second.third", "fourth").as(Boolean.class).or(false));
    }

}


