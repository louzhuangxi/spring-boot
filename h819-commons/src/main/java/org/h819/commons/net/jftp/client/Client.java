package org.h819.commons.net.jftp.client;


import org.h819.commons.net.jftp.client.auth.UserCredentials;
import org.h819.commons.net.jftp.connection.Connection;


public abstract class Client {

    protected String host;
    protected int port;

    protected UserCredentials userCredentials = UserCredentials.ANONYMOUS;

    public void setCredentials(UserCredentials userCredentials) {
        this.userCredentials = userCredentials;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(int port) {
        this.port = port;
    }

    /**
     * Opens a connection to the given host and port.
     *
     * @return An active connection matching the protocol set by the client. All activity and
     * communication should be handled using this connection.
     * @throws
     */
    public abstract Connection connect();

    /**
     * 连接动作比较耗时，增加判断方法，省去反复连接的动作。
     * @return
     */
    public abstract boolean isconnect();

    public abstract void disconnect();
}
