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
package org.nabucco.testautomation.engine.base.context;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nabucco.testautomation.engine.base.engine.ExecutionController;
import org.nabucco.testautomation.engine.base.util.PropertyHelper;

import org.nabucco.testautomation.facade.datatype.engine.SubEngineType;
import org.nabucco.testautomation.facade.datatype.engine.proxy.ProxyConfiguration;
import org.nabucco.testautomation.facade.datatype.property.base.Property;
import org.nabucco.testautomation.facade.datatype.property.base.PropertyComposite;
import org.nabucco.testautomation.result.facade.datatype.TestConfigurationResult;
import org.nabucco.testautomation.script.facade.datatype.dictionary.TestScript;
import org.nabucco.testautomation.script.facade.datatype.dictionary.base.TestScriptElement;

/**
 * TestContext
 * 
 * Context holding generic properties for the execution of the test and its
 * configuration.
 * 
 * @author Steffen Schmidt, PRODYNA AG
 */
public class TestContext implements Serializable, Cloneable {

	private static final long serialVersionUID = 1L;

	private static final String REGEX_SPLITTER = "\\.";

	private static final String SPLITTER = ".";

    /**
     * PropertyId for the brand of the current TestSheet.
     */
    public static final String BRAND = "global_brand";

    /**
     * PropertyId for the release of the current TestSheet.
     */
    public static final String RELEASE = "global_release";

    /**
     * PropertyId for the environment of the current TestSheet.
     */
    public static final String ENVIRONMENT = "global_environment";

    /**
     * PropertyId for the current username.
     */
    public static final String USERNAME = "global_username";

    /**
     * PropertyId for notices.
     */
    public static final String NOTICE = "global_notice";

    /**
     * PropertyId for the email-address.
     */
    public static final String EMAIL = "global_email";

    private final Map<String, Property> propertyMap;
    
    private Map<SubEngineType, ProxyConfiguration> proxyConfigurations;
    
    private ExecutionController executionController;
    
    private TestScript currentTestScript;
    
    private TestScriptElement currentTestScriptElement;
    
    private TestConfigurationResult testConfigurationResult;
    
    private boolean tracing = false;
    
    /**
     * Constructs a new TestContext instance.
     */
    public TestContext() {
        this.propertyMap = new HashMap<String, Property>();
        this.proxyConfigurations= new HashMap<SubEngineType, ProxyConfiguration>();
    }

    /**
     * Puts a property into the context.
     * 
     * @param property the property to add
     */
    public void put(Property property) {
    	
    	if (property == null || property.getName() == null) {
    		return;
    	}
        this.propertyMap.put(property.getName().getValue(), property);
    }

    /**
     * Gets a property from the context.
     * 
     * @param propertyId the id to lookup
     * @return the property or null, if not found
     */
    public Property getProperty(String propertyId) {
        
        if (propertyId == null) {
            return null;
        } else if (propertyId.contains(SPLITTER)) {
            String[] props = propertyId.split(REGEX_SPLITTER);
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
            return this.propertyMap.get(propertyId);
        }
    }
    
    /**
     * Gets a list of all properties contained by this context.
     * @return
     */
    public List<Property> getAll() {
        return new ArrayList<Property>(this.propertyMap.values());
    }

    /**
     * Puts each property in a given list to the context.
     * 
     * @param properties the property list to add
     */
    public void addAll(List<Property> properties) {
        
    	if (properties == null) {
    		return;
    	}
    	
    	for (Property prop : properties) {
    		
    		if (prop != null && prop.getName() != null) {
    			this.propertyMap.put(prop.getName().getValue(), prop);
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
     * @param property the property to remove
     */
    public void remove(Property property) {
    	
    	if (property == null || property.getName() == null) {
    		return;
    	}
        this.propertyMap.remove(property.getName().getValue());
    }
    
    public void setCurrentTestScript(TestScript currentTestScript) {
        this.currentTestScript = currentTestScript;
    }

    public TestScript getCurrentTestScript() {
        return currentTestScript;
    }

    public void setCurrentTestScriptElement(TestScriptElement currentTestScriptElement) {
        this.currentTestScriptElement = currentTestScriptElement;
    }

    public TestScriptElement getCurrentTestScriptElement() {
        return currentTestScriptElement;
    }
    
    public void setTracingEnabled(boolean enabled) {
    	this.tracing = enabled;
    }
    
    public boolean isTracingEnabled() {
    	return this.tracing;
    }

	/**
	 * @param testConfigurationResult the testConfigurationResult to set
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
	 * Creates a pseudo-clone. Only the contained properties are cloned.
	 * 
	 * @return the duplicated TestContext
	 */
	public TestContext dublicate() {
		
		TestContext clone = new TestContext();
		
		for (Property property : this.propertyMap.values()) {
			clone.put(property.cloneObject());
		}
		clone.proxyConfigurations = this.proxyConfigurations;
		clone.executionController = this.executionController;
		clone.testConfigurationResult = this.testConfigurationResult;
		clone.tracing = this.tracing;
		return clone;
	}
	
}
