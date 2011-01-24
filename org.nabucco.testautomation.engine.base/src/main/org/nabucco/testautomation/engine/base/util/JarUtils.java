/*
* Copyright 2010 PRODYNA AG
*
* Licensed under the Eclipse Public License (EPL), Version 1.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.opensource.org/licenses/eclipse-1.0.php or
* http://www.nabucco-source.org/nabucco-license.html
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package org.nabucco.testautomation.engine.base.util;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.jar.JarInputStream;
import java.util.zip.ZipEntry;

/**
 * JarUtils
 * 
 * @author Steffen Schmidt, PRODYNA AG
 */
public final class JarUtils {

    private static final String JAR_FILE_SUFFIX = ".jar";

    private static final String PROPERTY_FILE_SUFFIX = ".properties";

    private static final String XML_FILE_SUFFIX = ".xml";

    private JarUtils() {
    }

    /**
     * Extracts a filterable set of files from a given jar-file into a given destination path.
     * 
     * @param jarFile
     *            the jar-file to extract from
     * @param destPath
     *            the destination path to put the extracted files
     * @param filter
     *            the file filter to filter the files to be extracted
     * @throws IOException
     *             thrown, if an error occurs during the extraction
     */
    public static final void extractFiles(File jarFile, File destPath, FileFilter filter)
            throws IOException {
        JarInputStream jin = new JarInputStream(new FileInputStream(jarFile));
        ZipEntry entry = jin.getNextEntry();

        while (entry != null) {
            String fileName = entry.getName();
            File tmpFile = new File(fileName);

            if (filter.accept(tmpFile)) {
                File targetFile = new File(destPath.toString()
                        + File.separator
                        + tmpFile.toString());
                File parent = targetFile.getParentFile();

                if (!parent.exists()) {
                    parent.mkdirs();
                }
                if (targetFile.getName().endsWith(JAR_FILE_SUFFIX)
                        || targetFile.getName().endsWith(PROPERTY_FILE_SUFFIX)
                        || targetFile.getName().endsWith(XML_FILE_SUFFIX)) {
                    targetFile.createNewFile();
                    targetFile.deleteOnExit();
                    dumpFile(jin, targetFile);
                    targetFile.setLastModified(entry.getTime());
                    targetFile.deleteOnExit();
                } else if (!targetFile.exists()) {
                    targetFile.mkdirs();
                }
                jin.closeEntry();
            }
            entry = jin.getNextEntry();
        }
        jin.close();
    }

    /**
     * Reads an entry from a JarInputStream and stores it in the given targetFile.
     * 
     * @param jin
     *            the input stream to read the file data from
     * @param targetFile
     *            the target file
     * @throws IOException
     *             thrown, if an error occurs
     */
    private static final void dumpFile(JarInputStream jin, File targetFile) throws IOException {
        OutputStream out = new FileOutputStream(targetFile);
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = jin.read(buffer, 0, buffer.length)) != -1) {
            out.write(buffer, 0, len);
        }
        out.flush();
        out.close();
    }

}
