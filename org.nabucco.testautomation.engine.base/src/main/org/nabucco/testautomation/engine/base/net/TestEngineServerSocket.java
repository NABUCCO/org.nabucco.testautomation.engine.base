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
package org.nabucco.testautomation.engine.base.net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

/**
 * TestEngineServerSocket
 * 
 * @author Steffen Schmidt, PRODYNA AG
 */
public class TestEngineServerSocket extends ServerSocket {

	/**
	 * @throws IOException
	 */
	public TestEngineServerSocket() throws IOException {
		super();
		this.setReceiveBufferSize(TestEngineSocket.DEFAULT_BUFFER_SIZE);
	}

	/**
	 * Returns an instance of {@link TestEngineSocket}.
	 * {@inheritDoc}
	 */
	@Override
	public Socket accept() throws IOException {
		if (isClosed())
		    throw new SocketException("Socket is closed");
		if (!isBound())
		    throw new SocketException("Socket is not bound yet");
		TestEngineSocket s = new TestEngineSocket();
		implAccept(s);
		return s;
	}

}
