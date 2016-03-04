package org.h819.commons.net.jftp.connection;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPFileFilter;
import org.apache.commons.net.io.CopyStreamListener;
import org.h819.commons.MyStringUtils;
import org.h819.commons.net.jftp.FileFilter.FileNameEqualFilter;
import org.h819.commons.net.jftp.exception.FtpException;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

public class FtpConnection implements Connection {

    public static final int FTP_FILE_NOT_FOUND = 550;
    public static final int FTP_PATH_CREATED = 257;
    /**
     * Default SO_SNDBUF and SO_RCVBUF size
     */
    final public static int DEFAULT_TCP_BUFFER_SIZE = 128 * 1024;
    private static final String COULD_NOT_FIND_FILE_MESSAGE = "Could not find file: %s";
    private static final String FILE_DOWNLOAD_FAILURE_MESSAGE = "Unable to download file %s";
    private static final String FILE_STREAM_OPEN_FAIL_MESSAGE = "Unable to write to local directory %s";
    private static final String FILE_LISTING_ERROR_MESSAGE = "Unable to list files in directory %s";
    private static final String NO_SUCH_DIRECTORY_MESSAGE = "The directory %s doesn't exist on the remote server.";
    private static final String UNABLE_TO_CD_MESSAGE = "Remote server was unable to change directory.";
    private static final String FILE_SEPARATOR = "/";
    private Logger logger = LoggerFactory.getLogger(FtpConnection.class);

    private FTPClient client;

    //  private String currentFileName = "";

    private int replyCode;
    //ftp MDTM 命令返回的最后修改时间格式
    private String ftpModificationTimePattern = "yyyyMMddHHmmss";
    //本地默认时区
    private DateTimeZone deafaultZone = DateTimeZone.forTimeZone(TimeZone.getDefault());

    public FtpConnection(FTPClient client) {
        this.client = client;
    }


    /**
     * Returns the pathname of the current working directory.
     *
     * @return
     * @throws FtpException
     */

    @Override
    public String getWorkingDirectory() throws FtpException {

        try {
            return client.printWorkingDirectory();

        } catch (IOException e) {

            throw new FtpException("Unable to print the working directory", e);
        }
    }

    @Override
    public void changeDirectory(String directory) throws FtpException {

        try {

            boolean success = client.changeWorkingDirectory(directory);

            if (!success)
                throw new FtpException(String.format(NO_SUCH_DIRECTORY_MESSAGE, directory));

        } catch (IOException e) {

            throw new FtpException(UNABLE_TO_CD_MESSAGE, e);
        }
    }

    /**
     * 列出当前目录中所有的文件和文件夹(可以是单独的一个文件 )，重新包装为自定义的  JFTPFile
     *
     * @return
     * @throws FtpException
     */
    @Override
    public List<JFTPFile> listFiles() throws FtpException {
        return listFiles(getWorkingDirectory());
    }

    /**
     * 列出当前目录中所有的文件和文件夹(可以是单独的一个文件 )，重新包装为自定义的  JFTPFile
     *
     * @param ftpFileFilter 过滤器
     * @return
     * @throws FtpException
     */
    @Override
    public List<JFTPFile> listFiles(FTPFileFilter ftpFileFilter) throws FtpException {
        return listFiles(getWorkingDirectory(), ftpFileFilter);
    }

    /**
     * 列出指定路径中的文件和文件夹信息(可以是单独的一个文件 )，因为 org.apache.commons.net.ftp.FTPFile 中没有文件路径信息，故重新包装为自定义的 FTPFile 类
     *
     * @param remotePath
     * @return
     * @throws FtpException
     */
    @Override
    public List<JFTPFile> listFiles(String remotePath) throws FtpException {
        return listFiles(remotePath, null);
    }

    /**
     * 列出指定路径中的文件和文件夹信息(可以是单独的一个文件 )，因为 org.apache.commons.net.ftp.FTPFile 中没有文件路径信息，故重新包装为自定义的 FTPFile 类
     *
     * @param remotePath
     * @param ftpFileFilter 过滤器
     * @return
     * @throws FtpException
     */
    @Override
    public List<JFTPFile> listFiles(String remotePath, FTPFileFilter ftpFileFilter) throws FtpException {
        List<JFTPFile> files = new ArrayList<JFTPFile>();

        try {

            String originalWorkingDirectory = getWorkingDirectory();

            if (existsDirectory(remotePath))
                changeDirectory(remotePath);

            String newWorkingDirectory = getWorkingDirectory();

            FTPFile[] ftpFiles;


            if (ftpFileFilter == null)
                ftpFiles = client.listFiles(newWorkingDirectory);
            else
                ftpFiles = client.listFiles(newWorkingDirectory, ftpFileFilter);

            for (FTPFile file : ftpFiles)
                files.add(toFtpFile(file, newWorkingDirectory));

            //恢复 ftpClient 当前工作目录属性
            changeDirectory(originalWorkingDirectory);

        } catch (IOException e) {

            throw new FtpException(String.format(FILE_LISTING_ERROR_MESSAGE, remotePath), e);
        }

        return files;
    }

    /**
     * Download a single file from the FTP server，支持断点续传，如果指定目录不存在，自动创建
     * commons net ftp 下载的文件，文件的最后修改日期和创建日期，均为当前时间，不会保留源文件的时间。
     * 本方法，修改下载的文件的时间为源文件的最后修改时间，便于监控服务器端文件是否变化。
     *
     * @param remoteFilePath     path of the file on the server
     * @param localDirectoryPath path of directory where the file will be stored
     * @param logProcess         log 中是否显示下载进度，便于调试
     * @throws IOException if any network or IO error occurred.
     */
    @Override
    public void downloadFile(String remoteFilePath, String localDirectoryPath, boolean logProcess) throws FtpException {

        try {
            FTPFile[] files = client.listFiles(new String(remoteFilePath.getBytes("GBK"), "iso-8859-1"));

            //检查远程文件是否存在
            if (files.length != 1) {
                throw new FtpException("Unable to download file : " + remoteFilePath + " does not exist.");
            }


            //如果本地文件夹不存在，可以递归创建
            String localFilePath = getDownloadLocalFilePath(remoteFilePath, localDirectoryPath);


            File localFile = Paths.get(localFilePath).toFile();
            long localSize = localFile.length();
            long remoteSize = files[0].getSize();

            if (remoteSize == localSize) {
                logger.info("[下载续传]服务器文件和本地文件大小一致::{}B = {}B，退出下载...", remoteSize, localSize);
                return;
            } else if (remoteSize < localSize) {
                logger.info("[下载续传]本地文件比服务器文件大，有误差::{}B <--> {}B，退出下载...", remoteSize, localSize);
                return;
            }

            //进行断点续传，并记录状态
            logger.info("[下载续传]准备进行下载...");
            //设置被动模式
            client.enterLocalPassiveMode();
            //设置以二进制方式传输
            client.setFileType(FTP.BINARY_FILE_TYPE);
            FileOutputStream out = new FileOutputStream(localFile, true);
            client.setRestartOffset(localFile.length());
            InputStream in = client.retrieveFileStream(new String(remoteFilePath.getBytes("GBK"), "iso-8859-1"));

            if (logProcess) {
                /**
                 * 显示下载进度
                 */
            /* 自定义实现方法 */
//                byte[] bytes = new byte[DEFAULT_TCP_BUFFER_SIZE];
//                long step = remoteSize / 100;
//                long process = localSize / step;
//                long readbytes = localSize;
//                int c;
//                while ((c = in.read(bytes)) != -1) {
//                    out.write(bytes, 0, c);
//                    readbytes += c;
//                    if (readbytes / step != process) {
//                        process = readbytes / step;
//                        logger.info("下载文件完成进度:" + process + " %");
//                        //TODO 汇报上传状态
//                    }
//                }

            /* org.apache.commons.net.io.Util.copyStream 方法*/
                final long step = remoteSize / 100;
                final long[] process = {localSize / step};
                final long[] readbytes = {localSize};

                // 监听器  org.apache.commons.net.io.CopyStreamListener
                CopyStreamListener listener = new org.apache.commons.net.io.CopyStreamAdapter() {
                    @Override
                    public void bytesTransferred(long totalBytesTransferred, int bytesTransferred, long streamSize) {
                        readbytes[0] += bytesTransferred;
                        if (readbytes[0] / step != process[0]) {
                            process[0] = readbytes[0] / step;
                            logger.info("下载文件完成进度:" + process[0] + " %");
                            //TODO 汇报上传状态
                        }
                        //  System.out.println(" totalBytesTransferred :" + totalBytesTransferred + " , bytesTransferred :" + bytesTransferred + " ,streamSize :" + streamSize);
                    }
                };

                org.apache.commons.net.io.Util.copyStream(in, out, DEFAULT_TCP_BUFFER_SIZE, client.getBufferSize(), listener, true);

            } else {  //  利用 IOUtils 重新实现，效率是不是好点？否则都用 if 中的语句即可
                /**
                 * 不显示下载进度
                 */
                if (remoteSize - localSize >= 2 * FileUtils.ONE_GB) //大于 2G时
                    IOUtils.copyLarge(in, out);
                else
                    IOUtils.copy(in, out);


            }

            out.flush();
            IOUtils.closeQuietly(in);
            IOUtils.closeQuietly(out);
            client.completePendingCommand();

            // 修改下载的文件的最后修改日期为 ftp文件时间
            localFile.setLastModified(this.getModificationTime(remoteFilePath).getMillis());

        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    /**
     * 下载指定文件夹下面的文件
     *
     * @param remoteDirectoryPath Path of the current directory being downloaded.
     * @param localDirectoryPath  path of directory where the whole remote directory will be
     *                            downloaded and saved.
     * @param logProcess          log 中是否显示下载进度
     */

    @Override
    public void downloadDirectory(String remoteDirectoryPath, String localDirectoryPath, boolean logProcess) {


        List<JFTPFile> subFiles = listFiles(remoteDirectoryPath);

        if (subFiles != null && subFiles.size() > 0) {
            for (JFTPFile jFile : subFiles) {
                if (jFile.getName().equals(".") || jFile.getName().equals("..")) {
                    // skip parent directory and the directory itself
                    continue;
                }
                if (jFile.isDirectory()) {
                    // download the sub directory
                    downloadDirectory(jFile.getAbsolutePath(), localDirectoryPath + FILE_SEPARATOR + jFile.getName(), logProcess);
                } else {
                    // download the file
                    downloadFile(jFile.getAbsolutePath(), localDirectoryPath, logProcess);
                }
            }
        }

    }

    /**
     * Upload a single file to the FTP server.支持断点续传
     *
     * @param localFilePath       Path of the file on local computer
     * @param remoteDirectoryPath path of directory where the file will be stored
     * @param logProcess          log 中是否显示下载进度 ，便于调试
     * @return
     */
    @Override
    public void uploadFile(String localFilePath, String remoteDirectoryPath, boolean logProcess) throws FtpException {


        try {

            File localFile = Paths.get(localFilePath).toFile();

            //检查本地文件是否存在
            if (!localFile.exists()) {
                throw new FtpException("Unable to download file : " + localFilePath + " does not exist.");
            }

            //上传后，服务器上的文件名称，如果远端文件夹不存在，可以递归创建。
            String remoteFilePath = getUoloadloadLocalFilePath(localFilePath, remoteDirectoryPath);


            //查看上传文件是否存在
            logger.info("[上传]检查服务器端是否存在文件...");
            FTPFile[] files = client.listFiles(remoteFilePath);

            //本地文件大小
            long localSize = localFile.length();
            //服务器端文件大小
            long remoteSize = 0;

            OutputStream out = client.appendFileStream(new String(remoteFilePath.getBytes("GBK"), "iso-8859-1"));//能后解析远端，中文文件名
            //读取本地文件
            InputStream in = new FileInputStream(localFile);
            //断点续传，必需设置下面的属性
            //设置被动模式
            client.enterLocalPassiveMode();
            //设置以二进制方式传输
            client.setFileType(FTP.BINARY_FILE_TYPE);
            client.setControlEncoding("GBK");


            //显示进度的上传

            long step = localFile.length() / 100;
            long process = remoteSize / step;
            //已经上传的大小
            long readbytes = 0L;

            if (files.length == 1) { // 上传文件已经存在
                logger.info("[上传续传]服务器端已存在该文件，准备续传工作...");

                remoteSize = files[0].getSize();
                readbytes = remoteSize;

                if (remoteSize == localSize) {
                    logger.info("[上传续传]服务器文件和本地文件大小一致::{}B--{}B，退出上传...", remoteSize, localSize);
                    return;
                } else if (remoteSize > localSize) {
                    logger.info("[上传续传]服务器文件比本地文件大，有误差::{}B <--> {}B，退出上传...", remoteSize, localSize);
                    return;
                }

                //进行断点续传，并记录状态
                logger.info("[上传续传]准备续传工作完成，开始准备游标...");
                in.skip(remoteSize);
                client.setRestartOffset(remoteSize);

            } else {
                in.skip(0);
                client.setRestartOffset(0);
                logger.info(" file not exists. 全新上传");
            }

            if (logProcess) {

                byte[] bytes = new byte[DEFAULT_TCP_BUFFER_SIZE];
                int c;
                while ((c = in.read(bytes)) != -1) {
                    out.write(bytes, 0, c);
                    readbytes += c;

                    if (readbytes / step != process) {
                        process = readbytes / step;
                        logger.info("上传文件完成进度:" + process + " %");
                        //TODO 汇报上传状态
                    }
                }

                  /* 不尝试 org.apache.commons.net.io.Util.copyStream 方法了，变量声明有点麻烦，效率差不多*/

            } else {  //  利用 IOUtils 重新实现，效率是不是好点？否则都用 if 中的语句即可
                /**
                 * 不显示下载进度
                 */
                if (localSize - remoteSize >= 2 * FileUtils.ONE_GB) //大于 2G时
                    IOUtils.copyLarge(in, out);
                else
                    IOUtils.copy(in, out);
            }

            out.flush();
            IOUtils.closeQuietly(in);
            IOUtils.closeQuietly(out);
            client.completePendingCommand();

        } catch (IOException e) {
            throw new FtpException("Upload may not have completed.", e);
        }
    }


    /**
     * 上传本地文件夹到服务器文件夹中
     *
     * @param localDirectoryPath  Path of the local directory being uploaded.
     * @param remoteDirectoryPath Path of the current directory on the server
     * @param logProcess          log 中是否显示下载进度
     * @throws FtpException
     */
    @Override
    public void uploadDirectory(String localDirectoryPath, String remoteDirectoryPath, boolean logProcess) throws FtpException {

        logger.info("LISTING directory: " + localDirectoryPath);

        File[] localSubFiles = Paths.get(localDirectoryPath).toFile().listFiles();

        if (localSubFiles != null && localSubFiles.length > 0) {
            for (File localFile : localSubFiles) {

                if (localFile.isFile()) {

                    uploadFile(localFile.getAbsolutePath(), remoteDirectoryPath, logProcess);

                } else {

                    uploadDirectory(localFile.getAbsolutePath(), remoteDirectoryPath + FILE_SEPARATOR + localFile.getName(), logProcess);
                }
            }
        }

    }

    /**
     * This method calculates total number of sub directories, files and size
     * of a remote directory.
     *
     * @param remoteDirectoryPath path of remote directory
     * @return An array of long numbers in which:
     * - the 1st number is total directories.
     * - the 2nd number is total files.
     * - the 3rd number is total size.
     * @throws IOException If any I/O error occurs.
     */
    @Override
    public long[] directoryInfo(String remoteDirectoryPath) throws FtpException {
        return new long[0];
    }

    /**
     * Create a directory and all missing parent-directories.递归创建
     * <p/>
     * 设为 private ,不会有在服务器上仅仅创建文件夹的需求。
     *
     * @param remoteDirectoryPath
     * @throws IOException
     */
    private void createDirectory(String remoteDirectoryPath) throws IOException {

        logger.info("Create Directory: {}", remoteDirectoryPath);
        int createDirectoryStatus = client.mkd(remoteDirectoryPath); // makeDirectory...
        logger.debug("Create Directory Status: {}", createDirectoryStatus);


        //  remoteDirectoryPath ="/2/2/2/1"    格式，应该进行验证
        if (createDirectoryStatus == FTP_FILE_NOT_FOUND) {
            int sepIdx = remoteDirectoryPath.lastIndexOf('/');
            if (sepIdx > -1) {
                String parentPath = remoteDirectoryPath.substring(0, sepIdx);
                createDirectory(parentPath);
                logger.debug("2'nd CreateD irectory: {}", remoteDirectoryPath);
                createDirectoryStatus = client.mkd(remoteDirectoryPath); // makeDirectory...
                logger.debug("2'nd Create Directory Status: {}", createDirectoryStatus);
            }
        }
    }

    /**
     * directory：Removes a directory by delete all its sub files and , sub directories recursively. And finally remove the directory.
     * file : remove the file only
     *
     * @param remoteFileOrDirectoryPath Path of the destination directory on the server.
     * @throws IOException
     */
    @Override
    public void removeFileOrDirectory(String remoteFileOrDirectoryPath) throws FtpException {

        logger.info("Delete directory: {}", remoteFileOrDirectoryPath);


        try {

            if (existsFile(remoteFileOrDirectoryPath)) {
                client.deleteFile(remoteFileOrDirectoryPath);//如果是文件，则删除
                return;
            }

            List<JFTPFile> ftpFiles = listFiles(remoteFileOrDirectoryPath);

            if (ftpFiles != null && ftpFiles.size() > 0) {
                for (JFTPFile jFTPFile : ftpFiles) {
                    if (jFTPFile.getName().equals(".") || jFTPFile.getName().equals("..")) {
                        // skip parent directory and the directory itself
                        continue;
                    }

                    if (jFTPFile.isDirectory())
                        removeFileOrDirectory(jFTPFile.getAbsolutePath());
                    else
                        client.deleteFile(jFTPFile.getAbsolutePath());

                    client.removeDirectory(jFTPFile.getAbsolutePath());
                }

                client.removeDirectory(remoteFileOrDirectoryPath);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Determines whether a file exists or not
     * 经过测试，如果不是文件，而是文件夹，也返回 false
     * com.enterprisedt.net.ftp.FTPClient.java 中的方法，更复杂，不用。
     *
     * @param remoteFilePath
     * @return true if exists, false otherwise
     * @throws IOException thrown if any I/O error occurred.
     */
    @Override
    public boolean existsFile(String remoteFilePath) throws FtpException {

        FTPFile[] ftpFiles = new FTPFile[0];
        try {
            ftpFiles = client.listFiles(remoteFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ftpFiles.length == 1;
    }


    /**
     * Determines whether a directory exists or not
     * 如果是文件，也会抛出异常，返回 false
     * 参考 com.enterprisedt.net.ftp.FTPClient.java
     *
     * @param remoteDirectoryPath
     * @return true if exists, false otherwise
     * @throws IOException thrown if any I/O error occurred.
     */
    @Override
    public boolean existsDirectory(String remoteDirectoryPath) throws FtpException {

        String originalWorkingDirectory = getWorkingDirectory();

        try {
            changeDirectory(remoteDirectoryPath);
        } catch (FtpException e) {
            return false;
        }

        replyCode = client.getReplyCode();
        if (replyCode == 550) {
            return false;
        }
        //恢复 ftpClient 当前工作目录属性
        changeDirectory(originalWorkingDirectory);
        return true;
    }


    /**
     * http://stackoverflow.com/questions/29425471/how-to-compare-ftpclient-files-last-modification-with-a-local-file
     *
     * http://stackoverflow.com/questions/9673976/upload-progress-in-ftpclient?lq=1
     * There is no standard way to know ftp server timezone, but what you can do is to upload a file and then calculate the time difference between the file time reported by FTP and locally.
     * This must be a method running as a first step of your program to initialize the timezone logic in every client application of yours.
     */
    /**
     * 查找本地时间和 ftp server 时间的差值，用于比较本地文件和服务器端文件是否相同
     * 一般调用一次即可。除非服务器时间经常发生变化。也可以设置为定时任务，定期执行。
     * <p/>
     * 通过上传文件来比较。
     * <p/>
     * <p/>
     * ftp server 支持的指令集不同，相同的指令集处理结果又不一样，所以有一些命令在不同的 ftp server 上，处理结果不同，如果文件最后修改时间。
     * <p/>
     * 关于 commons net ftp
     * 1. 最后修改时间：
     * 通过 commons net ftp 上传后，服务器端生成的文件的最后修改时间，是当前时间，不是源文件的最后修改时间。这是ftp server 自己的实现机制造成的。
     * 同样 commons net ftp 下载后，在本地生成的文件的最后修改时间，也是当前时间。
     * （经过测试发现，flashfxp 不同，在有的 ftp 服务器上传或下载后，能保持源文件的时间属性，
     * 其原因是：
     * 上传时，flashfxp 针对不同的 ftp server ，调用 server 的修改最后时间命令，修改了上传文件的最后修改时间。
     * 下载时，flashfxp 修改了生成的本地文件的最后修改时间
     * ）
     * <p/>
     * commons net ftp 通过 client.getModificationTime (使用  MDTM 命令获取最后修改时间) 获取 server 端文件的最后修改时间
     * 通过 client.setModificationTime  (使用  MFMT 命令获取最后修改时间) 设置 server 端文件的最后修改时间
     * MDTM、MFMT 两个命令，如前所述，不同 ftp server 上的处理结果不同。
     * 经过测试发现，前者 MDTM 大多数都支持，后者 MFMT 大多数都不支持。
     * 不再耗费精力去找 不同的server 支持的指令集 （相信 commons net ftp 作者已经找过了）
     * <p/>
     * <p/>
     * 2. 文件大小
     * commons net ftp 获得的文件大小，和用 java file 类查看本地文件大小，数值一致。
     *
     */

    /**
     * 根据上述分析，利用 commons net ftp 监控ftp server 端文件变化的方案是：
     * 0. 假定 server 支持 MDTM 命令，即 commons net ftp 能读出文件最后修改时间
     * 1. 监控的文件肯定是从 server 上下载下来的，那么下载之后，修改下载后的文件的最后修改时间为server 端源文件的最后修改时间，使二者相同
     * 2. 经过某段时间，比较 server 端文件和本地文件的最后修改时间是否一致( server 端没有变化, 本地的下载文件已经在步骤 1 修改过和 server 端相同)
     * 3. 再加上，比较文件大小 (已经测试：通过 commons net ftp 命令 返回的文件大小，和本地文件一致。此方法不准确，如文件中一个字符a变成字符b，大小不变）
     */


    /**
     * 因为 getModificationTime 方法只精确到秒，所以比较 "秒"即可
     * 文件大小不变（用此方法不准确，如文件中一个字符a变成字符b，大小不变）
     * 大致可以确定文件变化
     * 所以根据文件创建时间和文件大小，来监控服务器上面的文件是否发生变化。不十分准确，但基本上够用。
     * 更加安全的方法是，在服务器上面，监控文件变化，需要有服务器的运行权限。
     *
     * @param remoteFilePath 服务器端文件
     * @param localFilePath  本地文件
     * @return
     */

    /**
     * 有时间时，参考作者的新项目 https://github.com/JAGFin1/auto-ftp
     */
    @Override
    public boolean isSync(String remoteFilePath, String localFilePath) {
        /**
         *本地文件
         */
        File loaclTempFile = Paths.get(localFilePath).toFile();

        //检查本地文件是否存在
        if (!loaclTempFile.exists()) {
            throw new FtpException("local file : " + localFilePath + " does not exist.");
        }
        //为了便于理解，添加时区标志，实际上不添加，也会调用默认时区。
        DateTime localTimeStamp = new DateTime(loaclTempFile.lastModified()).withZone(deafaultZone);
        /**
         * ftp server 文件
         */
        FTPFile[] ftpFiles = new FTPFile[0];
        try {
            ftpFiles = client.listFiles(remoteFilePath, new FileNameEqualFilter(loaclTempFile.getName()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //检查远程文件是否存在
        if (ftpFiles.length != 1) {
            throw new FtpException("remote file : " + remoteFilePath + " does not exist.");
        }

        DateTime serverTimeStamp = getModificationTime(remoteFilePath);

        return !(localTimeStamp.getSecondOfDay() == serverTimeStamp.getSecondOfDay()) && (loaclTempFile.length() == ftpFiles[0].getSize());

    }


    /**
     * 由于 ftp 协议标准问题，仅能通过 MDTM 命令返回 yyyyMMddHHmmss 格式的文件最后修改时间，精确到秒。
     * 另外，ftp 协议中 LIST 也能返回类似的时间。
     * 但因为不同的 ftp server 对这两个命令的支持不同，所以返回的时间又有不同，需要两个命令都测试后才能确定。
     * ----
     * 在 commons net ftp 中，有两个方法，可以直接获得文件的最后修改时间，但都只能精确到秒
     * String ts = client.getModificationTime(remoteFilePath); // 查看 commons net ftp 源码，得知是使用  MDTM 命令获取的最后修改时间
     * client.doCommandAsStrings(FTPCmd.MDTM.getCommand(), remoteFilePath) // 直接使用 DMTM 命令
     * 两个返回的字符串不同
     * client.getModificationTime 返回 20140220090225
     * client.doCommandAsStrings 返回 213 20140220090225
     * 需要做相应的字符串解析
     * ----
     * 另外，
     * commons ftp 中，client.list 方法，获取的 FTPFile 的时间，利用的是 GregorianCalendar ，没有秒属性。
     */

    /**
     * 获取 ftp 上的文件的最后修改时间。
     *
     * @param remoteFilePath 目标文件路径
     * @return
     */
    private DateTime getModificationTime(String remoteFilePath) {

        try {
            //  不用 Long ftpServerTimeStamp = ftpFile.getLastModified().getMillis();
            // commons net ftp 中，文件的 list 方法，获取的 FTPFile 的时间，利用的是 GregorianCalendar ，没有秒属性。
            String ts = client.getModificationTime(remoteFilePath);
           // logger.info("ftp file time ({})", ts);
            /**
             * ftp server 上的文件的最后创建时间。
             * ftp MDTM 返回的是 GMT （格林威治时间），yyyyMMddHHmmss 格式，仅能精确到秒
             */
            DateTimeFormatter formatter = DateTimeFormat.forPattern(this.ftpModificationTimePattern).withZone(DateTimeZone.forTimeZone(TimeZone.getTimeZone(("GMT"))));
            DateTime serverTimeStamp = formatter.parseDateTime(ts);
            serverTimeStamp = serverTimeStamp.withZone(this.deafaultZone); //格林威治时间在0时区，转换为默认时区时间。
            return serverTimeStamp;

        } catch (IOException e) {
            e.printStackTrace();
            throw new FtpException(String.format("getTimeDiff exception"), e);
        }
    }

    /**
     * 通过服务器端源文件路径和本地目标文件夹路径，生成本地目标文件绝对路径，下载文件时使用。
     * 如果本地目标文件夹不存在，则创建。
     *
     * @param remoteFilePath     源文件路径
     * @param localDirectoryPath 目标文件夹路径
     * @return
     */
    private String getDownloadLocalFilePath(String remoteFilePath, String localDirectoryPath) {

        Path targetPath = Paths.get(localDirectoryPath);

        if (!Files.exists(targetPath))
            Paths.get(localDirectoryPath).toFile().mkdirs();

        String safePath = targetPath.toString();
        String fileName = Paths.get(remoteFilePath).getFileName().toString();


        return safePath + FILE_SEPARATOR + fileName;
    }

    /**
     * 通过本地源文件路径和服务器端目标文件夹路径，生成服务器端目标文件绝对路径，上传时使用。
     * 如果服务器目标文件夹不存在，则创建。
     *
     * @param localFilePath       本地源文件路径
     * @param remoteDirectoryPath 服务器目标文件夹路径
     * @return
     */
    private String getUoloadloadLocalFilePath(String localFilePath, String remoteDirectoryPath) {

        if (!existsDirectory(remoteDirectoryPath)) {
            try {
                createDirectory(remoteDirectoryPath);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Path targetPath = Paths.get(remoteDirectoryPath);
        String safePath = targetPath.toString();
        String fileName = Paths.get(localFilePath).getFileName().toString();

        return safePath + FILE_SEPARATOR + fileName;
    }


    /**
     * 把指定路径中的 FTPFile 类型文件，转换为 JFtpFile
     *
     * @param ftpFile             FTPFile 类型的文件
     * @param remoteDirectoryPath 文件所在目录
     * @return
     * @throws IOException
     */
    private JFTPFile toFtpFile(FTPFile ftpFile, String remoteDirectoryPath) throws IOException {

        String name = ftpFile.getName();
        long fileSize = ftpFile.getSize();
        String fullPath = remoteDirectoryPath + FILE_SEPARATOR + ftpFile.getName();
        long mTime = ftpFile.getTimestamp().getTime().getTime();
        //  logger.info(" 1.  getTimestamp : "+new DateTime(mTime).withZone(DateTimeZone.forTimeZone(TimeZone.getDefault())));
        // logger.info(" 2.  getTimestamp : "+ftpFile.getTimestamp().getTimeInMillis());
        boolean isDirectory = ftpFile.isDirectory();

        return new JFTPFile(name, fileSize, fullPath, mTime, isDirectory);
    }


    /**
     * 查找本地文件和服务器文件的最后修改时间差
     * 未用到本方法
     *
     * @return
     */

    private long getTimeDiff() {
        Path path = null;
        //ftp MDTM 命令返回的最后修改时间格式
        //String pattern = "yyyyMMddHHmmss";

        //本地默认时区
        DateTimeZone deafaultZone = DateTimeZone.forTimeZone(TimeZone.getDefault());
//                                                                                            ?????
//                                                                                            ?????
        try {
            /**
             *本地文件
             */
            //在系统默认的临时文件夹，创建一个空文件。
            path = Files.createTempFile("temp", ".txt");
            // File loaclTempFile = path.toFile();

            File loaclTempFile = Paths.get("D:\\download\\edtftpj.zip").toFile();

            String localFilePath = loaclTempFile.getAbsolutePath();
            // FileUtils.writeStringToFile(loaclTempFile,"this is test string, for get time diff of local and remote"); //上传方法，用到了文件大小，此处写点东西，避免建立一个空文件
            //为了便于理解，添加时区标志，实际上不添加，也会调用默认时区。
            DateTime localTimeStamp = new DateTime(loaclTempFile.lastModified()).withZone(deafaultZone);


            /**
             * ftp server 文件
             */
            //上传到 ftp 根目录，因为本方法创建的临时文件过小，本类的方法 uploadFile 需要显示上传过程，在进行除法的时候出现异常，单独实现。

            //上传后，服务器上的文件名称。
            String remoteFilePath = getUoloadloadLocalFilePath(localFilePath, "temp_test_time");
            InputStream inputStream = null;
            try {
                inputStream = new FileInputStream(new File(localFilePath));
                boolean hasUploaded = client.storeFile(remoteFilePath, inputStream);
                if (!hasUploaded)
                    throw new FtpException("Upload failed.");

                logger.info("upload a file to: " + remoteFilePath);
            } catch (FileNotFoundException e) {

                throw new FtpException(String.format(COULD_NOT_FIND_FILE_MESSAGE, localFilePath), e);
            } catch (IOException e) {
                throw new FtpException("Upload may not have completed.", e);
            } finally {
                IOUtils.closeQuietly(inputStream);

            }

            client.setModificationTime(remoteFilePath, localTimeStamp.toString(ftpModificationTimePattern));


            //只返回刚上传的文件，只有一个
            List<JFTPFile> ftpFiles = this.listFiles("/temp_test_time/", new FileNameEqualFilter(loaclTempFile.getName()));
            JFTPFile ftpFile = ftpFiles.get(0);
            // 通过 调用 MDTM ，获取最好修改时间。
            //  不用 Long ftpServerTimeStamp = ftpFile.getLastModified().getMillis();
            // commons ftp 中，文件的 list 方法，获取的 FTPFile 的时间，利用的是 GregorianCalendar ，没有秒属性。
            //
            String ts = client.getModificationTime("/temp_test_time/" + ftpFile.getName());

            /**
             * ftp server 上的文件的最后创建时间。
             * ftp MDTM 返回的是 GMT （格林威治时间），yyyyMMddHHmmss 格式，仅能精确到秒
             */
            DateTimeFormatter formatter = DateTimeFormat.forPattern(ftpModificationTimePattern).withZone(DateTimeZone.forTimeZone(TimeZone.getTimeZone(("GMT"))));
            //解析 MDTM 命令返回的字符串  "213 yyyyMMddHHmmss" 格式
            DateTime serverTimeStamp = formatter.parseDateTime(MyStringUtils.substringAfter(ts, " ").trim());
            serverTimeStamp = serverTimeStamp.withZone(DateTimeZone.forTimeZone(TimeZone.getDefault())); //格林威治时间在0时区，转换为默认时区时间。
            /**
             *下面为测试
             */
            //logger.info("local time : " + localTimeStamp.toString() + " ,ftp server time : " + serverTimeStamp.toString());
            //测试，commons ftp list 方法，获取的 FTPFile 的时间，没有秒属性。
            //logger.info("ftp server time 2 : " + ftpFile.getLastModified().withZone(DateTimeZone.forTimeZone(TimeZone.getDefault())).toString());

            //logger.info(" ========== local time : " + localTimeStamp.getSecondOfDay() + " ,ftp server time : " + serverTimeStamp.getSecondOfDay());
            //返回二者的时间差，精确到秒
            return localTimeStamp.getSecondOfDay() - serverTimeStamp.getSecondOfDay();

        } catch (IOException e) {
            e.printStackTrace();
            throw new FtpException(String.format("getTimeDiff exception"), e);
        }
    }
}
