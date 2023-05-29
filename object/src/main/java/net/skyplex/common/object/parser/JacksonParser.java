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
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.skyplex.common.object.SimpleArray;
import net.skyplex.common.object.SimpleObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Map;

@SuppressWarnings("unchecked")
public class JacksonParser implements Parser {

    private ObjectMapper mapper;

    public JacksonParser(JsonFactory factory) {
        mapper = new ObjectMapper(factory);
    }

    @Override
    public SimpleObject parseObject(String text) {
        try {
            return SimpleObject.wrap(mapper.readValue(text, Map.class));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public SimpleObject parseObject(InputStream stream) throws IOException {
        return SimpleObject.wrap(mapper.readValue(stream, Map.class));
    }

    @Override
    public SimpleObject parseObject(Reader reader) throws IOException {
        return SimpleObject.wrap(mapper.readValue(reader, Map.class));
    }

    @Override
    public SimpleArray parseArray(String json) {
        try {
            return SimpleArray.wrap(mapper.readValue(json, Object[].class));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public SimpleArray parseArray(InputStream stream) throws IOException {
        return SimpleArray.wrap(mapper.readValue(stream, Object[].class));
    }

    @Override
    public SimpleArray parseArray(Reader reader) throws IOException {
        return SimpleArray.wrap(mapper.readValue(reader, Object[].class));
    }

    public ObjectMapper getMapper() {
        return mapper;
    }
}
