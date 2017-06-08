package org.h819.commons.file.pdf;

/**
 * Description : TODO()
 * User: h819
 * Date: 2017/6/8
 * Time: 14:01
 * To change this template use File | Settings | File Templates.
 */
public class PdfBase {
    /**
     * 这是一段 js 代码，用来判断是否在有效期内，仅适用于 adobe 产品。
     * 1. 在警告期，弹出警告框进行提示，能进行查看
     * 2. 在超过有效期之后，警告后直接关闭。
     */
    protected static String expireDate =
            "function toDate(dateStr) {\n" +
                    "var parts = dateStr.split(\"-\");\n" +
                    "return new Date(parts[0], parts[1] - 1, parts[2]);\n" +
                    "}\n" +
                    "\n" +

                    "function CheckExpire() {\n" +
                    "var nowDate = new Date();\n" +
                    "var startDateStr = 'startDateStr_replace' // \"2011-01-01\";\n" +
                    "var startDate = toDate(startDateStr);\n" +
                    "var alertDays = alertDays_replace // 355;\n" +
                    "var expiredDays = expiredDays_replace // 365;\n" +
                    "var DiffDays =(nowDate - startDate)/(1000 * 60 * 60 * 24);\n" +
                    "if ( alertDays <=DiffDays && DiffDays<expiredDays) {\n" +
                    "app.alert(\"The expiration date is drawing near!\");\n" +
                    "}\n" +
                    "if (DiffDays>=expiredDays) {\n" +
                    "app.alert(\"The expiration date has already passed !\");\n" +
                    "app.execMenuItem('Close');\n" +
                    "}\n" +
                    "};\n" +
                    "CheckExpire();";
}
