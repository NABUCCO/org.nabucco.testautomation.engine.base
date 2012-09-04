/*
 * Copyright 2012 PRODYNA AG
 *
 * Licensed under the Eclipse Public License (EPL), Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/eclipse-1.0.php or
 * http://www.nabucco.org/License.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.nabucco.testautomation.engine.base.element.config.base;

/**
 * Skipable
 * 
 * A marker interface to enable elements (e.g. TestCase, TestStep) be be skipped.
 * 
 * @author Steffen Schmidt (PRODYNA AG)
 * 
 */
public interface Skipable {

    /**
     * Marks a TestConfigElement to be skipped or not.
     * 
     * @param skipped
     *            true, if the element is supposed to be skipped, false otherwise
     */
    public void setSkipped(boolean skipped);

    /**
     * Returns if the element is marked to be skipped or not.
     * 
     * @return true, if the element is marked to be skipped, otherwise false
     */
    public boolean isSkipped();

}
