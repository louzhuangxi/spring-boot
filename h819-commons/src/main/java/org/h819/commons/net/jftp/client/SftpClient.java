package org.h819.commons.net.jftp.client;


import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.h819.commons.net.jftp.connection.Connection;
import org.h819.commons.net.jftp.connection.ConnectionFactory;
import org.h819.commons.net.jftp.exception.FtpException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 没有找到，在控制台输出执行的命令的方法。
 */
public class SftpClient extends Client {

    private static final String SFTP = "sftp";
    private static final String CONNECTION_ERROR_MESSAGE = "Unable to connect to host %s on port %d";
    private Logger logger = LoggerFactory.getLogger(SftpClient.class);
    private JSch jsch;
    private ConnectionFactory connectionFactory;

    private Session session;
    private Channel channel;

    public SftpClient() {
        this.jsch = new JSch();
        this.connectionFactory = new ConnectionFactory();
    }

    public boolean isconnect() {

        if (channel != null) {
            return channel.isConnected();
        }
        return false;
    }

    public Connection connect() {

        session = null;
        channel = null;

        try {

            configureSessionAndConnect();
            openChannelFromSession();

        } catch (JSchException e) {
            throw new FtpException(String.format(CONNECTION_ERROR_MESSAGE, host, port), e);
        }

        return connectionFactory.createSftpConnection(channel);
    }

    public void disconnect() {

        if (channel != null) {
            if (channel.isConnected()) {
                channel.disconnect();
                logger.info("logged out.");
            } else if (channel.isClosed()) {
                logger.info("sftp is closed already");
            }
        }

        if (session != null) {
            if (session.isConnected()) {
                session.disconnect();
                logger.info("logged out.");
            } else {
                logger.info("sshSession is disconnected already.");
            }
        }


    }

    private void configureSessionAndConnect() throws JSchException {
        logger.info("Connected to " + host + ":" + port + " via SFTP ...");
        session = jsch.getSession(userCredentials.getUsername(), host, port);
        session.setConfig("StrictHostKeyChecking", "no");
        session.setPassword(userCredentials.getPassword());
        session.connect();
    }

    private void openChannelFromSession() throws JSchException {

        channel = session.openChannel(SFTP);
        channel.connect();
    }
}
