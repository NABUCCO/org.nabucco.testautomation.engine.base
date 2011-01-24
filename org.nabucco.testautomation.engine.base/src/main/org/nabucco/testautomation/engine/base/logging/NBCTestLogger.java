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
package org.nabucco.testautomation.engine.base.logging;

/**
 * NBCTestLogger
 * 
 * @author Frank Ratschinski, PRODYNA AG
 */
public interface NBCTestLogger {

    /**
     * Severity 0.
     * 
     * @param e
     * @param message
     */
    void fatal(Exception e, String... message);

    /**
     * Severity 1.
     * 
     * @param e
     * @param message
     */
    void error(Exception e, String... message);

    /**
     * Severity 2.
     * 
     * @param e
     * @param message
     */
    void warning(Exception e, String... message);

    /**
     * Severity 3.
     * 
     * @param e
     * @param message
     */
    void info(Exception e, String... message);

    /**
     * Severity 4.
     * 
     * @param e
     * @param message
     */
    void debug(Exception e, String... message);

    /**
     * Severity 0.
     * 
     * @param message
     */
    void fatal(String... message);

    /**
     * Severity 1.
     * 
     * @param message
     */
    void error(String... message);

    /**
     * Severity 2.
     * 
     * @param message
     */
    void warning(String... message);

    /**
     * Severity 3.
     * 
     * @param message
     */
    void info(String... message);

    /**
     * Severity 4.
     * 
     * @param message
     */
    void debug(String... message);

    /**
     * Severity 5.
     * 
     * @param message
     */
    void trace(String... message);

}
