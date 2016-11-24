package org.h819.commons.file.base;

import com.google.common.hash.Hashing;
import com.google.common.io.Files;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class FileUtilsBase {
    private static Map<Long, List<String>> duplicateFilesMapBySize = new HashMap();
    private static Map<String, List<String>> duplicateFilesMapByHash = new HashMap();

    /**
     * 第一步：先比较文件大小，找到所有文件大小相同的文件，结果存放到 duplicateFilesMapBySize
     * -
     * 之后文件大小相同，才可能相同 。文件大小相同之后，再比较文件的 hash
     * -
     * 1. 已经测试过(几万个文件，大小不一)，本方案和不比较文件大小，而直接比较所有文件的 hash 值的方案(仅有步骤二)，找到的相同的文件的结果一致。
     * 2. 但两个方案效率差太多，比较所有文件的 hash 值，可能需要一个小时，而先比较文件大小，后比较文件大小相同的文件的 hash 值，仅需要几秒钟（视文件数量而定）。
     * 3. 大文件进行 hash 计算很慢，单个2G 以上的文件可能就需要十几秒钟。
     * -
     *
     * @param directory
     * @param fileFilter
     * @return key: 文件的 hash 值,  value: hash 值相同文件的路径集合，也就是相同文件的集合
     */
    private static void findDuplicateFilesBySize(File directory, FileFilter fileFilter) {

        for (File dirChild : directory.listFiles(fileFilter)) {
            // Iterate all file sub directories recursively
            if (dirChild.isDirectory()) {
                findDuplicateFilesBySize(dirChild, fileFilter);
            } else {
                System.out.println(String.format("calculate file size : %d -> %s", dirChild.length(), dirChild.getAbsolutePath()));
                long uniqueFileLength = dirChild.length();
                List<String> identicalList = duplicateFilesMapBySize.get(uniqueFileLength);
                if (identicalList == null) {
                    identicalList = new LinkedList();
                }
                // Add path to list
                identicalList.add(dirChild.getAbsolutePath());
                // push updated list to Hash table
                duplicateFilesMapBySize.put(uniqueFileLength, identicalList);

            }
        }
    }

    /**
     * 第二步：文件大小相同的文件，进行 hash 比较 ，结果存放到 duplicateFilesMapByHash
     * -
     * 仅比较文件大小相同的文件，避免了所有文件都进行 hash 计算，节省了时间
     */
    private static void findDuplicateFilesByHash() {

        for (Map.Entry<Long, List<String>> entry : duplicateFilesMapBySize.entrySet()) {
            List<String> fileList = entry.getValue();

            if (fileList.size() == 1) //无重复
                continue;

            for (String fileStr : fileList) {

                try {
                    /**
                     * 文件 Hash
                     * 1. com.google.common.io.Files.hash 最快 (比 commons DigestUtils.md5Hex 快 50%)
                     * 2. md5 比 SHA-256 快 40% 以上，md5 用于文件 hash ，足够
                     */
                    String uniqueFileHash = Files.hash(new File(fileStr), Hashing.md5()).toString();
                    System.out.println(String.format("calculate file hash : %s -> %s", uniqueFileHash, fileStr));
                    List<String> identicalList = duplicateFilesMapByHash.get(uniqueFileHash);
                    if (identicalList == null) {
                        identicalList = new LinkedList();
                    }
                    // Add path to list
                    identicalList.add(fileStr);
                    // push updated list to Hash table
                    duplicateFilesMapByHash.put(uniqueFileHash, identicalList);
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
     * @param directory
     * @param fileFilter
     * @return key: 文件的 hash 值,  value: hash 值相同文件的路径集合，也就是相同文件的集合
     */

    public static Map<String, List<String>> findDuplicateFiles(File directory, FileFilter fileFilter) {
        //第一步，比较文件大小
        findDuplicateFilesBySize(directory, fileFilter); // duplicateFilesMapBySize 赋值
        System.out.println(StringUtils.center("begin to hash file which has same size", 80, "="));
        //第二步，比较文件 hash
        findDuplicateFilesByHash();  // duplicateFilesMapByHash 赋值
        //返回结果
        Map<String, List<String>> duplicate = new HashMap();
        for (Map.Entry<String, List<String>> entry : duplicateFilesMapByHash.entrySet()) {
            if (entry.getValue().size() > 1) // 找到重复 hash 的文件
                duplicate.put(entry.getKey(), entry.getValue());
        }
        return duplicate;
    }

    /**
     * 比较所有文件
     *
     * @param directory
     * @return
     */
    public static Map<String, List<String>> findDuplicateFiles(File directory) {
        return findDuplicateFiles(directory, FileFilterUtils.trueFileFilter());
    }

}