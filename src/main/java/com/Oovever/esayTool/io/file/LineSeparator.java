package com.Oovever.esayTool.io.file;

/**
 * @author OovEver
 * 2018/6/10 16:40
 */
public enum LineSeparator {
    /** Windows系统换行符："\r\n" */
    WINDOWS("\r\n");

    private String value;

    private LineSeparator(String lineSeparator) {
        this.value = lineSeparator;
    }

    public String getValue() {
        return this.value;
    }
}
