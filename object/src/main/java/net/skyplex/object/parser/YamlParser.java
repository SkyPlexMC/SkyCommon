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
package net.skyplex.object.parser;

import net.skyplex.object.SimpleArray;
import net.skyplex.object.SimpleObject;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.io.Reader;

public class YamlParser implements Parser {

    @Override
    public SimpleObject parseObject(String text) {
        return SimpleObject.wrap(new Yaml().load(text));
    }

    @Override
    public SimpleObject parseObject(InputStream stream) {
        return SimpleObject.wrap(new Yaml().load(stream));
    }

    @Override
    public SimpleObject parseObject(Reader reader) {
        return SimpleObject.wrap(new Yaml().load(reader));
    }

    @Override
    public SimpleArray parseArray(String text) {
        return SimpleArray.wrap(new Yaml().loadAs(text, Object[].class));
    }

    @Override
    public SimpleArray parseArray(InputStream stream) {
        return SimpleArray.wrap(new Yaml().loadAs(stream, Object[].class));
    }

    @Override
    public SimpleArray parseArray(Reader reader) {
        return SimpleArray.wrap(new Yaml().loadAs(reader, Object[].class));
    }

}
