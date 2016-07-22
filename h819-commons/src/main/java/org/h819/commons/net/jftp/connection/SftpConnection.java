package org.h819.commons.net.jftp.connection;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.SftpException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTPFileFilter;
import org.h819.commons.net.jftp.exception.FtpException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * 在 sftp 中，路径可以是一个符合 reg 的字符串，而不必是真实的路径名称
 * 这和 ssh 的命令一致
 */
public class SftpConnection implements Connection {

    private static final String COULD_NOT_FIND_FILE_MESSAGE = "Could not find file: %s";
    private static final String DIRECTORY_DOES_NOT_EXIST_MESSAGE = "Directory %s does not exist.";
    private static final String FILE_LISTING_ERROR_MESSAGE = "Unable to list files in directory %s";
    private static final String FILE_SEPARATOR = "/";

    private static final int MILLIS = 1000;

    private ChannelSftp channel;

    private Logger logger = LoggerFactory.getLogger(SftpConnection.class);

    public SftpConnection(ChannelSftp channel) {
        this.channel = channel;
    }


    @Override
    public String getWorkingDirectory() throws FtpException {

        try {

            return channel.pwd();

        } catch (SftpException e) {

            throw new FtpException("Unable to print the working directory", e);
        }
    }

    @Override
    public void changeDirectory(String directory) throws FtpException {

        try {

            channel.cd(directory);

        } catch (SftpException e) {

            throw new FtpException(String.format(DIRECTORY_DOES_NOT_EXIST_MESSAGE, directory), e);
        }
    }


    @Override
    public List<JFTPFile> listFiles() throws FtpException {

        return listFiles(getWorkingDirectory());
    }

    /**
     * 列出当前目录中所有的文件和文件夹，重新包装为自定义的  JFTPFile
     *
     * @param ftpFileFilter 过滤器
     * @return
     * @throws FtpException
     */
    @Override
    public List<JFTPFile> listFiles(FTPFileFilter ftpFileFilter) throws FtpException {
        return null;
    }

    /**
     * @param remotePath 格式同 ssh 上的命令，如 *.cvs
     * @return
     * @throws FtpException
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<JFTPFile> listFiles(String remotePath) throws FtpException {

        try {

            List<JFTPFile> files = new ArrayList<JFTPFile>();

            String originalWorkingDirectory = getWorkingDirectory();

            changeDirectory(remotePath);

            String newWorkingDirectory = getWorkingDirectory();

            Vector<LsEntry> lsEntries = channel.ls(newWorkingDirectory);

            for (LsEntry entry : lsEntries)
                files.add(toFtpFile(entry, newWorkingDirectory));

            changeDirectory(originalWorkingDirectory);

            return files;

        } catch (SftpException e) {

            throw new FtpException(String.format(FILE_LISTING_ERROR_MESSAGE, remotePath), e);
        }
    }

    /**
     * 列出指定路径的文件和文件夹，重新包装为自定义的  JFTPFile
     *
     * @param remotePath
     * @param ftpFileFilter 过滤器
     * @return
     * @throws FtpException
     */
    @Override
    public List<JFTPFile> listFiles(String remotePath, FTPFileFilter ftpFileFilter) throws FtpException {
        return null;
    }


    /**
     * 保持源文件名不变
     *
     * @param remoteFilePath     path of the file on the server
     * @param localDirectoryPath path of directory where the file will be stored
     * @throws IOException if any network or IO error occurred.
     */
    @Override
    public void downloadFile(String remoteFilePath, String localDirectoryPath, boolean logProcess) throws FtpException {

        downloadFile(remoteFilePath, localDirectoryPath, null, logProcess);

    }

    /**
     * Download a single file from the FTP server，断点续传，如果指定目录不存在，自动创建，可以重命名下载的文件名
     *
     * @param remoteFilePath     path of the file on the server
     * @param localDirectoryPath path of directory where the file will be stored
     * @param localFileName      下载到本地的文件名称(个别需求中，需要重命名远程文件)
     * @param logProcess         log 中是否显示下载进度
     * @throws IOException if any network or IO error occurred.
     */
    @Override
    public void downloadFile(String remoteFilePath, String localDirectoryPath, String localFileName, boolean logProcess) throws FtpException {

        if (!existsFile(remoteFilePath))
            return;

        //本地文件夹不存在，递归创建
        Path targetPath = Paths.get(localDirectoryPath);
        if (!Files.exists(targetPath))
            Paths.get(localDirectoryPath).toFile().mkdirs();

        try {

            if (localFileName == null)
                channel.get(remoteFilePath, localDirectoryPath);
            else
                channel.get(remoteFilePath, localDirectoryPath + File.separator + localFileName);


        } catch (SftpException e) {

            throw new FtpException("Unable to download file " + remoteFilePath, e);
        }

        logger.info("download file '" + remoteFilePath + "'  to  localDirectory '" + localDirectoryPath + FILE_SEPARATOR + StringUtils.substringAfterLast(remoteFilePath, "/") + "' succeed.");

    }


    /**
     * 下载指定文件夹下面的文件
     * <p>
     * 实现过程同 FtpConnection.java 中的同名函数
     *
     * @param remoteDirectoryPath Path of the current directory being downloaded.
     * @param localDirectoryPath  path of directory where the whole remote directory will be
     *                            downloaded and saved.
     * @throws IOException if any network or IO error occurred.
     */
    @Override
    public void downloadDirectory(String remoteDirectoryPath, String localDirectoryPath, boolean logProcess) throws FtpException {

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
     * Upload a single file to the sFTP server.未支持断点续传
     *
     * @param localFilePath       Path of the file on local computer
     * @param remoteDirectoryPath path of directory where the file will be stored
     * @return true if the file was uploaded successfully, false otherwise
     * @throws IOException if any network or IO error occurred.
     */
    @Override
    public void uploadFile(String localFilePath, String remoteDirectoryPath, boolean logProcess) throws FtpException {

        if (!Paths.get(localFilePath).toFile().exists()) {
            throw new FtpException("Unable to upload file, file does not exist :  " + localFilePath);
        }

        if (!existsDirectory(remoteDirectoryPath))
            createDirectory(remoteDirectoryPath);

        try {
            channel.put(localFilePath, remoteDirectoryPath);
        } catch (SftpException e) {
            e.printStackTrace();
            throw new FtpException("Unable to upload file :  " + localFilePath);
        }

        logger.info("upload file succeed : " + localFilePath);
    }


    /**
     * 上传本地文件夹到服务器文件夹中
     *
     * @param localDirectoryPath  Path of the local directory being uploaded.
     * @param remoteDirectoryPath Path of the current directory on the server
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

        logger.info("upload local Directory " + localDirectoryPath + " succeed.");

    }


    @Override
    public long[] directoryInfo(String remoteDirectoryPath) throws FtpException {
        return new long[0];
    }


    /**
     * 强制删除远程文件夹和里面所有文件。
     *
     * @param remoteFileOrDirectoryPath Path of the parent directory of the current directory on the
     *                                  server (used by recursive calls).
     * @throws IOException
     */
    @Override
    public void removeFileOrDirectory(String remoteFileOrDirectoryPath) throws FtpException {

        //   logger.info("Delete directory: {}", remoteFileOrDirectoryPath);

//        String s = getWorkingDirectory();
//        removeFileOrDirectory1(workRoot,remoteFileOrDirectoryPath);
//
//        changeDirectory(s);

        /**
         *
         * sftp 没有提供递归删除非空文件夹的方法，只能自己实现
         *
         * 或者使用 jsch's exec channel rm -rf
         *
         * 下面的实现方法不行，暂时没有找到正确的实现方法
         */


//        try {
//
//            if (existFile(remoteFileOrDirectoryPath)) {
//                channel.rm(remoteFileOrDirectoryPath);//如果是文件，则删除
//                return;
//            }
//
//            List<JFTPFile> ftpFiles = listFiles(remoteFileOrDirectoryPath);
//
//            if (ftpFiles != null && ftpFiles.size() > 0) {
//                for (JFTPFile jFTPFile : ftpFiles) {
//                    if (jFTPFile.getName().equals(".") || jFTPFile.getName().equals("..")) {
//                        // skip parent directory and the directory itself
//                        continue;
//                    }
//
//                    logger.info(" name path : " + jFTPFile.getName());
//                    logger.info(" fulll path : " + jFTPFile.getFullPath());
//                    logger.info(" work root path : " + getWorkingDirectory());
//
//                    changeDirectory(jFTPFile.getFullPath());
//
//                    if (jFTPFile.isDirectory()) {
//                        changeDirectory(jFTPFile.getFullPath());
//                        removeFileOrDirectory(jFTPFile.getName());
//                    } else {
//                        channel.rm((jFTPFile.getFullPath()));
//                        // changeDirectory(originalWorkingDirectoryForRemove);
//                    }
//
//                    channel.rmdir((jFTPFile.getFullPath()));
//                    // changeDirectory(originalWorkingDirectoryForRemove);
//                }
//
//                channel.rmdir(remoteFileOrDirectoryPath);
//                // changeDirectory(originalWorkingDirectoryForRemove);
//            }
//
//        } catch (SftpException e) {
//            e.printStackTrace();
//            throw new FtpException(String.format("removeFileOrDirectory failed : ", remoteFileOrDirectoryPath), e);
//        }

    }

//    private void removeFileOrDirectory1(String workRoot, String remoteFileOrDirectoryPath) throws FtpException {
//
//        logger.info("Delete directory: {}", remoteFileOrDirectoryPath);
//
//        /**
//         *
//         * sftp 没有提供递归删除非空文件夹的方法，只能自己实现
//         *
//         * 或者使用 jsch's exec channel rm -rf
//         *
//         * 下面的实现方法不行，暂时没有找到正确的实现方法
//         */
//
//
//        changeDirectory(workRoot);
//
//        try {
//
//            if (existFile(remoteFileOrDirectoryPath)) {
//                channel.rm(remoteFileOrDirectoryPath);//如果是文件，则删除
//                return;
//            }
//
//            String p = workRoot + "/" + remoteFileOrDirectoryPath;
//            changeDirectory(p);
//            Vector<LsEntry> lsEntries = channel.ls(p);
//
//            if (lsEntries != null && lsEntries.size() > 0) {
//                for (LsEntry jFTPFile : lsEntries) {
//                    if (jFTPFile.getFilename().equals(".") || jFTPFile.getFilename().equals("..")) {
//                        // skip parent directory and the directory itself
//                        continue;
//                    }
//
//                    logger.info(" name path : " + jFTPFile.getFilename());
//
//                    logger.info(" work root path : " + getWorkingDirectory());
//
//                    if (jFTPFile.getAttrs().isDir()) {
//
//                        removeFileOrDirectory1(getWorkingDirectory(),jFTPFile.getFilename());
//                    } else {
//                        channel.rm((jFTPFile.getFilename()));
//                        // changeDirectory(originalWorkingDirectoryForRemove);
//                    }
//                    logger.info("curnt 1: " + getWorkingDirectory());
//                    changeDirectory("..");
//                    logger.info("curnt 2: " + getWorkingDirectory());
//                    logger.info("file name 1: " + jFTPFile.getFilename());
//
//                    channel.rmdir(jFTPFile.getFilename());
//                    // changeDirectory(originalWorkingDirectoryForRemove);
//                }
//
//                logger.info("curnt 3: " + getWorkingDirectory());
//                changeDirectory("..");
//                logger.info("curnt 4: " + getWorkingDirectory());
//
//                logger.info("file name 2: " + remoteFileOrDirectoryPath);
//                channel.rmdir(remoteFileOrDirectoryPath);
//                // changeDirectory(originalWorkingDirectoryForRemove);
//            }
//
//        } catch (SftpException e) {
//            e.printStackTrace();
//            throw new FtpException(String.format("removeFileOrDirectory failed : ", remoteFileOrDirectoryPath), e);
//        }
//
//    }

    /**
     * Determines whether a file exists or not
     * 如果是文件夹，返回 false
     *
     * @param remoteFilePath
     * @return true if exists, false otherwise
     * @throws IOException thrown if any I/O error occurred.
     */
    @Override
    public boolean existsFile(String remoteFilePath) {


        try {
            // System.out.println(channel.realpath(remoteFilePath));
            SftpATTRS attrs = channel.stat(remoteFilePath);
            return attrs.isReg();
        } catch (SftpException e) {
            e.printStackTrace();
            return false;
        }

//        try {
//            // ls 命令
//            // 如果是文件夹或文件夹的符号链接，会进入该文件夹，进行列表，则文件个数会大于 1
//            // 如果是文件，则只会返回该文件本身，文件个数 =1
//            Vector<LsEntry> lsEntries = channel.ls(remoteFilePath);
//
//            for(LsEntry entry:lsEntries)
//               System.out.println(entry.getFilename());
//
//            return lsEntries.size() == 1;
//        } catch (SftpException e) {
//            e.printStackTrace();
//            return false;
//        }

    }


    /**
     * Determines whether a directory exists or not
     * 如果是文件，也会抛出异常，返回 false
     *
     * @param remoteDirectoryPath
     * @return true if exists, false otherwise
     * @throws IOException thrown if any I/O error occurred.
     */
    @Override
    public boolean existsDirectory(String remoteDirectoryPath) {

        try {
            // System.out.println(channel.realpath(remoteFilePath));
            SftpATTRS attrs = channel.stat(remoteDirectoryPath);
            return attrs.isDir();
        } catch (SftpException e) {
            // e.printStackTrace();
            return false;
        }


//        String originalWorkingDirectory = getWorkingDirectory();
//        try {
//            changeDirectory(remoteDirectoryPath);
//        } catch (FtpException e) {
//            //文件夹不存在，会抛出异常。
//            return false;
//        }
//        //恢复 ftpClient 当前工作目录属性
//        changeDirectory(originalWorkingDirectory);
//        return true;
    }

    /**
     * 判断文件是否需要被同步
     *
     * @param remoteFilePath 服务器端文件
     * @param localFilePath  本地文件
     * @return
     */
    @Override
    public boolean isSync(String remoteFilePath, String localFilePath) {
        return false;
    }


    /**
     * Create a directory and all missing parent-directories.
     * <p>
     * 设为 private ,不会有在服务器上仅仅创建文件夹的需求。
     *
     * @param remoteDirectoryPath
     * @throws IOException
     */
    private void createDirectory(String remoteDirectoryPath) {

        String originalWorkingDirectory = getWorkingDirectory();

        String[] folders = remoteDirectoryPath.split("/");
        for (String folder : folders) {
            if (folder.length() > 0) {
                try {
                    channel.cd(folder);
                } catch (SftpException e) {
                    try {
                        channel.mkdir(folder);
                        channel.cd(folder);
                    } catch (SftpException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        }
        logger.info("create remote Directory '" + remoteDirectoryPath + "' succeed.");
        //还原当前目录
        changeDirectory(originalWorkingDirectory);
    }

    private JFTPFile toFtpFile(LsEntry lsEntry, String filePath) throws SftpException {

        String name = lsEntry.getFilename();
        long fileSize = lsEntry.getAttrs().getSize();
        String fullPath = String.format("%s%s%s", filePath, FILE_SEPARATOR, lsEntry.getFilename());
        //   String fullPath = channel.realpath(filePath); //为何不用这个
        int mTime = lsEntry.getAttrs().getMTime();
        boolean directory = lsEntry.getAttrs().isDir();

        return new JFTPFile(name, fileSize, fullPath, (long) mTime * MILLIS, directory);
    }
}
