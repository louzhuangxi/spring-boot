package org.h819.commons.exe;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.List;

/**
 * Description : TODO()
 * User: h819
 * Date: 2016-1-23
 * Time: 4:54
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractCommonExecs {
    private Logger log = LoggerFactory.getLogger(AbstractCommonExecs.class);
    private static final String DEFAULT_ENCODING = "UTF-8";
    private String encoding = DEFAULT_ENCODING;

    private String bin;
    private List<String> arguments;

    public AbstractCommonExecs(String bin, List<String> arguments) {
        this.bin = bin;
        this.arguments = arguments;
    }
      /*
    public ExecResult exec() throws IOException {
        ExecResult er = new ExecResult();
        //ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PipedOutputStream outputStream = new PipedOutputStream();
        PipedInputStream pis = new PipedInputStream(outputStream);
        ByteArrayOutputStream errorStream = new ByteArrayOutputStream();
        CodeInfoCallback codeInfoCb = new CodeInfoCallback();
        StdOutputCallback stdoutCb = new StdOutputCallback();
        ErrorOutputCallback stderrCb = new ErrorOutputCallback();
        String stdout = null;
        String stderr = null;
        try {
            Executor executor = getExecutor();
            CommandLine cmdLine = getCommandLine();
            log.info("Executing script {}", cmdLine.toString());
            if (supportWatchdog()) {
                executor.setWatchdog(getWatchdog());
            }
            PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream, errorStream);
            executor.setStreamHandler(streamHandler);
            int ret = executor.execute(cmdLine);

            readInputStream(pis, stdoutCb, codeInfoCb);
            pis.close();
            readErrorStream(errorStream, stderrCb);
            stdout = join(stdoutCb.getLines());
            stderr = stderrCb.getErrors();
            log.info("output from script {} is {}", this.bin, stdout);
            log.info("error output from script {} is {}", this.bin, stderr);
            log.info("exit code from script {} is {}", this.bin, ret);
            er.setStdout(stdout);
            er.setStderr(stderr);
            er.setCodeInfo(codeInfoCb.getCodeInfo());
            er.setExitCode(ret);
            return er;
        } catch (ExecuteException e) {
            if (pis != null) {
                readInputStream(pis, stdoutCb, codeInfoCb);
                pis.close();
            }
            if (errorStream != null) {
                readErrorStream(errorStream, stderrCb);
            }
            stdout = join(stdoutCb.getLines());
            stderr = stderrCb.getErrors();
            int ret = e.getExitValue();
            log.info("output from script {} is {}", this.bin, stdout);
            log.info("error output from script {} is {}", this.bin, stderr);
            log.info("exit code from script {} is {}", this.bin, ret);
            er.setStdout(stdout);
            er.setStderr(stderr);
            er.setCodeInfo(codeInfoCb.getCodeInfo());
            er.setExitCode(ret);
            return er;
        }

    }  */

    /**
     * 接口回调的方式解析脚本的错误输出
     *
     * @param baos
     * @param cbs
     * @throws IOException
     */
    private void readErrorStream(ByteArrayOutputStream baos, OutputCallback... cbs) throws IOException {
        String err = baos.toString(getEncoding());
        for (OutputCallback cb : cbs) {
            cb.parse(err);
        }
    }

    /**
     * 接口回调的方式解析脚本的标准输出
     *
     * @param pis
     * @param cbs
     * @throws IOException
     */
    private void readInputStream(PipedInputStream pis, OutputCallback... cbs) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(pis, getEncoding()));
        String line = null;
        while ((line = br.readLine()) != null) {
            for (OutputCallback cb : cbs) {
                cb.parse(line);
            }
        }
    }

    public Executor getExecutor() {
        Executor executor = new DefaultExecutor();
        executor.setWorkingDirectory(new File(this.bin).getParentFile());
        return executor;
    }

    public CommandLine getCommandLine() {
        String fullCommand = bin + join(arguments);

        return CommandLine.parse(fullCommand);
    }

    protected String join(List<String> arguments) {
        if (arguments == null || arguments.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (String arg : arguments) {
            sb.append(" ").append(arg);
        }
        return sb.toString();
    }

    /**
     * @return the encoding
     */
    protected String getEncoding() {
        return encoding;
    }

    /**
     * @param encoding the encoding to set
     */
    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    /**
     * @return the bin
     */
    protected String getBin() {
        return bin;
    }

    /**
     * @param bin the bin to set
     */
    public void setBin(String bin) {
        this.bin = bin;
    }

    /**
     * @return the arguments
     */
    protected List<String> getArguments() {
        return arguments;
    }

    /**
     * @param arguments the arguments to set
     */
    public void setArguments(List<String> arguments) {
        this.arguments = arguments;
    }

    public abstract boolean supportWatchdog();

    public abstract ExecuteWatchdog getWatchdog();
}
