package org.h819.commons.crypto;

import org.apache.commons.codec.digest.HmacUtils;

import java.util.Map;
import java.util.TreeMap;

/**
 * Description : TODO(基于散列的消息认证码，使用一个密钥和一个消息作为输入，生成它们的消息摘要)
 * User: h819
 * Date: 2016/5/25
 * Time: 11:08
 * To change this template use File | Settings | File Templates.
 */
//使用例子见 standard open api
public class MyHmacUtils {
    /**
     * 使用指定的密码对内容生成消息摘要（散列值）
     *
     * @param key
     * @param content
     * @return
     */

    public static String hmacSha256Hex(String key, String content) {
        return HmacUtils.hmacSha256Hex(key, content);
    }


    /**
     * 使用指定的密码对整个 Map 的内容生成消息摘要（散列值）
     * 验证时需要注意，应该是按照 key 排序的 TreeMap
     *
     * @param key
     * @param map
     * @return
     */
    public static String hmacSha256Hex(String key, Map<String, String> map) {

        Map treeMap = new TreeMap<>(map); // 按照 key 重新排序
        return hmacSha256Hex(key, treeMap.toString());
    }

}
