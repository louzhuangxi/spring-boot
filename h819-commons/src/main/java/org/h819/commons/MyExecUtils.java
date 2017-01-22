package org.h819.commons;

import org.apache.commons.exec.*;
import org.apache.commons.io.IOUtils;
import org.h819.commons.exe.ExecParameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Description : TODO()
 * User: h819
 * Date: 2015/7/27
 * Time: 16:18
 * To change this template use File | Settings | File Templates.
 */
public class MyExecUtils {

    //等待时间
    //大文件，pdf2swf 命令要读入，会有一段时间，所以等待时间不能太短。另外如果文件太大，jvm 会不会假死？
    private static int wait = 60;

    private static Logger logger = LoggerFactory.getLogger(MyExecUtils.class);

    /**
     * @param exeFile   命令一般以 exe 等文件的形式存在，本参数为该命令文件的绝对值路径
     * @param arguments 参数。
     *                  命令执行的参数不需要严格的顺序，即参数先后出现在哪个位置都可以。
     * @param exitValue 执行成功的返回值，这只之后，如果返回该值，程序不抛出异常 ，一般为 1 或 0
     */
    public static void exec(Path exeFile, List<ExecParameter> arguments, int exitValue) {

        if (!Files.exists(exeFile) || !exeFile.isAbsolute()) {
            System.out.println(exeFile + " 不存在");
            return;
        }

        if (!exeFile.toFile().canRead()) {
            System.out.println(exeFile + "  无读取'该可执行文件'权限");
            return;
        }

        if (!exeFile.toFile().canExecute()) {
            System.out.println(exeFile + "  无执行'该可执行文件'权限");
            return;
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayOutputStream errorStream = new ByteArrayOutputStream();
        //定义一个结果输出流
        PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream, errorStream);

        CommandLine cmdLine = getCommandLine(exeFile.toAbsolutePath().toString(), arguments);

        /**
         * 防假死
         * 每个准备执行的命令，执行成功后返回值不尽相同，有点是 0，有的是 1。程序根据这个成功信息来决定是否继续执行，如果成功则继续，不成功则抛出异常后停止。
         * 有的命令，在执行过程中需要等待某种资源，或者发生某种错误，而阻塞在那里不动。 通过设置命令执行超时时间(毫秒)。对于需要连续执行该命令的程序，阻塞造成假死。超时之后，自动杀死该进程
         */

        DefaultExecutor executor = new DefaultExecutor();
        DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();
        ExecuteWatchdog watchdog = new ExecuteWatchdog(wait * 1000);
        executor.setWatchdog(watchdog); //如果任务挂起，则等待5秒钟后杀死命令进程
        executor.setStreamHandler(streamHandler);//指定命令执行过程中，产生的信息的输出渠道
        executor.setExitValue(exitValue); //如果 执行结果返回该值，则程序不抛出异常

        try {
            executor.execute(cmdLine, resultHandler);
            // waitFor
            // Causes the current thread to wait, if necessary, until the process has terminated.
            // This method returns immediately if the process has already terminated.
            // If the process has not yet terminated, the calling thread will be blocked until the process exits.
            // 不设置这句，下面的 outputStream 无法输出，可能是没有等到进程结束，下面的输出已经开始了。
            resultHandler.waitFor();
            //  System.out.println(StringUtils.center(" pdf2swf.exe begin to execute ", 80, "="));
            if (resultHandler.hasResult()) { //如果命令进程执行有返回值，则通过该返回值进行判断提示。有的没有返回值，则无法判断
                if (executor.isFailure(resultHandler.getExitValue()) && watchdog.killedProcess()) {//提示 process was killed
                    System.out.println("命令等待时间超过 watchdog 规定的时间，process was killed");
                }
                ExecuteException ex = resultHandler.getException();

                if (ex != null) {  // 发生异常
                    ex.printStackTrace();
                }
            }

            //输出结果到控制台，必须在命令执行完成之后
            System.out.println(outputStream.toString("gbk"));
            System.err.println(errorStream.toString("gbk"));

        } catch (ExecuteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {

            IOUtils.closeQuietly(outputStream);
            IOUtils.closeQuietly(errorStream);

        }
    }

    /**
     * 创建  CommandLine
     *
     * @param cmdPath
     * @param arguments
     * @return
     */
    private static CommandLine getCommandLine(String cmdPath, List<ExecParameter> arguments) {
        String execStr = "'" + cmdPath + "'" + join(arguments);
        logger.info("exec command= {}", execStr);
        return CommandLine.parse(execStr);
    }

    /**
     * 构造 "key 空格 value"  形式的参数, 如 -o a.txt 中间用空格分开
     * ExecParameter 参数，如果 key 或 value 本身有空格，会被分解成多个参数，所以对有空格的参数，加单引号包围起来。
     *
     * @param arguments
     * @return
     */

    private static String join(List<ExecParameter> arguments) {
        if (arguments == null || arguments.size() == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder(arguments.size());

        String key;
        String value;

        for (ExecParameter arg : arguments) {
            key = arg.getKey().trim();
            value = arg.getValue().trim();

            if (value.equals(MyConstants.ExecEmptyValue)) {//仅有key，直接添加
                if (key.contains(" "))// key ，即 命令包含空格
                    key = "'" + key + "'";
                sb.append(" ").append(key);

            } else { // key value 对应情况如 -o c://program files//bin/cmd.exe

                if (key.contains(" "))// key ，即 命令包含空格
                    key = "'" + key + "'";

                if (value.contains(" "))// value ，即 命令包含空格
                    value = "'" + value + "'";
                sb.append(" ").append(key).append(" ").append(value);
            }
        }
        return sb.toString();
    }


    /**
     * java.lang.Runtime 执行 cmd 命令
     * 可执行文件路径不能有空格，需要用引号包含起来(windows exe 如此, linux 未验证)
     *
     * @param cmdPath
     * @return
     */
    public static String formatRuntimeCmdPath(String cmdPath) {
        return cmdPath.replaceAll(" ", "\" \"");
    }

}
