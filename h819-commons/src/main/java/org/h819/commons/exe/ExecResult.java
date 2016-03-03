package org.h819.commons.exe;

/**
 * Description : TODO()
 * User: h819
 * Date: 2016-1-23
 * Time: 4:52
 * To change this template use File | Settings | File Templates.
 */
public class ExecResult {

    private int exitCode;
    private String stdout;
    private String stderr;
    private String codeInfo;

    public ExecResult() {
    }

    public ExecResult(int exitCode, String stdout, String stderr, String codeInfo) {
        this.exitCode = exitCode;
        this.stdout = stdout;
        this.stderr = stderr;
        this.codeInfo = codeInfo;
    }

    public int getExitCode() {
        return exitCode;
    }

    public void setExitCode(int exitCode) {
        this.exitCode = exitCode;
    }

    public String getStdout() {
        return stdout;
    }

    public void setStdout(String stdout) {
        this.stdout = stdout;
    }

    public String getStderr() {
        return stderr;
    }

    public void setStderr(String stderr) {
        this.stderr = stderr;
    }

    public String getCodeInfo() {
        return codeInfo;
    }

    public void setCodeInfo(String codeInfo) {
        this.codeInfo = codeInfo;
    }
}
