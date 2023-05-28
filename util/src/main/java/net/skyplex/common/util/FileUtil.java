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

import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class FileUtil {

    public static String stripFileExtension(String filename) {
        return stripFileExtension(filename, System.getProperty("file.separator"));
    }

    public static String stripFileExtension(String filename, String separator) {
        var separatorIndex = filename.lastIndexOf(separator) + 1;
        int lastExtensionIndex = filename.lastIndexOf('.');
        return lastExtensionIndex == -1 || lastExtensionIndex < separatorIndex ?
                filename.substring(separatorIndex) :
                filename.substring(separatorIndex, lastExtensionIndex);
    }

    public static void deleteDirectory(Path directory) throws IOException {
        var iterator = walkReversed(directory).iterator();
        while (iterator.hasNext()) {
            Files.delete(iterator.next());
        }
    }

    /**
     * Equivalent of {@link Files#walk(Path, FileVisitOption...)} but it walks from deepest to lowest files
     */
    public static Stream<Path> walkReversed(Path start) throws IOException {
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(new ReversedFileIterator(start), Spliterator.DISTINCT), false);
    }

}
