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
package org.nabucco.testautomation.engine.base.xml;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.Writer;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.nabucco.testautomation.engine.base.exception.NBCTestConfigurationException;
import org.nabucco.testautomation.engine.base.logging.NBCTestLogger;
import org.nabucco.testautomation.engine.base.logging.NBCTestLoggingFactory;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


/**
 * XMLToolkit
 * 
 * @author Frank Ratschinski, PRODYNA AG
 */
public final class XMLToolkit {

	private static final Object EMPTY_STRING = "";

    private static final String SINGLE_QUOT = "&#039;";

    private static final String QUOT = "&quot;";

    private static final String GT = "&gt;";

    private static final String LT = "&lt;";
    
    private static final Pattern PATTERN_LT = Pattern.compile(LT);
    
    private static final Pattern PATTERN_GT = Pattern.compile(GT);
    
    private static final Pattern PATTERN_QUOT = Pattern.compile(QUOT);
    
    private static final Pattern PATTERN_SINGLE_QUOT = Pattern.compile(SINGLE_QUOT);

    private static NBCTestLogger logger = NBCTestLoggingFactory.getInstance().getLogger(XMLToolkit.class);

    private static final List<Element> EMPTY_LIST = Collections.unmodifiableList(new ArrayList<Element>());

    /**
     * Gets a list of child elements with the given name from the given parent element.
     * 
     * @param element the parent element
     * @param tagname the name of the requested tags
     * @return a list with elements or an empty list, if none was found
     */
    public static final List<Element> getElementsbyTagName(Element element, String tagname) {

        if (tagname == null) {
            logger.warning("tagname is null");
            return EMPTY_LIST;
        }
        if (element == null) {
            logger.warning("element is null");
            return EMPTY_LIST;
        }

        NodeList nl = element.getElementsByTagName(tagname);
        if (nl.getLength() == 0) {
            return EMPTY_LIST;
        }

        List<Element> returnList = new ArrayList<Element>();
        for (int i = 0; i < nl.getLength(); i++) {
            Node n = nl.item(i);
            if (n.getNodeType() == Node.ELEMENT_NODE && n.getParentNode() == element) {
                returnList.add((Element) n);
            }
        }
        return returnList;
    }
    
    /**
     * Gets the child elements from the given parent element as list.
     * 
     * @param parent the parent element
     * @return the list of child elements
     */
    public static final List<Element> getChildren(Element parent) {
    	
    	if (parent == null) {
            logger.warning("element is null");
            return EMPTY_LIST;
        }
    	
    	NodeList nl = parent.getChildNodes();
    	List<Element> returnList = new ArrayList<Element>();
    	
    	for (int i = 0; i < nl.getLength(); i++) {
            Node n = nl.item(i);
            if (n.getNodeType() == Node.ELEMENT_NODE && n.getParentNode() == parent) {
                returnList.add((Element) n);
            }
        }
    	return returnList;
    }

    /**
     * Loads a XML file into a {@link Document}.
     * 
     * @param file the source file
     * @return the parsed XML document
     * @throws NBCTestConfigurationException thrown, if I/O failed or if the content could not be parsed. 
     */
    public static Document loadXMLDocument(File file) throws NBCTestConfigurationException {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(file);
            return doc;
        } catch (ParserConfigurationException e) {
            logger.warning(e, "ParserConfigurationException");
            throw new NBCTestConfigurationException(e);
        } catch (SAXException e) {
            logger.warning(e, "SAXException");
            throw new NBCTestConfigurationException(e);
        } catch (IOException e) {
            logger.warning(e, "IOException");
            throw new NBCTestConfigurationException(e);
        }

    }
    
    /**
     * Loads a XML string into a {@link Document}.
     * 
     * @param xml the source string
     * @return the parsed XML document
     * @throws NBCTestConfigurationException
     */
    public static Document loadXMLDocument(String xml) throws NBCTestConfigurationException {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new InputSource(new StringReader(xml)));
            return doc;
        } catch (ParserConfigurationException e) {
            logger.warning(e, "ParserConfigurationException");
            throw new NBCTestConfigurationException(e);
        } catch (SAXException e) {
            logger.warning(e, "SAXException");
            throw new NBCTestConfigurationException(e);
        } catch (IOException e) {
            logger.warning(e, "IOException");
            throw new NBCTestConfigurationException(e);
        }
        
    }

    /**
     * Writes a given {@link Document} into a target file.
     * 
     * @param document the document to write
     * @param target the target file
     * @throws NBCTestConfigurationException thrown, if I/O failed
     */
    public static void writeXMLDocument(Document document, File target) throws NBCTestConfigurationException {
        try {
            writeXMLDocument(document, new FileOutputStream(target));
        } catch (FileNotFoundException e) {
            logger.warning("File not found: " + target);
            throw new NBCTestConfigurationException(e);
        }
    }
    
    /**
     * Writes a given {@link Document} into a target {@link OutputStream}.
     * 
     * @param document the document to write
     * @param target the target stream
     * @throws NBCTestConfigurationException thrown, if I/O failed
     */
    public static void writeXMLDocument(Document document, OutputStream target) throws NBCTestConfigurationException {
        try {
             // Use a Transformer for output
            TransformerFactory tFactory =
              TransformerFactory.newInstance();
            Transformer transformer = tFactory.newTransformer();

            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(target);
            transformer.transform(source, result); 
        } catch (Exception e) {
            logger.warning("Could not write document to file " + target);
            throw new NBCTestConfigurationException(e);
        }
    }
    
    /**
     * Writes a given {@link Document} into a target {@link Writer}.
     * 
     * @param document document the document to write
     * @param target the target writer
     * @throws NBCTestConfigurationException thrown, if I/O failed
     */
    public static void writeXMLDocument(Document document, Writer target) throws NBCTestConfigurationException {
        try {
             // Use a Transformer for output
            TransformerFactory tFactory =
              TransformerFactory.newInstance();
            Transformer transformer = tFactory.newTransformer();

            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(target);
            transformer.transform(source, result); 
        } catch (Exception e) {
            logger.warning("Could not write document to file " + target);
            throw new NBCTestConfigurationException(e);
        }
    }
    
    /**
     * Returns the value of the named attribute from the given element.
     * This method does not return an empty string, if the attribute does 
     * not exist, but null.
     * 
     * @param e the element containing the requested attribute
     * @param attributeName the name of the requested attribute
     * @return the value of the attribute or null, if it does not exist
     */
    public static String getAttribute(Element e, String attributeName) {
        
        String attr = e.getAttribute(attributeName);
        
        if (attr == null || EMPTY_STRING.equals(attr)) {
            return null;
        }
        return attr;
    }

    /**
     * Returns the value of the named attribute from the given element. This method does not return
     * an empty string, if the attribute does not exist, but null. If characters were escaped, this
     * operation will unescape them before returning.
     * 
     * @param e the element containing the requested attribute
     * @param attributeName the name of the requested attribute
     * @return the value of the attribute containing no escaped characters or null, if it does not
     *         exist
     */
    public static String getUnescapedAttribute(Element e, String attributeName) {
        String attr = e.getAttribute(attributeName);
        
        if (attr == null || EMPTY_STRING.equals(attr)) {
            return null;
        }
        return unescapeCharacters(attr);
    }
    
    /**
     * Gets the content of a node, e.g.: <node>node content</node>.
     * 
     * @param e the element (node)
     * @return node content
     */
    public static String getNodeContent(Element e) {
        String content = e.getTextContent();
        
        if (content != null && content.equals(EMPTY_STRING)) {
            return null;
        }
        return content;
    }
    
    /**
     * Creates a new {@link Document} with a root tag of the given name. The root tag
     * gets an attribute 'version' with the given value.
     * 
     * @param rootTag the name of the root tag
     * @param version the value of the version attribute
     * @return a new Document
     * @throws NBCTestConfigurationException thrown, if 
     */
    public static Document createDocument(String rootTag, String version) throws NBCTestConfigurationException {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.newDocument();
            Element root = doc.createElement(rootTag);
            root.setAttribute("version", version);
            doc.appendChild(root);
            return doc;
        } catch (DOMException e) {
            throw new NBCTestConfigurationException(e);
        } catch (ParserConfigurationException e) {
            throw new NBCTestConfigurationException(e);
        }
    }
    
    /**
     * Creates a new {@link Document} with a root tag of the given name. The root tag
     * gets an attribute 'version' with the given value.
     * 
     * @param rootTag the name of the root tag
     * @param version the value of the version attribute
     * @return a new Document
     * @throws NBCTestConfigurationException thrown, if 
     */
    public static Document createDocument() throws NBCTestConfigurationException {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.newDocument();
            return doc;
        } catch (DOMException e) {
            throw new NBCTestConfigurationException(e);
        } catch (ParserConfigurationException e) {
            throw new NBCTestConfigurationException(e);
        }
    }
    
    /**
     * Escape characters for text appearing as XML data, between tags.
     * 
     * <P>The following characters are replaced with corresponding character entities :
     * <table border='1' cellpadding='3' cellspacing='0'>
     * <tr><th> Character </th><th> Encoding </th></tr>
     * <tr><td> < </td><td> &lt; </td></tr>
     * <tr><td> > </td><td> &gt; </td></tr>
     * <tr><td> " </td><td> &quot;</td></tr>
     * <tr><td> ' </td><td> &#039;</td></tr>
     * </table>
     */
    public static String escapeCharacters(String text) {

        if (text == null) {
            return null;
        }
        final StringBuilder result = new StringBuilder();
        final StringCharacterIterator iterator = new StringCharacterIterator(text);
        char character = iterator.current();
        while (character != CharacterIterator.DONE) {
            if (character == '<') {
                result.append(LT);
            } else if (character == '>') {
                result.append(GT);
            } else if (character == '\"') {
                result.append(QUOT);
            } else if (character == '\'') {
                result.append(SINGLE_QUOT);
            } else {
                result.append(character);
            }
            character = iterator.next();
        }
        return result.toString();
    }
    
    /**
     * Unescapes a text. See escapeCharacters().
     * 
     * @param text the text
     * @return  unescaped text
     */
    public static String unescapeCharacters(String text) {
        
        if (text == null) {
            return null;
        }
        Matcher matcher;
        matcher = PATTERN_LT.matcher(text);
        text = matcher.replaceAll("<");
        
        matcher = PATTERN_GT.matcher(text);
        text = matcher.replaceAll(">");
        
        matcher = PATTERN_QUOT.matcher(text);
        text = matcher.replaceAll("\"");
        
        matcher = PATTERN_SINGLE_QUOT.matcher(text);
        text = matcher.replaceAll("\'");
        return text;
    }
    
}
