package org.h819.commons.file.base;

import com.google.common.hash.Hashing;
import com.google.common.io.Files;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.lang.StringUtils;
import org.h819.commons.MyJsonUtils;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

public class FileUtilsBase {
    //暂存文件大小相同的文件
    private static Map<Long, List<String>> outDuplicateFilesMapBySize = new HashMap();
    //暂存文件 hash 相同的文件
    private static Map<String, List<String>> outDuplicateFilesMapByHash = new HashMap();
    //操作系统文件，忽略
    private static String[] sysFiles = {"$RECYCLE.BIN"};


    /**
     *  查找重复文件：
     *  -
     *  方案一：
     *  比较所有文件的 hash 值
     *  -
     *  方案二：
     *  1. 先比较文件大小
     *  2. 在比较文件大小相同的文件的 Hash
     *  -
     *  总结
     *  1. 已经测试过(几万个文件，大小不一)，方案一和方案二，找到的相同的文件的结果一致。
     *  2. 但两个方案效率相差悬殊，计算文件的 Hash 值很耗时间，尤其是大文件，耗时更长。文件很多，大文件很大的话，方案一以可能需要数小时，而方案二仅需要数秒钟。
     *  3. 大文件进行 hash 计算很慢，单个2G 以上的文件可能就需要十几秒钟。
     */

    /**
     * 第一步：先比较文件大小，找到所有文件大小相同的文件，结果存放到 outDuplicateFilesMapBySize
     * -
     * 之后文件大小相同，才可能相同 。文件大小相同之后，再比较文件的 hash
     * -
     *
     * @param directories
     * @param fileFilter
     * @return key: 文件的 hash 值,  value: hash 值相同文件的路径集合，也就是相同文件的集合
     */
    private static void findDuplicateFilesBySize(FileFilter fileFilter, List<File> directories) {
        if (directories == null || directories.isEmpty())
            return;
        for (File dirChild : directories) {
            if (!dirChild.canRead() || Arrays.asList(sysFiles).contains(dirChild.getName()))
                continue;
            if (dirChild.isDirectory()) {
                if (dirChild.listFiles() == null || dirChild.listFiles().length == 0)
                    continue;
                findDuplicateFilesBySize(fileFilter, Arrays.asList(dirChild.listFiles(fileFilter)));
            } else {
                if (dirChild.length() == 0)
                    continue;
                System.out.println(String.format("calculate file size : %d -> %s", dirChild.length(), dirChild.getAbsolutePath()));
                long uniqueFileLength = dirChild.length();
                List<String> identicalList = outDuplicateFilesMapBySize.get(uniqueFileLength);
                if (identicalList == null) {
                    identicalList = new LinkedList();
                }
                // Add path to list
                identicalList.add(dirChild.getAbsolutePath());
                // push updated list to Hash table
                outDuplicateFilesMapBySize.put(uniqueFileLength, identicalList);

            }

        }

    }

    /**
     * 第二步：文件大小相同的文件，进行 hash 比较 ，结果存放到 outDuplicateFilesMapByHash
     * -
     * 仅比较文件大小相同的文件，避免了所有文件都进行 hash 计算，节省了时间
     */
    private static void findDuplicateFilesByHash() {

        for (Map.Entry<Long, List<String>> entry : outDuplicateFilesMapBySize.entrySet()) {
            List<String> fileList = entry.getValue();

            if (fileList.size() == 1) //无重复
                continue;

            for (String fileStr : fileList) {

                try {
                    /**
                     * 文件 Hash
                     * 1. com.google.common.io.Files.hash 最快 (比 commons DigestUtils.md5Hex 快 50%)
                     * 2. md5 比 SHA-256 快 40% 以上，md5 用于文件 hash ，足够准确
                     * md5 和 sha-1 已经被证实，在 pdf 文件中，不同的文件内容，sha-1 相同，如果不是要求非常高的系统 md5 足够
                     */
                    String uniqueFileHash = Files.hash(new File(fileStr), Hashing.md5()).toString();
                    System.out.println(String.format("calculate file hash : %s -> %s", uniqueFileHash, fileStr));
                    List<String> identicalList = outDuplicateFilesMapByHash.get(uniqueFileHash);
                    if (identicalList == null) {
                        identicalList = new LinkedList();
                    }
                    // Add path to list
                    identicalList.add(fileStr);
                    // push updated list to Hash table
                    outDuplicateFilesMapByHash.put(uniqueFileHash, identicalList);
                } catch (IOException e) {
                    throw new RuntimeException("cannot read file " + fileStr, e);
                }
            }


            // duplicate.put(entry.getKey(), entry.getValue());
        }
    }


    /**
     * 找到指定文件夹中重复的文件
     *
     * @param directories
     * @param fileFilter
     * @return key: 文件的 hash 值,  value: hash 值相同文件的路径集合，也就是相同文件的集合
     */

    public static Map<String, List<String>> findDuplicateFiles(FileFilter fileFilter, List<File> directories) {

        /**
         * 静态变量在全局固定，如果多次使用，map 会积累所有的使用数据
         * 所以每次使用时，要用一个外部方法清空
         * - Duplicate 方法仅在此方法中进行即可
         *
         */
        outDuplicateFilesMapBySize.clear();
        outDuplicateFilesMapByHash.clear();

        //第一步，比较文件大小
        findDuplicateFilesBySize(fileFilter, directories); // outDuplicateFilesMapBySize 赋值
        System.out.println(StringUtils.center("begin to hash file which has same size", 80, "="));
        //第二步，比较文件 hash
        findDuplicateFilesByHash();  // outDuplicateFilesMapByHash 赋值
        //返回结果
        Map<String, List<String>> duplicate = new HashMap();
        for (Map.Entry<String, List<String>> entry : outDuplicateFilesMapByHash.entrySet()) {
            if (entry.getValue().size() > 1) // 找到重复 hash 的文件
                duplicate.put(entry.getKey(), entry.getValue());
        }
        return duplicate;
    }

    /**
     * 比较所有文件
     *
     * @param directories
     * @return
     */
    public static Map<String, List<String>> findDuplicateFiles(List<File> directories) {
        return findDuplicateFiles(FileFilterUtils.trueFileFilter(), directories);
    }


    /**
     * 所有文件
     *
     * @param deletePathStartWith
     * @param outDuplicateFile
     * @param directories
     */
    public static void deleteDuplicateFiles(String deletePathStartWith, File outDuplicateFile, List<File> directories) {
        deleteDuplicateFiles(deletePathStartWith, outDuplicateFile, directories, FileFilterUtils.trueFileFilter());
    }

    /**
     * 所有文件，不输出重复结果
     *
     * @param deletePathStartWith
     * @param directories
     */
    public static void deleteDuplicateFiles(String deletePathStartWith, List<File> directories) {
        deleteDuplicateFiles(deletePathStartWith, null, directories, FileFilterUtils.trueFileFilter());
    }

    /**
     * 不输出重复结果
     *
     * @param deletePathStartWith
     * @param directories
     */
    public static void deleteDuplicateFiles(String deletePathStartWith, List<File> directories, FileFilter fileFilter) {
        deleteDuplicateFiles(deletePathStartWith, null, directories, fileFilter);
    }

    /**
     * 删除找到的重复文件
     *
     * @param deletePathStartWith 删除文件路径以 deletePathStartWith 开始的重复文件，保留其他；
     *                            如果重复的文件都是以该字符串开始，保留文件路径层次最浅的
     * @param outDuplicateFile    输出找到的重复文件路径到外部文件
     * @param directories
     * @param fileFilter
     */
    public static void deleteDuplicateFiles(String deletePathStartWith, File outDuplicateFile, List<File> directories, FileFilter fileFilter) {
        Map<String, List<String>> duplicateFiles = findDuplicateFiles(fileFilter, directories);
        //MyJsonUtils.prettyPrint(duplicateFiles);
        if (outDuplicateFile != null)
            MyJsonUtils.prettyWrite(outDuplicateFile, duplicateFiles);

        System.out.println(StringUtils.center("begin to delete duplicate file start with " + deletePathStartWith, 80, "="));

        for (Map.Entry<String, List<String>> entry : duplicateFiles.entrySet()) {
            List<String> duplicates = entry.getValue();
            List<String> deletes = new ArrayList<>(duplicates.size());
            for (String path : duplicates) {
                if (path.startsWith(deletePathStartWith)) {
                    deletes.add(path);
                }
            }
            if (!deletes.isEmpty())
                if (deletes.size() == duplicates.size()) {  //文件在同一个起始文件路径中，保留层次最浅的文件
                    // 按照路径由短到长排序，删除路径层次深的文件
                    Collections.sort(deletes, new Comparator<String>() {
                        @Override
                        public int compare(String path1, String path2) {
                            return path1.length() - path2.length();
                        }
                    });
                    //删除其他
                    deleteFiles(deletes.subList(1, deletes.size()));

                } else {
                    deleteFiles(deletes);
                }
        }
    }


    private static void deleteFiles(List<String> paths) {
        for (String path : paths) {
            try {
                System.out.println(String.format("file %s -> delete", path));
                java.nio.file.Files.deleteIfExists(Paths.get(path));
            } catch (IOException e) {
                e.printStackTrace();

            }
        }
    }

}