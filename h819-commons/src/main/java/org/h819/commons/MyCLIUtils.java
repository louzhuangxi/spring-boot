package org.h819.commons;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

/**
 * Description : TODO(commons-cli 1.2 演示例子, 2.0 版本会变化)
 * User: h819
 * Date: 14-4-24
 * Time: 下午2:00
 * To change this template use File | Settings | File Templates.
 */
public class MyCLIUtils {

    public static void main(String[] args) {
        String[] arg = {"-h", "-c", "config.xml"};
        testOptions(arg);
    }


    public static void testOptions(String[] args) {
        Options options = new Options();
//        Option(String opt, String longOpt, boolean hasArg, String description)
//        opt - short representation of the option   // 短名称
//        longOpt - the long representation of the option  //长名称
//        hasArg - specifies whether the Option takes an argument or not   //是否必须有参数
//        description - describes the function of the option               //描述
        Option opt = new Option("h", "help", false, "Print help");
        opt.setRequired(false);                                            //此参数是否为必需
        options.addOption(opt);

        opt = new Option("c", "configFile", true, "Name server config properties file");
        opt.setRequired(false);
        options.addOption(opt);

        opt = new Option("p", "printConfigItem", false, "Print all config item");
        opt.setRequired(false);
        options.addOption(opt);

        HelpFormatter hf = new HelpFormatter();//输出格式化帮助类
        hf.setWidth(110);
        CommandLine commandLine = null;
        CommandLineParser parser = new PosixParser();
        try {
            commandLine = parser.parse(options, args);
            if (commandLine.hasOption('h')) {
                // 打印使用帮助
                hf.printHelp("testApp", options, true);
            }

            // 打印opts的名称和值
            System.out.println("--------------------------------------");
            Option[] opts = commandLine.getOptions();
            if (opts != null) {
                for (Option opt1 : opts) {
                    String name = opt1.getLongOpt();
                    String value = commandLine.getOptionValue(name);
                    System.out.println(name + "=>" + value);
                }
            }
        } catch (ParseException e) {
            hf.printHelp("testApp", options, true);
        }
    }
}
