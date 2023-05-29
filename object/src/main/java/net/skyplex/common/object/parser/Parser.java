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
package net.skyplex.common.object.parser;

import com.fasterxml.jackson.core.JsonFactory;
import net.skyplex.common.object.SimpleArray;
import net.skyplex.common.object.SimpleObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

public interface Parser {

    JacksonParser JSON = new JacksonParser(new JsonFactory());
    YamlParser YAML = new YamlParser();

    SimpleObject parseObject(String text);

    SimpleObject parseObject(InputStream stream) throws IOException;

    SimpleObject parseObject(Reader reader) throws IOException;

    SimpleArray parseArray(String text);

    SimpleArray parseArray(InputStream stream) throws IOException;

    SimpleArray parseArray(Reader reader) throws IOException;

}
