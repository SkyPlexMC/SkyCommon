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
package net.skyplex.common.util;

import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;

public class StringUtil {

    public static final String NUMBERS = createRange('0', '9');
    public static final String UPPERCASE_LETTERS = createRange('A', 'Z');
    public static final String LOWERCASE_LETTERS = createRange('a', 'z');

    public static @NotNull String createRange(char start, char end) {
        char[] chars = new char[end - start + 1];
        for (int i = 0; i < chars.length; i++) {
            chars[i] = (char) (i + start);
        }
        return new String(chars);
    }

    /**
     * Generate a random string of the specified length using the characters Uses the random from
     * {@link ThreadLocalRandom#current()}
     *
     * @see StringUtil#generateString(int, Random, String...)
     */
    public static @NotNull String generateString(int length, String... characters) {
        return generateString(length, ThreadLocalRandom.current(), characters);
    }

    /**
     * Generate a random string
     *
     * @param length     the length of the string to generate
     * @param random     the random number generator to use
     * @param characters one of multiple character sets to use for the generated string
     * @return the randomly generated string
     */
    public static @NotNull String generateString(int length, Random random, String... characters) {
        // TODO: Weight the random so that it's distributed equally trough different length character sets

        char[] chars = new char[length];
        for (int i = 0; i < chars.length; i++) {
            var set = characters[random.nextInt(characters.length)];
            chars[i] = set.charAt(random.nextInt(set.length()));
        }
        return new String(chars);
    }

    public static int escapedIndexOf(@NotNull String string, char c) {
        return escapedIndexOf(string, c, '\\');
    }

    public static int escapedIndexOf(@NotNull String string, char c, char escape) {
        if (string.isEmpty()) {
            return -1;
        }
        if (string.charAt(0) == c) {
            return 0;
        }
        for (int i = 1; i < string.length(); i++) {
            if (string.charAt(i) == c && string.charAt(i - 1) != escape) {
                return i;
            }
        }
        return -1;
    }

    public static int levenshtein(@NotNull String a, @NotNull String b) {
        int[][] map = new int[a.length()][b.length()];

        for (int x = 0; x < a.length(); x++) {
            map[x][0] = x;
        }
        for (int y = 0; y < b.length(); y++) {
            map[0][y] = y;
        }

        for (int x = 1; x < a.length(); x++) {
            for (int y = 1; y < b.length(); y++) {
                int cost = a.charAt(x) == b.charAt(y) ? 0 : 1;
                int va = map[x - 1][y] + 1;
                int vb = map[x][y - 1] + 1;
                int vc = map[x - 1][y - 1] + cost;
                int min = Math.min(va, Math.min(vb, vc));
                map[x][y] = min;
            }
        }

        return map[a.length() - 1][b.length() - 1] + 1;
    }

    public static @NotNull String replaceMultiple(@NotNull String text, @NotNull Map<String, String> arguments) {
        return replaceMultiple(text, arguments, Function.identity());
    }

    public static <V> @NotNull String replaceMultiple(@NotNull String text, @NotNull Map<String, V> arguments, @NotNull Function<V, String> toString) {
        if (arguments.size() == 0) {
            return text;
        }

        PriorityQueue<ReplacementIndex> queue = new PriorityQueue<>(arguments.size());

        for (var entry : arguments.entrySet()) {
            int index = text.indexOf(entry.getKey());
            if (index == -1) {
                continue;
            }
            queue.add(new ReplacementIndex(entry.getKey(), toString.apply(entry.getValue()), index));
        }

        // no matches with any of the keys, no need to replace
        if (queue.isEmpty()) {
            return text;
        }

        var builder = new StringBuilder();
        builder.ensureCapacity(text.length());

        int position = 0;

        while (!queue.isEmpty()) {
            ReplacementIndex replacement = queue.poll();

            builder.append(text, position, replacement.nextIndex);
            builder.append(replacement.replacement);

            position = replacement.nextIndex + replacement.key.length();

            replacement.nextIndex = text.indexOf(replacement.key, position);

            if (replacement.nextIndex != -1) {
                queue.add(replacement);
            }
        }

        // append the remaining text
        builder.append(text, position, text.length());

        return builder.toString();
    }

    private static final class ReplacementIndex implements Comparable<ReplacementIndex> {
        private final String key;
        private final String replacement;
        private int nextIndex;

        private ReplacementIndex(String key, String replacement, int nextIndex) {
            this.key = key;
            this.replacement = replacement;
            this.nextIndex = nextIndex;
        }

        @Override
        public int compareTo(@NotNull StringUtil.ReplacementIndex o) {
            return Integer.compare(nextIndex, o.nextIndex);
        }

    }

}
