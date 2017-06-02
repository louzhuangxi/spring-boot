package org.h819.commons.file.pdf.itext;

/**
 * Description : TODO()
 * User: h819
 * Date: 2017/5/31
 * Time: 16:20
 * To change this template use File | Settings | File Templates.
 */
public class PdfBase {

    /**
     * itext pdf 执行 js 语句
     */
    //日期限制
    protected static String JsStrExpireDate ="" +
            "function CheckExpire() {\n" +
            "\tvar nowDate = new Date();\n" +
            "\tvar startDateStr = \"2011-01-01\";\n" +
            "\tvar startDate = new Date(startDateStr);\n" +
            "\tvar expiredDays = 365;\n" +
            "\tvar alertDays = 355;\n" +
            "\tvar nButton;\n" +
            "\n" +
            "\t//现在时间减去开始时间，得到已经进行的时间\n" +
            "\tvar DiffDays = ((nowDate - startDate) / (1000 * 60 * 60 * 24));\n" +
            "\t\n" +
            "\t//((nowDay >= alertDays) && (nowDay <= expiredDays)) {\n" +
            "\t\n" +
            "\t//到了警告期，还没有过期\n" +
            "\tif ( alertDays <=DiffDays && DiffDays<expiredDays) {\n" +
            "\t\tnButton = app.alert(\"文件即将超出使用日期！\\n\\n The expiration date is drawing near!\");\n" +
            "\t\t// 修改 nButtton 的默认返回值，不关闭文件\n" +
            "\t\tnButton = 0;\n" +
            "\t}\n" +
            "\t//过期\n" +
            "\tif (DiffDays>=expiredDays) {\n" +
            "\t\tnButton = app.alert(\"文件超出使用日期！\\n\\n The expiration date has already passed !\");\n" +
            "\n" +
            "\t}\n" +
            "\t//nType =0 时, nButtton 的默认返回值是 1\n" +
            "\tif (nButton == 1)//关闭文件\n" +
            "\t\tthis.closeDoc();\n" +
            "}\n" +
            "\n" +
            "CheckExpire();";

    //启动文件时，显示打印界面
    protected static String print="\"this.print(true);\\r\"";
}
