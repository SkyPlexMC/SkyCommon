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
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;

public class ReversedFileIterator implements Iterator<Path> {

    private WalkNode currentNode;
    private Path next;

    public ReversedFileIterator(Path start) throws IOException {
        currentNode = new WalkNode(null, start);
    }

    private void supplyNextIfNeeded() {
        if (next != null) {
            return;
        }
        if (currentNode == null) {
            return;
        }
        try {
            next = currentNode.next();
        } catch (IOException exception) {
            throw new UncheckedIOException(exception);
        }
    }

    @Override
    public boolean hasNext() {
        supplyNextIfNeeded();
        return next != null;
    }

    @Override
    public Path next() {
        supplyNextIfNeeded();
        Path value = next;
        next = null;
        return value;
    }

    private class WalkNode {

        private WalkNode parent;
        private Path path;
        private Iterator<Path> iterator;

        public WalkNode(WalkNode parent, Path path) throws IOException {
            this.parent = parent;
            this.path = path;
            this.iterator = Files.list(path).iterator();
        }

        public Path next() throws IOException {
            if (iterator.hasNext()) {
                Path next = iterator.next();

                if (Files.isDirectory(next)) {
                    currentNode = new WalkNode(this, next);
                    return currentNode.next();
                }

                return next;
            }

            currentNode = parent;
            return path;
        }

    }

}
