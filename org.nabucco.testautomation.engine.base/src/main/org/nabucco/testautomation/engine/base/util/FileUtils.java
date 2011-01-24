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
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.nabucco.testautomation.engine.base.config.NBCTestProperties;


/**
 * FileUtils
 * 
 * @author Steffen Schmidt, PRODYNA AG
 * 
 */
public final class FileUtils {

    private FileUtils() {
    }

    /**
     * Gets the given file location as URL.
     * 
     * @param file
     *            the file
     * @return the URL representing the given file
     * @throws MalformedURLException
     *             thrown, if the URL cannot be created
     */
    public static final URL toURL(File file) throws MalformedURLException {
        String url = "file:///" + file.getAbsolutePath();
        return new URL(url);
    }

    /**
     * Creates the given path.
     * 
     * @param path
     *            the path to create
     * @return a File object representing the new path
     */
    public static final File createPath(String path) {
        File destPath = new File(path);

        if (destPath.isFile()) {
            destPath = destPath.getParentFile();
        }
        if (!destPath.exists()) {
            destPath.mkdirs();
        }
        return destPath;
    }

    /**
     * Reads a given file from a given path and creates properties from it.
     * 
     * @param path
     *            the source path
     * @param propFile
     *            the property-file
     * @return an NBCTestProperties created from the file
     * @throws IOException
     *             thrown, if the source file does not exist or cannot be read
     */
    public static final NBCTestProperties getProperties(File path, String propFile)
            throws IOException {
        NBCTestProperties props = new NBCTestProperties();
        props.load(new FileReader(new File(path, propFile)));
        return props;
    }

    /**
     * Finds all files in a given path and adds them to a given list. The operation may also iterate
     * through all subdirectories.
     * 
     * @param rootPath
     *            the path to search
     * @param list
     *            the list to add the files to
     * @param deep
     *            if true, then all subdirectories will also be used for searching
     */
    public static final void addFilesToList(File rootPath, List<File> list, boolean deep) {
        File[] files = rootPath.listFiles();

        for (File file : files) {
            if (file.isFile()) {
                list.add(file);
            } else if (file.isDirectory() && deep) {
                addFilesToList(file, list, deep);
            }
        }
    }

    /**
     * Deletes a given file or path and all of its subdirectories.
     * 
     * @param root
     *            the file or path to be deleted
     */
    public static final void delete(File root) {

        if (root == null) {
            return;
        }
        if (root.isFile()) {
            root.delete();
        } else if (root.isDirectory()) {
            File[] files = root.listFiles();
            for (File file : files) {
                if (file.isDirectory()) {
                    delete(file);
                } else {
                    file.delete();
                }
            }
            root.delete();
        }
    }

}
