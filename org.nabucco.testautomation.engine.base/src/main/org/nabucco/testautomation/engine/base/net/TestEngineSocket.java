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
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

import org.nabucco.framework.base.facade.datatype.logger.NabuccoLogger;
import org.nabucco.framework.base.facade.datatype.logger.NabuccoLoggingFactory;

/**
 * TestEngineSocket
 * 
 * @author Steffen Schmidt, PRODYNA AG
 */
public class TestEngineSocket extends Socket {

    public static final int DEFAULT_BUFFER_SIZE = 65536;

    private static final NabuccoLogger logger = NabuccoLoggingFactory.getInstance().getLogger(TestEngineSocket.class);

    private TransferMode transferMode = TransferMode.STANDARD;

    /**
     * @throws SocketException
     */
    public TestEngineSocket() throws SocketException {
        super();
        this.setReceiveBufferSize(DEFAULT_BUFFER_SIZE);
        this.setSendBufferSize(DEFAULT_BUFFER_SIZE);

        if (logger.isDebugEnabled()) {
            logger.debug("TestEngineSocket created with BufferSize (send and receive): " + DEFAULT_BUFFER_SIZE);
        }
    }

    /**
     * 
     * @param bufferSize
     * @throws SocketException
     */
    public TestEngineSocket(int bufferSize) throws SocketException {
        super();
        this.setReceiveBufferSize(bufferSize);
        this.setSendBufferSize(bufferSize);

        if (logger.isDebugEnabled()) {
            logger.debug("TestEngineSocket created with BufferSize (send and receive): " + bufferSize);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void connect(SocketAddress endpoint) throws IOException {

        if (logger.isDebugEnabled()) {
            logger.debug("Connecting to: " + endpoint.toString());
        }
        super.connect(endpoint);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void connect(SocketAddress endpoint, int timeout) throws IOException {

        if (logger.isDebugEnabled()) {
            logger.debug("Connecting to: " + endpoint.toString() + ", Timeout: " + timeout + " ms");
        }
        super.connect(endpoint, timeout);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void bind(SocketAddress bindpoint) throws IOException {

        if (logger.isDebugEnabled()) {
            logger.debug("Binding to: " + bindpoint.toString());
        }
        super.bind(bindpoint);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void close() throws IOException {
        super.close();

        if (logger.isDebugEnabled()) {
            logger.debug("TestEngineSocket closed");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void shutdownInput() throws IOException {
        super.shutdownInput();

        if (logger.isDebugEnabled()) {
            logger.debug("Input shutdown");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void shutdownOutput() throws IOException {
        super.shutdownOutput();

        if (logger.isDebugEnabled()) {
            logger.debug("Output shutdown");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InputStream getInputStream() throws IOException {

        if (this.transferMode == TransferMode.COMPRESSED) {

            if (logger.isDebugEnabled()) {
                logger.debug("Returning InflaterInputStream");
            }
            return new InflaterInputStream(super.getInputStream());
        }
        return super.getInputStream();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OutputStream getOutputStream() throws IOException {

        if (this.transferMode == TransferMode.COMPRESSED) {

            if (logger.isDebugEnabled()) {
                logger.debug("Returning DeflaterOutputStream");
            }
            return new DeflaterOutputStream(super.getOutputStream());
        }
        return super.getOutputStream();
    }

    /**
     * @param transferMode
     *            the transferMode to set
     */
    public void setTransferMode(TransferMode transferMode) {
        this.transferMode = transferMode;
    }

    /**
     * @return the transferMode
     */
    public TransferMode getTransferMode() {
        return transferMode;
    }

}
