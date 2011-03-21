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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.nabucco.framework.base.facade.datatype.DatatypeState;
import org.nabucco.framework.base.facade.datatype.file.TextFileContent;
import org.nabucco.testautomation.engine.base.context.TestContext;
import org.nabucco.testautomation.engine.base.exception.PropertyException;

import org.nabucco.testautomation.facade.datatype.PropertyFactory;
import org.nabucco.testautomation.facade.datatype.base.BooleanValue;
import org.nabucco.testautomation.facade.datatype.base.DateValue;
import org.nabucco.testautomation.facade.datatype.base.DoubleValue;
import org.nabucco.testautomation.facade.datatype.base.IntegerValue;
import org.nabucco.testautomation.facade.datatype.base.LongValue;
import org.nabucco.testautomation.facade.datatype.base.SqlValue;
import org.nabucco.testautomation.facade.datatype.base.StringValue;
import org.nabucco.testautomation.facade.datatype.base.XPathValue;
import org.nabucco.testautomation.facade.datatype.base.XmlValue;
import org.nabucco.testautomation.facade.datatype.property.BooleanProperty;
import org.nabucco.testautomation.facade.datatype.property.DateProperty;
import org.nabucco.testautomation.facade.datatype.property.DoubleProperty;
import org.nabucco.testautomation.facade.datatype.property.FileProperty;
import org.nabucco.testautomation.facade.datatype.property.IntegerProperty;
import org.nabucco.testautomation.facade.datatype.property.LongProperty;
import org.nabucco.testautomation.facade.datatype.property.PropertyList;
import org.nabucco.testautomation.facade.datatype.property.SqlProperty;
import org.nabucco.testautomation.facade.datatype.property.StringProperty;
import org.nabucco.testautomation.facade.datatype.property.XPathProperty;
import org.nabucco.testautomation.facade.datatype.property.XmlProperty;
import org.nabucco.testautomation.facade.datatype.property.base.Property;
import org.nabucco.testautomation.facade.datatype.property.base.PropertyComposite;
import org.nabucco.testautomation.facade.datatype.property.base.PropertyContainer;
import org.nabucco.testautomation.facade.datatype.property.base.PropertyType;

/**
 * PropertyHelper
 * 
 * @author Steffen Schmidt, PRODYNA AG
 */
public abstract class PropertyHelper {

    private PropertyHelper() {}
    
    /**
     * 
     * @param propertyList
     * @param type
     * @return
     */
    public static boolean contains(PropertyComposite propertyList, PropertyType type) {
    	
    	if (propertyList == null) {
    		return false;
    	}
    	
    	for (PropertyContainer container : propertyList.getPropertyList()) {
    		Property property = container.getProperty();
    		
    		if (property.getType() == type) {
    			return true;
    		}
    	}
    	return false;
    }
    
    /**
     * Returns the first Property of the given type.
     *  
     * @param propertyList
     * @param type
     * @return
     */
    public static Property getFromList(PropertyList propertyList, PropertyType type) {
    	
    	if (propertyList == null) {
    		return null;
    	}
    	
    	for (PropertyContainer container : propertyList.getPropertyList()) {
    		Property property = container.getProperty();
    		
    		if (property.getType() == type) {
    			return property;
    		}
    	}
    	return null;
    }
    
    /**
     * Returns the Property of the given type and name or null, if not found.
     *  
     * @param propertyList
     * @param type
     * @return
     */
    public static Property getFromList(PropertyList propertyList, PropertyType type, String name) {
    	
    	if (propertyList == null) {
    		return null;
    	}
    	
    	for (PropertyContainer container : propertyList.getPropertyList()) {
    		Property property = container.getProperty();
    		
    		if (property.getType() == type && property.getName().getValue().equals(name)) {
    			return property;
    		}
    	}
    	return null;
    }
    
    /**
     * 
     * @param propertyList
     * @return
     */
    public static List<Property> extract(List<PropertyContainer> propertyList) {
    	
    	if (propertyList == null) {
    		return null;
    	}
    	
    	List<Property> list = new ArrayList<Property>();
    	
    	for (PropertyContainer container : propertyList) {
			list.add(container.getProperty());
		}
    	return list;
    }
    
    /**
     * 
     * @param propertyList
     * @param name
     * @return
     */
    public static Property getFromList(PropertyList propertyList, String name) {
    	
    	if (propertyList == null) {
    		return null;
    	}
        return getFromContainerList(propertyList.getPropertyList(), name);
    }
    
    /**
     * 
     * @param propertyList
     * @param name
     * @return
     */
    public static Property getFromList(List<Property> propertyList, String name) {
		
    	if (propertyList == null) {
    		return null;
    	}
    	
    	for (Property prop : propertyList) {
		
    		if (prop.getName() != null && prop.getName().getValue() != null
					&& prop.getName().getValue().equals(name)) {
				return prop;
			}
		}
        return null;
    }
    
    /**
     * 
     * @param propertyList
     * @param name
     * @return
     */
    public static Property getFromContainerList(List<PropertyContainer> propertyList, String name) {
		
    	if (propertyList == null) {
    		return null;
    	}
    	
    	for (PropertyContainer container : propertyList) {
    		Property prop = container.getProperty();
    		
    		if (prop.getName() != null && prop.getName().getValue() != null
					&& prop.getName().getValue().equals(name)) {
				return prop;
			}
		}
        return null;
    }
    
    /**
     * 
     * @param name
     * @param value
     * @return
     */
    public static BooleanProperty createBooleanProperty(String name, Boolean value) {
        BooleanProperty prop = (BooleanProperty) PropertyFactory.getInstance().produceProperty(PropertyType.BOOLEAN);
        prop.setName(name);
        prop.setValue(value);
        return prop;
    }
    
    /**
     * 
     * @param name
     * @param value
     * @return
     */
    public static DateProperty createDateProperty(String name, Date value) {
        DateProperty prop = (DateProperty) PropertyFactory.getInstance().produceProperty(PropertyType.DATE);
        prop.setName(name);
        prop.setValue(value);
        return prop;
    }
    
    /**
     * 
     * @param name
     * @param value
     * @return
     */
    public static IntegerProperty createIntegerProperty(String name, Integer value) {
        IntegerProperty prop =(IntegerProperty) PropertyFactory.getInstance().produceProperty(PropertyType.INTEGER);
        prop.setName(name);
        prop.setValue(value);
        return prop;
    }
    
    /**
     * 
     * @param name
     * @param value
     * @return
     */
    public static DoubleProperty createDoubleProperty(String name, Double value) {
        DoubleProperty prop = (DoubleProperty) PropertyFactory.getInstance().produceProperty(PropertyType.DOUBLE);
        prop.setName(name);
        prop.setValue(value);
        return prop;
    }
    
    /**
     * 
     * @param name
     * @param value
     * @return
     */
    public static LongProperty createLongProperty(String name, Long value) {
        LongProperty prop = (LongProperty) PropertyFactory.getInstance().produceProperty(PropertyType.LONG);
        prop.setName(name);
        prop.setValue(value);
        return prop;
    }
    
    /**
     * 
     * @param name
     * @param value
     * @return
     */
    public static StringProperty createStringProperty(String name, String value) {
        StringProperty prop = (StringProperty) PropertyFactory.getInstance().produceProperty(PropertyType.STRING);
        prop.setName(name);
        prop.setValue(value);
        return prop;
    }
    
    /**
     * 
     * @param name
     * @param value
     * @return
     */
    public static XmlProperty createXmlProperty(String name, String value) {
        XmlProperty prop = (XmlProperty) PropertyFactory.getInstance().produceProperty(PropertyType.XML);
        prop.setName(name);
        prop.setValue(value);
        return prop;
    }
    
    /**
     * 
     * @param name
     * @param value
     * @return
     */
    public static XPathProperty createXPathProperty(String name, String value) {
        XPathProperty prop = (XPathProperty) PropertyFactory.getInstance().produceProperty(PropertyType.XPATH);
        prop.setName(name);
        prop.setValue(value);
        return prop;
    }
    
    /**
     * 
     * @param name
     * @return
     */
    public static PropertyList createPropertyList(String name) {
        PropertyList prop = (PropertyList) PropertyFactory.getInstance().produceProperty(PropertyType.LIST);
        prop.setName(name);
        return prop;
    }
    
    /**
     * 
     * @param name
     * @param value
     * @return
     */
    public static FileProperty createFileProperty(String name, String value) {
    	FileProperty prop = (FileProperty) PropertyFactory.getInstance().produceProperty(PropertyType.FILE);
        prop.setName(name);
        prop.setContent(value);
        return prop;
    }    
    
    /**
     * 
     * @param name
     * @param value
     * @return
     */
    public static SqlProperty createSqlProperty(String name, String value) {
    	SqlProperty prop = (SqlProperty) PropertyFactory.getInstance().produceProperty(PropertyType.SQL);
        prop.setName(name);
        prop.setValue(value);
        return prop;
    }    
    
    /**
     * 
     * @param propertyType
     * @param name
     * @return
     */
    public static Property createProperty(PropertyType propertyType, String name) {
        Property property = PropertyFactory.getInstance().produceProperty(propertyType);
        property.setName(name);
        return property;
    }
    
    /**
     * 
     * @param property
     * @return
     */
    public static String toString(Property property) {
        
        if (property == null) {
            return null;
        }
        
        switch (property.getType()) {
        
        case BOOLEAN: {
            BooleanValue value = ((BooleanProperty) property).getValue();
            
            if (value == null || value.getValue() == null) {
            	return null;
            }
			return value.getValue().toString();
        }
        case DATE: {
            DateValue value = ((DateProperty) property).getValue();
            
            if (value == null || value.getValue() == null) {
            	return null;
            }
			return value.getValue().toString();
        }
        case DOUBLE: {
            DoubleValue value = ((DoubleProperty) property).getValue();
			
            if (value == null || value.getValue() == null) {
            	return null;
            }
            return value.getValue().toString();
        }
        case INTEGER: {
            IntegerValue value = ((IntegerProperty) property).getValue();
			
            if (value == null || value.getValue() == null) {
            	return null;
            }
            return value.getValue().toString();
        }
        case LONG: {
        	LongValue value = ((LongProperty) property).getValue();

        	if (value == null || value.getValue() == null) {
            	return null;
            }
        	return value.getValue().toString();
        }
        case STRING: {
            StringValue value = ((StringProperty) property).getValue();
			
            if (value == null) {
            	return null;
            }
            return value.getValue();
        }
        case XML: {
            XmlValue value = ((XmlProperty) property).getValue();
			
            if (value == null) {
            	return null;
            }
            return value.getValue();
        }
        case XPATH: {
            XPathValue value = ((XPathProperty) property).getValue();
			
            if (value == null) {
            	return null;
            }
            return value.getValue();
        }
        case FILE: {
            TextFileContent content = ((FileProperty) property).getContent();
			
            if (content == null) {
            	return null;
            }
            return content.getValue();
        } 
        case SQL: {
            SqlValue value = ((SqlProperty) property).getValue();
			
            if (value == null) {
            	return null;
            }
            return value.getValue();  
        }      
        case LIST:
        	return printList((PropertyList) property);
        default:
            return "n/a";
        }
        
    }
    
    /**
     * 
     * @param list
     * @return
     */
    private static String printList(PropertyList list) {
    	
    	StringBuilder sb = new StringBuilder();
    	sb.append("[");
    	
    	for (PropertyContainer container : list.getPropertyList()) {
    		Property prop = container.getProperty();
    		sb.append(toString(prop));
    		sb.append(", ");    		
    	}
    	
    	if (sb.length() > 1) {
    		sb.delete(sb.length() - 2, sb.length());
    	}
    	sb.append("]");
    	return sb.toString();
    }
    
    /**
     * 
     * @param property
     * @return
     */
    public static Object getValue(Property property) {
        
        if (property == null) {
            return null;
        }
        
        switch (property.getType()) {
        
        case BOOLEAN: {
            BooleanValue value = ((BooleanProperty) property).getValue();
            
            if (value == null) {
            	return null;
            }
			return value.getValue();
        }
        case DATE: {
            DateValue value = ((DateProperty) property).getValue();
            
            if (value == null) {
            	return null;
            }
			return value.getValue();
        }
        case DOUBLE: {
            DoubleValue value = ((DoubleProperty) property).getValue();
			
            if (value == null) {
            	return null;
            }
            return value.getValue();
        }
        case INTEGER: {
            IntegerValue value = ((IntegerProperty) property).getValue();
			
            if (value == null) {
            	return null;
            }
            return value.getValue();
        }
        case LIST: {
        	List<PropertyContainer> children = ((PropertyList) property).getPropertyList();
        	
        	if (children == null || children.isEmpty()) {
        		return 0;
        	}
        	return children.size();
        }
        case LONG: {
        	LongValue value = ((LongProperty) property).getValue();

        	if (value == null) {
            	return null;
            }
        	return value.getValue();
        }
        case STRING: {
            StringValue value = ((StringProperty) property).getValue();
			
            if (value == null) {
            	return null;
            }
            return value.getValue();
        }
        case XML: {
            XmlValue value = ((XmlProperty) property).getValue();
			
            if (value == null) {
            	return null;
            }
            return value.getValue();
        }
        case XPATH: {
            XPathValue value = ((XPathProperty) property).getValue();
			
            if (value == null) {
            	return null;
            }
            return value.getValue();
        }
        case FILE: {
            TextFileContent content = ((FileProperty) property).getContent();
			
            if (content == null) {
            	return null;
            }
            return content.getValue();
        } 
        case SQL: {
            SqlValue value = ((SqlProperty) property).getValue();
			
            if (value == null) {
            	return null;
            }
            return value.getValue();  
        }      
        default:
            return null;
        }
        
    }
    
    /**
     * 
     * @param props
     * @param ctx
     * @throws PropertyException
     */
    public static void resolvePropertyRefs(PropertyList props, TestContext ctx) throws PropertyException {
		
    	if (props == null) {
    		return;
    	}
    	
		for (PropertyContainer container : props.getPropertyList()) {
			Property prop = container.getProperty();			
			
			if ((prop.getName() == null || prop.getName().getValue() == null)
					&& prop.getReference() != null
					&& prop.getReference().getValue() != null) {
				String nameRef = prop.getReference().getValue();
				Property refProperty = ctx.getProperty(nameRef);
				
				if (refProperty == null) {
					throw new PropertyException("PropertyReference '" + nameRef + "' could not be resolved");
				}
				
				try {
					switch (prop.getType()) {
					case BOOLEAN:
						BooleanProperty booleanProp = (BooleanProperty) prop;
						booleanProp.setName(refProperty.getName());
						booleanProp.setValue(((BooleanProperty) refProperty).getValue());
						break;
					case DATE:
						DateProperty dateProp = (DateProperty) prop;
						dateProp.setName(refProperty.getName());
						dateProp.setValue(((DateProperty) refProperty).getValue());
						break;
					case DOUBLE:
						DoubleProperty doubleProp = (DoubleProperty) prop;
						doubleProp.setName(refProperty.getName());
						doubleProp.setValue(((DoubleProperty) refProperty).getValue());
						break;
					case INTEGER:
						IntegerProperty integerProp = (IntegerProperty) prop;
						integerProp.setName(refProperty.getName());
						integerProp.setValue(((IntegerProperty) refProperty).getValue());
						break;
					case LONG:
						LongProperty longProp = (LongProperty) prop;
						longProp.setName(refProperty.getName());
						longProp.setValue(((LongProperty) refProperty).getValue());
						break;
					case STRING:
						StringProperty stringProp = (StringProperty) prop;
						stringProp.setName(refProperty.getName());
						stringProp.setValue(((StringProperty) refProperty).getValue());
						break;
					case XML:
						XmlProperty xmlProp = (XmlProperty) prop;
						xmlProp.setName(refProperty.getName());
						xmlProp.setValue(((XmlProperty) refProperty).getValue());
						break;
					case XPATH:
						XPathProperty xpathProp = (XPathProperty) prop;
						xpathProp.setName(refProperty.getName());
						xpathProp.setValue(((XPathProperty) refProperty).getValue());
						break;
					case FILE:
						FileProperty fileProp = (FileProperty) prop;
						fileProp.setName(refProperty.getName());
						fileProp.setContent(((FileProperty) refProperty).getContent());
						break;
					case SQL:
						SqlProperty sqlProp = (SqlProperty) prop;
						sqlProp.setName(refProperty.getName());
						sqlProp.setValue(((SqlProperty) refProperty).getValue());
						break;
					default:
						throw new PropertyException("Unsupported PropertyType referenced in resolvePropertyRefs: "
								+ prop.getType());
					}
				} catch (ClassCastException ex) {
					throw new PropertyException("Invalid PropertyType found in resolvePropertyRefs: "
							+ (prop != null ? prop.getClass() : "null")
							+ " -> "
							+ (refProperty != null ? refProperty.getClass()
									: "null"));
				}
			}
		}
	}
    
    /**
     * 
     * @param property
     * @param list
     */
    public static void add(Property property, PropertyList list) {
    	
    	if (property == null || list == null) {
    		return;
    	}
    	
    	PropertyContainer container = new PropertyContainer();
    	container.setDatatypeState(DatatypeState.INITIALIZED);
    	container.setOrderIndex(list.getPropertyList().size());
    	container.setProperty(property);
    	list.getPropertyList().add(container);
    }
    
}
