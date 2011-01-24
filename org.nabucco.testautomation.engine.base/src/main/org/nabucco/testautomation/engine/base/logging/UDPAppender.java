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
package org.nabucco.testautomation.engine.base.logging;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.zip.GZIPOutputStream;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.spi.LoggingEvent;

/**
 * UDPAppender
 * 
 * @author Frank Ratschinski, PRODYNA AG
 * 
 */
public class UDPAppender extends AppenderSkeleton {

    private int serverPort;

    private String serverHost;

    private DatagramSocket udpSocket;

    public String getServerHost() {
        return serverHost;
    }

    public void setServerHost(String serverHost) {
        this.serverHost = serverHost;
    }

    public String getServerPort() {
        return "" + serverPort;
    }

    public void setServerPort(String serverPort) {
        try {
            this.serverPort = Integer.parseInt(serverPort);
        } catch (Exception e) {
            LogLog.error("Cannot parse serverPort", e);
        }
    }

    @Override
    public synchronized void activateOptions() {
        try {

            udpSocket = new DatagramSocket();

        } catch (SocketException e) {
            LogLog.error("SocketException", e);
        }
    }

    @Override
    protected void append(LoggingEvent loggingEvent) {
        if (this.udpSocket != null) {
            try {
                ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                GZIPOutputStream gout = new GZIPOutputStream(buffer);
                ObjectOutputStream oos = new ObjectOutputStream(gout);

                oos.writeObject(loggingEvent);
                oos.flush();
                oos.close();
                byte[] payload = buffer.toByteArray();

                InetAddress adr = InetAddress.getByName(serverHost);

                //LogLog.error("Sending UPD Message: length='" + payload.length + "'");
                DatagramPacket dp = new DatagramPacket(payload, payload.length, adr, serverPort);

                udpSocket.send(dp);

            } catch (IOException e) {
                System.err.println(e);
            }
        }
    }

    @Override
    public synchronized void close() {

        try {

            if (this.udpSocket != null) {
                this.udpSocket.disconnect();
                this.udpSocket.close();
            }

        } catch (Exception e) {
            LogLog.error("SocketException", e);
        }

    }

    @Override
    public boolean requiresLayout() {
        return true;
    }

}
