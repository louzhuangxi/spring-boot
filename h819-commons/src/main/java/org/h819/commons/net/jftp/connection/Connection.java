package org.h819.commons.net.jftp.connection;

import org.apache.commons.net.ftp.FTPFileFilter;
import org.h819.commons.net.jftp.exception.FtpException;

import java.io.IOException;
import java.util.List;


/**
 * 用 String 类型代表本地或服务器端文件路径，不要用 File
 * <p/>
 * commons ftp 上的文件，用 org.apache.commons.net.ftp.FTPFile ，其属性不能和 File 对应
 * <p/>
 * 所以用 String 代表本地文件和服务器端文件，可以通用。
 */

/**
 * 用接口定义常用的方法，实际调用时，用不同的实现
 */
public interface Connection {
    /**
     * 获得当前工作目录
     *
     * @return
     * @throws FtpException
     */
    String getWorkingDirectory() throws FtpException;

    /**
     * 切换当前工作目录
     *
     * @param directory
     * @throws FtpException
     */
    void changeDirectory(String directory) throws FtpException;

    /**
     * 列出当前目录中所有的文件和文件夹，重新包装为自定义的  JFTPFile
     *
     * @return
     * @throws FtpException
     */
    List<JFTPFile> listFiles() throws FtpException;

    /**
     * 列出当前目录中所有的文件和文件夹，重新包装为自定义的  JFTPFile
     *
     * @param ftpFileFilter 过滤器
     * @return
     * @throws FtpException
     */
    List<JFTPFile> listFiles(FTPFileFilter ftpFileFilter) throws FtpException;

    /**
     * 列出指定路径的文件和文件夹，重新包装为自定义的  JFTPFile
     * 无过滤器
     *
     * @param remotePath
     * @return
     * @throws FtpException
     */
    List<JFTPFile> listFiles(String remotePath) throws FtpException;


    /**
     * 列出指定路径的文件和文件夹，重新包装为自定义的  JFTPFile
     *
     * @param remotePath
     * @param ftpFileFilter 过滤器
     * @return
     * @throws FtpException
     */
    List<JFTPFile> listFiles(String remotePath, FTPFileFilter ftpFileFilter) throws FtpException;


    /**
     * Upload a single file to the FTP server.
     *
     * @param localFilePath       Path of the file on local computer
     * @param remoteDirectoryPath path of directory where the file will be stored
     * @param logProcess          log 中是否显示下载进度
     * @return
     */
    void uploadFile(String localFilePath, String remoteDirectoryPath, boolean logProcess) throws FtpException;

    /**
     * 上传本地文件夹到服务器文件夹中
     *
     * @param localDirectoryPath  Path of the local directory being uploaded.
     * @param remoteDirectoryPath Path of the current directory on the server
     * @param logProcess          log 中是否显示下载进度
     * @throws FtpException
     */
    void uploadDirectory(String localDirectoryPath, String remoteDirectoryPath, boolean logProcess) throws FtpException;

    /**
     * Download a single file from the FTP server，断点续传，如果指定目录不存在，自动创建
     *
     * @param remoteFilePath     path of the file on the server
     * @param localDirectoryPath path of directory where the file will be stored
     * @param logProcess         log 中是否显示下载进度
     * @throws IOException if any network or IO error occurred.
     */
    void downloadFile(String remoteFilePath, String localDirectoryPath, boolean logProcess) throws FtpException;

    /**
     * 下载指定文件夹下面的文件
     *
     * @param remoteDirectoryPath path of remote directory will be downloaded.
     * @param localDirectoryPath  path of local directory will be saved.
     * @param logProcess          log 中是否显示下载进度
     * @throws IOException if any network or IO error occurred.
     */
    void downloadDirectory(String remoteDirectoryPath, String localDirectoryPath, boolean logProcess);


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
    long[] directoryInfo(String remoteDirectoryPath) throws FtpException;


    /**
     * directory：Removes a directory by delete all its sub files and , sub directories recursively. And finally remove the directory.
     * file : remove the file only
     *
     * @param remoteFileOrDirectoryPath Path of the parent directory of the current directory on the
     *                                  server (used by recursive calls).
     * @throws IOException
     */
    void removeFileOrDirectory(String remoteFileOrDirectoryPath) throws FtpException;


    /**
     * Determines whether a file exists or not
     * 如果是文件夹，返回 false
     *
     * @param remoteFilePath
     * @return true if exists, false otherwise
     * @throws IOException thrown if any I/O error occurred.
     */
    boolean existsFile(String remoteFilePath) throws FtpException;


    /**
     * Determines whether a directory exists or not
     * 如果是文件，返回 false
     *
     * @param remoteDirPath
     * @return true if exists, false otherwise
     * @throws IOException thrown if any I/O error occurred.
     */
    boolean existsDirectory(String remoteDirPath) throws FtpException;


    /**
     * http://stackoverflow.com/questions/29425471/how-to-compare-ftpclient-files-last-modification-with-a-local-file
     * There is no standard way to know ftp server timezone, but what you can do is to upload a file and then calculate the time difference between the file time reported by FTP and locally.
     * This must be a method running as a first step of your program to initialize the timezone logic in every client application of yours.
     */

    /**
     * 判断文件是否需要被同步
     *
     * @param remoteFilePath 服务器端文件
     * @param localFilePath  本地文件
     * @return
     */
    boolean isSync(String remoteFilePath, String localFilePath);

}