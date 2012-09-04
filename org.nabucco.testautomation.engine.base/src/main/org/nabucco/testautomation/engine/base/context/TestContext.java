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
package org.nabucco.testautomation.engine.base.context;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nabucco.framework.base.facade.datatype.Name;
import org.nabucco.testautomation.config.facade.datatype.TestConfigElement;
import org.nabucco.testautomation.engine.base.engine.ExecutionController;
import org.nabucco.testautomation.property.facade.datatype.PropertyList;
import org.nabucco.testautomation.property.facade.datatype.base.HierarchyLevelType;
import org.nabucco.testautomation.property.facade.datatype.base.Property;
import org.nabucco.testautomation.property.facade.datatype.base.PropertyComponent;
import org.nabucco.testautomation.property.facade.datatype.base.PropertyComposite;
import org.nabucco.testautomation.property.facade.datatype.base.PropertyContainer;
import org.nabucco.testautomation.property.facade.datatype.base.PropertyReference;
import org.nabucco.testautomation.property.facade.datatype.util.PropertyHelper;
import org.nabucco.testautomation.result.facade.datatype.TestConfigurationResult;
import org.nabucco.testautomation.script.facade.datatype.dictionary.TestScript;
import org.nabucco.testautomation.script.facade.datatype.dictionary.base.TestScriptElement;
import org.nabucco.testautomation.settings.facade.datatype.engine.SubEngineType;
import org.nabucco.testautomation.settings.facade.datatype.engine.proxy.ProxyConfiguration;

/**
 * TestContext
 * 
 * Context holding generic properties for the execution of the test and its configuration.
 * 
 * @author Steffen Schmidt, PRODYNA AG
 */
public final class TestContext implements Serializable, Cloneable {

    private static final long serialVersionUID = 1L;

    private static final String REGEX_SPLITTER = "\\.";

    private static final String SPLITTER = ".";

    /**
     * PropertyId for the brand of the current TestSheet.
     */
    public static final Name BRAND = new Name("global_brand");

    /**
     * PropertyId for the release of the current TestSheet.
     */
    public static final Name RELEASE = new Name("global_release");

    /**
     * PropertyId for the environment of the current TestSheet.
     */
    public static final Name ENVIRONMENT = new Name("global_environment");

    /**
     * PropertyId for the current username.
     */
    public static final Name USERNAME = new Name("global_username");

    /**
     * PropertyId for notices.
     */
    public static final Name NOTICE = new Name("global_notice");

    /**
     * PropertyId for the email-address.
     */
    public static final Name EMAIL = new Name("global_email");

    private final Map<Name, Property> propertyMap;

    private Map<SubEngineType, ProxyConfiguration> proxyConfigurations;

    private ExecutionController executionController;

    private Map<HierarchyLevelType, TestConfigElement> currentTestConfigElement;

    private TestScript currentTestScript;

    private TestScriptElement currentTestScriptElement;

    private TestConfigurationResult testConfigurationResult;

    private boolean tracing = false;

    /**
     * Constructs a new TestContext instance.
     */
    public TestContext() {
        this.propertyMap = new HashMap<Name, Property>();
        this.currentTestConfigElement = new HashMap<HierarchyLevelType, TestConfigElement>();
    }

    /**
     * Puts a property into the context.
     * 
     * @param property
     *            the property to add
     */
    public void put(Property property) {

        if (property == null || property.getName() == null) {
            return;
        }
        this.propertyMap.put(property.getName(), property);
    }

    /**
     * Merges the given {@link PropertyList} into the TestContext.
     * 
     * @param propertyList
     *            the PropertyList to merge into the context
     */
    public void merge(PropertyList propertyList) {

        PropertyList existingPropertyList = (PropertyList) this.propertyMap.get(propertyList.getName());

        // No merging if PropertyList does not exists in context
        if (existingPropertyList == null) {
            this.put(propertyList);
            return;
        }

        // Merge
        merge(propertyList, existingPropertyList);
    }

    /**
     * Merges Properties from one {@link PropertyComposite} to another.
     * 
     * @param from
     *            PropertyComposite to merge from
     * @param to
     *            PropertyComposite to merge to
     */
    private void merge(PropertyComposite from, PropertyComposite to) {

        for (PropertyContainer container : from.getPropertyList()) {
            Property prop = container.getProperty();

            if (prop instanceof PropertyComposite) {
                PropertyComposite existing = (PropertyComposite) PropertyHelper.getFromList(to, prop.getType(), prop
                        .getName().getValue());

                if (existing == null) {
                    PropertyHelper.add(prop, to);
                } else {
                    merge((PropertyComposite) prop, existing);
                }
            } else {
                PropertyHelper.replace(to, (PropertyComponent) prop);
            }
        }
    }

    /**
     * Gets the property with the given name from the context.
     * 
     * @param propertyName
     *            the name to lookup
     * @return the property or null, if not found
     */
    public Property getProperty(Name propertyName) {

        if (propertyName == null || propertyName.getValue() == null) {
            return null;
        } else if (propertyName.getValue().contains(SPLITTER)) {
            String[] props = propertyName.getValue().split(REGEX_SPLITTER);
            List<Property> tempPropertyList = new ArrayList<Property>(this.propertyMap.values());
            Property property = null;

            for (String name : props) {
                property = PropertyHelper.getFromList(tempPropertyList, name);

                if (property == null) {
                    return null;
                }

                if (property instanceof PropertyComposite) {
                    tempPropertyList = PropertyHelper.extract(((PropertyComposite) property).getPropertyList());
                }
            }
            return property;
        } else {
            return this.propertyMap.get(propertyName);
        }
    }

    /**
     * Gets the property with the referenced name from the context.
     * 
     * @param propertyRef
     *            the reference
     * @return the property or null, if it is not contained by the context
     */
    public Property getProperty(PropertyReference propertyRef) {
        return getProperty(new Name(propertyRef.getValue()));
    }

    /**
     * Gets a list of all properties contained by this context.
     * 
     * @return
     */
    public List<Property> getAll() {
        return new ArrayList<Property>(this.propertyMap.values());
    }

    /**
     * Puts each property in a given list to the context.
     * 
     * @param properties
     *            the property list to add
     */
    public void addAll(List<Property> properties) {

        if (properties == null) {
            return;
        }

        for (Property prop : properties) {

            if (prop != null && prop.getName() != null) {
                this.propertyMap.put(prop.getName(), prop);
            }
        }
    }

    /**
     * Adds the given ProxyConfiguration of its specified {@link SubEngineType} to the context.
     * 
     * @param configuration
     *            the configuration to set
     */
    public void addProxyConfiguration(ProxyConfiguration configuration) {

        if (this.proxyConfigurations == null) {
            this.proxyConfigurations = new HashMap<SubEngineType, ProxyConfiguration>();
        }
        this.proxyConfigurations.put(configuration.getSubEngineType(), configuration);
    }

    /**
     * Gets the configuration properties of the given {@link SubEngineType} from the context.
     * 
     * @param type
     *            the type of the configuration properties
     * @return the configuration or null, if not found
     */
    public ProxyConfiguration getProxyConfiguration(SubEngineType type) {

        if (this.proxyConfigurations == null) {
            return null;
        }
        return this.proxyConfigurations.get(type);
    }

    /**
     * Sets the {@link ExecutionController} to the context.
     * 
     * @param executionController
     *            the execution controller to set
     */
    public void setExecutionController(ExecutionController executionController) {
        this.executionController = executionController;
    }

    /**
     * Gets the {@link ExecutionController} set to this context.
     * 
     * @return the ExecutionController
     */
    public ExecutionController getExecutionController() {
        return executionController;
    }

    /**
     * Removes a property from the context.
     * 
     * @param property
     *            the property to remove
     */
    public void remove(Property property) {

        if (property == null || property.getName() == null) {
            return;
        }
        this.propertyMap.remove(property.getName());
    }

    /**
     * 
     * @param currentTestScript
     */
    public void setCurrentTestScript(TestScript currentTestScript) {
        this.currentTestScript = currentTestScript;
    }

    /**
     * 
     * @return
     */
    public TestScript getCurrentTestScript() {
        return currentTestScript;
    }

    /**
     * 
     * @param currentTestScriptElement
     */
    public void setCurrentTestScriptElement(TestScriptElement currentTestScriptElement) {
        this.currentTestScriptElement = currentTestScriptElement;
    }

    /**
     * 
     * @return
     */
    public TestScriptElement getCurrentTestScriptElement() {
        return currentTestScriptElement;
    }

    /**
     * 
     * @param enabled
     */
    public void setTracingEnabled(boolean enabled) {
        this.tracing = enabled;
    }

    /**
     * 
     * @return
     */
    public boolean isTracingEnabled() {
        return this.tracing;
    }

    /**
     * @param testConfigurationResult
     *            the testConfigurationResult to set
     */
    public void setTestConfigurationResult(TestConfigurationResult testConfigurationResult) {
        this.testConfigurationResult = testConfigurationResult;
    }

    /**
     * @return the testConfigurationResult
     */
    public TestConfigurationResult getTestConfigurationResult() {
        return testConfigurationResult;
    }

    /**
     * Sets the current {@link TestConfigElement}.
     * 
     * @param element
     *            the TestConfigElement to set
     */
    public void setCurrentTestConfigElement(TestConfigElement element) {

        if (element == null || element.getSchemaElement() == null) {
            return;
        }

        if (this.currentTestConfigElement == null) {
            this.currentTestConfigElement = new HashMap<HierarchyLevelType, TestConfigElement>();
        }
        this.currentTestConfigElement.put(element.getSchemaElement().getLevel(), element);
    }

    /**
     * Gets the current {@link TestConfigElement} of a given {@link HierarchyLevelType}.
     * 
     * @param level
     *            the current TestConfigElement of the given level
     * @return
     */
    public TestConfigElement getCurrentTestConfigElement(HierarchyLevelType level) {

        if (level == null || this.currentTestConfigElement == null) {
            return null;
        }
        return this.currentTestConfigElement.get(level);
    }

    /**
     * Creates a pseudo-clone. Only the contained properties are cloned.
     * 
     * @return the duplicated TestContext
     */
    public TestContext dublicate() {

        TestContext clone = new TestContext();
        clone.tracing = this.tracing;

        for (Property property : this.propertyMap.values()) {
            clone.put(property.cloneObject());
        }
        clone.setProxyConfigurations(this.proxyConfigurations);
        clone.setExecutionController(this.executionController);
        clone.setTestConfigurationResult(this.testConfigurationResult);
        clone.setCurrentTestConfigElement(this.currentTestConfigElement);
        return clone;
    }

    protected void setProxyConfigurations(Map<SubEngineType, ProxyConfiguration> proxyConfigurations) {
        this.proxyConfigurations = proxyConfigurations;
    }

    protected void setCurrentTestConfigElement(Map<HierarchyLevelType, TestConfigElement> currentTestConfigElement) {
        this.currentTestConfigElement = currentTestConfigElement;
    }

}
