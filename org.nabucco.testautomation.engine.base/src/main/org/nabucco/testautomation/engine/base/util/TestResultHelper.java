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

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import org.nabucco.framework.base.facade.datatype.DatatypeState;
import org.nabucco.testautomation.result.facade.datatype.ActionResponse;
import org.nabucco.testautomation.result.facade.datatype.ActionTraceFactory;
import org.nabucco.testautomation.result.facade.datatype.TestConfigurationResult;
import org.nabucco.testautomation.result.facade.datatype.TestResult;
import org.nabucco.testautomation.result.facade.datatype.TestResultContainer;
import org.nabucco.testautomation.result.facade.datatype.TestScriptResult;
import org.nabucco.testautomation.result.facade.datatype.manual.ManualState;
import org.nabucco.testautomation.result.facade.datatype.manual.ManualTestResult;
import org.nabucco.testautomation.result.facade.datatype.status.TestConfigElementStatusType;
import org.nabucco.testautomation.result.facade.datatype.status.TestConfigurationStatusType;
import org.nabucco.testautomation.result.facade.datatype.status.TestScriptElementStatusType;
import org.nabucco.testautomation.result.facade.datatype.status.TestScriptStatusType;
import org.nabucco.testautomation.result.facade.datatype.trace.ActionTrace;
import org.nabucco.testautomation.result.facade.datatype.trace.FileTrace;
import org.nabucco.testautomation.result.facade.datatype.trace.MessageTrace;
import org.nabucco.testautomation.result.facade.datatype.trace.ScreenshotTrace;
import org.nabucco.testautomation.schema.facade.datatype.SchemaElement;

/**
 * TestResultFactory
 * 
 * @author Steffen Schmidt, PRODYNA AG
 */
public class TestResultHelper {

	public static TestConfigurationResult createTestConfigurationResult() {
		TestConfigurationResult result = new TestConfigurationResult();
		result.setDatatypeState(DatatypeState.INITIALIZED);
		result.setStatus(TestConfigurationStatusType.INITIALIZED);
		result.setIdentificationKey("");
		return result;
	}
	
	public static TestResult createTestResult(SchemaElement schemaElement) {
		TestResult result = new TestResult();
		result.setDatatypeState(DatatypeState.INITIALIZED);
		result.setStatus(TestConfigElementStatusType.PASSED);
		result.setJiraExport(schemaElement.getJiraExport());
		return result;
	}
	
	public static ManualTestResult createManualTestResult(SchemaElement schemaElement) {
		ManualTestResult result = new ManualTestResult();
		result.setDatatypeState(DatatypeState.INITIALIZED);
		result.setStatus(TestConfigElementStatusType.PASSED);
		result.setState(ManualState.INITIALIZED);
		result.setJiraExport(schemaElement.getJiraExport());
		return result;
	}

	public static ManualTestResult createManualTestResult(TestResult orgResult) {
		ManualTestResult result = new ManualTestResult();
		result.setDatatypeState(DatatypeState.INITIALIZED);
		result.setBrandType(orgResult.getBrandType());
		result.setStatus(TestConfigElementStatusType.PASSED);
		result.setState(ManualState.INITIALIZED);
		result.setTestConfigElementId(orgResult.getTestConfigElementId());
        result.setTestConfigElementName(orgResult.getTestConfigElementName());
        result.setTestConfigElementKey(orgResult.getTestConfigElementKey());
        result.setName(orgResult.getName());
        result.setLevel(orgResult.getLevel());
        result.setSchemaElementId(orgResult.getSchemaElementId());
        result.setJiraExport(orgResult.getJiraExport());
		return result;
	}
	
	public static TestScriptResult createTestScriptResult() {
		TestScriptResult result = new TestScriptResult();
		result.setDatatypeState(DatatypeState.INITIALIZED);
		result.setStatus(TestScriptStatusType.PASSED);
		return result;
	}

	public static ActionResponse createActionResponse() {
		ActionResponse response = new ActionResponse();
		response.setDatatypeState(DatatypeState.INITIALIZED);
		response.setElementStatus(TestScriptElementStatusType.NOT_EXECUTED);
		return response;
	}
	
	public static ActionTrace createActionTrace() {
		return ActionTraceFactory.getInstance().createActionTrace();
	}

	public static MessageTrace createMessageTrace() {
		return ActionTraceFactory.getInstance().createMessageTrace();
	}
	
	public static ScreenshotTrace createScreenshotTrace() {
		return ActionTraceFactory.getInstance().createScreenshotTrace();
	}
	
	public static FileTrace createFileTrace() {
		return ActionTraceFactory.getInstance().createFileTrace();
	}
	
	public static void addTestResult(TestResult result, TestResult parent) {
		
		if (parent == null) {
			return;
		}
		
    	TestResultContainer container = new TestResultContainer();
    	container.setDatatypeState(DatatypeState.INITIALIZED);
    	container.setResult(result);
    	container.setOrderIndex(parent.getTestResultList().size());
    	parent.getTestResultList().add(container);
    }
	
	public static TestResult removeTestResult(TestResult result, TestResult parent) {
		
		if (parent == null) {
			return null;
		}
		
    	List<TestResultContainer> testResultList = parent.getTestResultList();
    	
		for (TestResultContainer container : testResultList) {
    		
    		if (container.getResult().equals(result)) {
    			testResultList.remove(container);
    			return container.getResult();
    		}
    	}
    	return null;
    }

	public static void addTestResult(TestResult result, TestConfigurationResult parent) {
		
		if (parent == null) {
			return;
		}
		
		TestResultContainer container = new TestResultContainer();
		container.setDatatypeState(DatatypeState.INITIALIZED);
		container.setResult(result);
		container.setOrderIndex(parent.getTestResultList().size());
		parent.getTestResultList().add(container);
	}
	
    public static String getStackTrace(Exception ex) {
    	
    	if (ex == null) {
    		return null;
    	}
    	
    	StringWriter str = new StringWriter();
    	PrintWriter pw = new PrintWriter(str); 
    	ex.printStackTrace(pw);
    	return str.getBuffer().toString();
    }
	
}
