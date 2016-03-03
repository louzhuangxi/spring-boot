package org.h819.commons.net.jftp.client;

public class ClientFactory {

    public Client createClient(Protocol clientType) {

        if (clientType == Protocol.FTP)
            return new FtpClient();

        if (clientType == Protocol.FTPS)
            return new FtpsClient();

        return new SftpClient();
    }

    public enum Protocol {
        FTP, FTPS, SFTP
    }
}
