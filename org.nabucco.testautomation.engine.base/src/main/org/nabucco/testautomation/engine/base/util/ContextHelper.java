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
package org.nabucco.testautomation.engine.base.util;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.nabucco.testautomation.engine.base.context.TestContext;
import org.nabucco.testautomation.engine.base.exception.PropertyException;
import org.nabucco.testautomation.property.facade.datatype.BooleanProperty;
import org.nabucco.testautomation.property.facade.datatype.DateProperty;
import org.nabucco.testautomation.property.facade.datatype.FileProperty;
import org.nabucco.testautomation.property.facade.datatype.NumericProperty;
import org.nabucco.testautomation.property.facade.datatype.SqlProperty;
import org.nabucco.testautomation.property.facade.datatype.TextProperty;
import org.nabucco.testautomation.property.facade.datatype.XPathProperty;
import org.nabucco.testautomation.property.facade.datatype.XmlProperty;
import org.nabucco.testautomation.property.facade.datatype.base.Property;
import org.nabucco.testautomation.property.facade.datatype.base.PropertyComposite;
import org.nabucco.testautomation.property.facade.datatype.base.PropertyContainer;
import org.nabucco.testautomation.property.facade.datatype.base.PropertyReference;
import org.nabucco.testautomation.property.facade.datatype.util.PropertyHelper;

/**
 * ContextHelper
 * 
 * @author Steffen Schmidt, PRODYNA AG
 */
public abstract class ContextHelper {

    private static final String EMPTY_STRING = "";

    private ContextHelper() {
    }

    /**
     * Iterates over the given {@link PropertyComposite} and recursively replaces all references
     * with values from the given {@link TestContext}.
     * 
     * @param properties
     *            the PropertyComposite to iterate over
     * @param ctx
     *            the TestContext
     * @throws PropertyException
     *             thrown, if an error occurs, e.g. if an PropertyReference cannot be resolved
     */
    public static void resolvePropertyRefs(PropertyComposite properties, TestContext ctx) throws PropertyException {

        if (properties == null || ctx == null) {
            return;
        }

        for (PropertyContainer container : properties.getPropertyList()) {
            Property property = container.getProperty();
            PropertyReference reference = property.getReference();

            if (reference != null && reference.getValue() != null && !reference.getValue().equals(EMPTY_STRING)) {
                Property referencedProperty = ctx.getProperty(reference);

                if (referencedProperty == null) {
                    throw new PropertyException("PropertyReference '" + reference + "' could not be resolved.");
                } 
                
                switch (property.getType()) {
                case TEXT:
                    ((TextProperty) property).setValue(PropertyHelper.toString(referencedProperty));
                    break;
                case SQL:
                    ((SqlProperty) property).setValue(PropertyHelper.toString(referencedProperty));
                    break;
                case XML:
                    ((XmlProperty) property).setValue(PropertyHelper.toString(referencedProperty));
                    break;
                case XPATH:
                    ((XPathProperty) property).setValue(PropertyHelper.toString(referencedProperty));
                    break;
                case BOOLEAN:
                    ((BooleanProperty) property).setValue(Boolean.parseBoolean(PropertyHelper.toString(referencedProperty)));
                    break;
                case NUMERIC:
                    ((NumericProperty) property).setValue(new BigDecimal(PropertyHelper.toString(referencedProperty)));
                    break;
                case DATE:
                    ((DateProperty) property).setValue(parseDate(PropertyHelper.toString(referencedProperty)));
                    break;
                case FILE:
                    ((FileProperty) property).setContent(PropertyHelper.toString(referencedProperty));
                    break;
                case LIST:
                    Property clonedProperty = referencedProperty.cloneObject();
                    clonedProperty.setName(property.getName().getValueAsString());
                    container.setProperty(clonedProperty);
                    break;
                }
            } else if (property instanceof PropertyComposite) {
                resolvePropertyRefs((PropertyComposite) property, ctx);
            }
        }
    }
    
    private static Date parseDate(String dateString) throws PropertyException {
        try {
            return new SimpleDateFormat(PropertyHelper.DEFAULT_DATE_PATTERN).parse(dateString);
        } catch (ParseException e) {
            throw new PropertyException("Could not parse date string: " + dateString);
        }
    }
    
}
