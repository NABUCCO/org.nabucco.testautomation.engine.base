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
package org.nabucco.testautomation.engine.base.config;

import java.util.Properties;

/**
 * NBCTestProperties
 * 
 * @author Frank Ratschinski, PRODYNA AG
 * 
 */
public class NBCTestProperties extends Properties {

    private static final long serialVersionUID = 1L;

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized Object setProperty(String key, String value) {

        if (value != null) {
            value = value.trim();
        }

        return super.setProperty(key, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized Object put(Object key, Object value) {
        if (value != null) {
            if (value instanceof String) {
                value = ((String) value).trim();
            }
        }
        return super.put(key, value);
    }

}
