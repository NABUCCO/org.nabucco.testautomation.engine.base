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
package org.nabucco.testautomation.engine.base.engine;

import org.nabucco.testautomation.engine.base.client.ClientInteraction;


/**
 * ExecutionController
 *
 * @author Steffen Schmidt, PRODYNA AG
 *
 */
public interface ExecutionController {

    /**
     * Gets, if the underlying process is active or paused.
     *  
     * @return true, if the underlying process is paused, otherwise false
     */
	public boolean isPaused();
	
	/**
     * Gets, if the underlying process is active or interrupted.
     *  
     * @return true, if the underlying process is interrupted, otherwise false
     */
	public boolean isInterrupted();
	
	/**
	 * Tries to set the underlying process asleep.
	 */
	public void tryPause();
	
	/**
	 * Tries to interrupt the underlying process.
	 */
	public void tryInterruption();
	
	/**
	 * Sets the calling thread asleep for the given number of milliseconds.
	 * 
	 * @param duration the sleep time in milliseconds
	 */
	public void sleep(Long duration);
	
	/**
	 * Receives the {@link ClientInteraction} to interact with the client. This
	 * operation blocks with no timeout until the input of the client is available.
	 * 
	 * @return the received ClientInteraction or null, if waiting was interrupted
	 */
	public ClientInteraction receiveClientInteraction();
	
}
