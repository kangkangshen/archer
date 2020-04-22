package org.archer.archermq.common.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public class LogInfo {


    private final static String DEFAULT_DELIMITER = ":";

    private Date createTime;

    private String layer;

    private String type;

    private StringBuffer content = new StringBuffer();

    private String traceId;

    private String result;


    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getLayer() {
        return layer;
    }

    public LogInfo setLayer(String layer) {
        this.layer = layer;
        return this;
    }

    public String getType() {
        return type;
    }

    public LogInfo setType(String type) {
        this.type = type;
        return this;
    }

    public String getContent() {
        return content.toString();
    }

    public LogInfo setContent(String content) {
        this.content = new StringBuffer(content);
        return this;
    }

    public LogInfo addContent(String logContentKey, String content) {
        this.content.append(logContentKey).append(DEFAULT_DELIMITER).append(content);
        return this;
    }

    public LogInfo appendContent(String content) {
        this.content.append(content);
        return this;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "LogInfo{" +
                "createTime=" + createTime +
                ", layer='" + layer + '\'' +
                ", type='" + type + '\'' +
                ", content=" + content +
                ", traceId='" + traceId + '\'' +
                ", result='" + result + '\'' +
                '}';
    }

}
