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

import java.nio.charset.Charset;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@SuppressWarnings("unchecked")
public class ObjectTypeConverter {

    private static final Map<Class<?>, Function<Object, ?>> converters = new HashMap<>();

    static {
        registerConverter(Boolean.class, object -> Boolean.parseBoolean(String.valueOf(object)));
        registerConverter(Character.class, String.class, string -> string.length() == 1 ? string.charAt(0) : null);
        registerConverter(String.class, String::valueOf);
        registerConverter(Byte.class, createNumberConverter(Number::byteValue, Byte::parseByte));
        registerConverter(Short.class, createNumberConverter(Number::shortValue, Short::parseShort));
        registerConverter(Integer.class, createNumberConverter(Number::intValue, Integer::parseInt));
        registerConverter(Long.class, createNumberConverter(Number::longValue, Long::parseLong));
        registerConverter(Float.class, createNumberConverter(Number::floatValue, Float::parseFloat));
        registerConverter(Double.class, createNumberConverter(Number::doubleValue, Double::parseDouble));
        registerConverter(SimpleArray.class, object -> {
            if (object instanceof List<?> list) {
                return SimpleArray.wrap((List<Object>) list);
            }
            return null;
        });
        registerConverter(SimpleObject.class, object -> {
            if (object instanceof Map<?, ?> map) {
                return SimpleObject.wrap((Map<String, Object>) map);
            }
            return null;
        });
        registerConverter(Charset.class, String.class, Charset::forName);
    }

    public static <A, B> Function<A, B> converter(Class<A> from, Class<B> to) {
        if (to.isAssignableFrom(from)) {
            return to::cast;
        }
        if (to.isEnum()) {
            return a -> convertEnum(to, a);
        }
        // TODO: Use a table rather than instanceof inside of the converter
        Function<A, B> converter = (Function<A, B>) converters.get(to);
        if (converter == null) {
            throw new NullPointerException("No converter for type " + from.getName() + " to " + to.getName());
        }
        return converter;
    }

    private static <T> T convertEnum(Class<T> type, Object value) {
        if (value instanceof Number ordinal) {
            return type.getEnumConstants()[ordinal.intValue()];
        }
        try {
            var field = type.getDeclaredField(String.valueOf(value));
            return field.isEnumConstant() ? type.cast(field.get(null)) : null;
        } catch (NoSuchFieldException exception) {
            return null;
        } catch (IllegalAccessException exception) {
            throw new RuntimeException(exception);
        }
    }

    public static <T> void registerConverter(Class<T> type, Function<Object, T> function) {
        converters.put(type, object -> {
            // explicit check
            if (type.isAssignableFrom(object.getClass())) {
                return type.cast(object);
            }
            return function.apply(object);
        });
    }

    public static <T, I> void registerConverter(Class<T> type, Class<I> inputType, Function<I, T> function) {
        registerConverter(type, object -> {
            var converted = ((Function<Object, I>) converter(object.getClass(), inputType)).apply(object);
            return converted == null ? null : function.apply(converted);
        });
    }

    private static <T> Function<Object, T> createNumberConverter(Function<Number, T> function, Function<String, T> parseFunction) {
        return object -> {
            if (object instanceof Number number) {
                return function.apply(number);
            }
            try {
                return parseFunction.apply(String.valueOf(object));
            } catch (NumberFormatException ignored) {
                return null;
            }
        };
    }

    public static Map<Class<?>, Function<Object, ?>> getConverters() {
        return Collections.unmodifiableMap(converters);
    }

}
