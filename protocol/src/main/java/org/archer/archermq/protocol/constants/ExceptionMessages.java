package org.archer.archermq.protocol.constants;


import org.apache.commons.lang3.StringUtils;

/**
 * 异常信息常量类
 *
 * @author dongyue
 * @date 2020年04月14日16:37:08
 */
public class ExceptionMessages {

    private final int id;
    private final int errType;
    private final String name;
    private final String desc;

    public ExceptionMessages(int id, int errType, String name, String desc) {
        this.id = id;
        this.errType = errType;
        this.name = name;
        this.desc = desc;
    }

    /**
     * These codes are all associated with failures that affect the current channel but not other channels in the same connection;
     */
    public interface ChannelErrors {
        ExceptionMessages CONTENT_TOO_LARGE = build("content-too-large", 311, 0, "The client attempted to transfer content larger than the server could accept at the present time. The client may retry at a later time.");
        ExceptionMessages NO_CONSUMERS = build("no-consumers", 313, 0, "When the exchange cannot deliver to a consumer when the immediate flag is set. As a result of pending data on the queue or the absence of any consumers of the queue.");
        ExceptionMessages ACCESS_REFUSED = build("access-refused", 403, 0, "The client attempted to work with a server entity to which it has no access due to security settings.");
        ExceptionMessages NOT_FOUND = build("not-found", 404, 0, "The client attempted to work with a server entity that does not exist.");
        ExceptionMessages RESOURCE_LOCKED = build("resource-locked", 405, 0, "The client attempted to work with a server entity to which it has no access because another client is working with it.");
        ExceptionMessages PRECONDITION_FAILED = build("precondition-failed", 406, 0, "The client requested a method that was not allowed because some precondition failed.");
        ExceptionMessages EXCLUSIVE = build("exclusive",407,0,"The channel want to occupy the queue but queue is not exclusive mode or the message queue is exclusive and already occupied by the other.");

    }

    public interface ConnectionErrors {
        ExceptionMessages CONNECTION_FORCED = build("connection-forced", 320, 1, "An operator intervened to close the connection for some reason. The client may retry at some later date.");
        ExceptionMessages INVALID_PATH = build("invalid-path", 402, 1, "The client tried to work with an unknown virtual host.");
        ExceptionMessages FRAME_ERR = build("frame-err", 501, 1, "The sender sent a malformed frame that the recipient could not decode. This strongly implies a programming error in the sending peer.");
        ExceptionMessages SYNTAX_ERR = build("syntax-err", 502, 1, "The sender sent a frame that contained illegal values for one or more fields. This strongly implies a programming error in the sending peer.");
        ExceptionMessages COMMAND_INVALID = build("command-invalid", 503, 1, "The client sent an invalid sequence of frames, attempting to perform an operation that was considered invalid by the server. This usually implies a programming error in the client.");
        ExceptionMessages CHANNEL_ERR = build("channel-err", 504, 1, "The client attempted to work with a channel that had not been correctly opened. This most likely indicates a fault in the client layer.");
        ExceptionMessages UNEXPECTED_FRAME = build("unexpected-frame", 505, 1, "The peer sent a frame that was not expected, usually in the context of a content header and body. This strongly indicates a fault in the peer's content processing.");
        ExceptionMessages RESOURCE_ERR = build("resource-err", 506, 1, "The server could not complete the method because it lacked sufficient resources. This may be due to the client creating too many of some type of entity.");
        ExceptionMessages NOT_ALLOWED = build("not-allowed", 530, 1, "The client tried to work with some entity in a manner that is prohibited by the server, due to security settings or by some other criteria.");
        ExceptionMessages NOT_IMPLEMENTED = build("not-implemented", 540, 1, "The client tried to use functionality that is not implemented in the server.");
        ExceptionMessages INTERNAL_ERR = build("internal-err", 541, 1, "The server could not complete the method because of an internal error. The server may require intervention by an operator in order to resume normal operations.");

    }

    public interface SystemErrors {
        ExceptionMessages STATE_ERR = build("state-err", 641, 2, "current state not allow change to target state,check your invocation.");
        ExceptionMessages FEATURE_ERR = build("feature-err", 642, 2, "cannot find out the feature or feature type is wrong");
    }

    public static ExceptionMessages build(String name, int id, int errType, String desc) {
        return new ExceptionMessages(id, errType, name, desc);
    }


    public int getId() {
        return id;
    }

    public int getErrType() {
        return errType;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    @Override
    public String toString() {
        return "ExceptionMessages{" +
                "id=" + id +
                ", errType=" + errType +
                ", name='" + name + '\'' +
                ", desc='" + desc + '\'' +
                '}';
    }

    /**
     * 基于占位符的异常信息生成方法
     *
     * @param exceptionMsgTemplate 带占位符的异常信息模板，使用#作为占位符
     * @param msg                  用于替换占位符的值
     * @return 新的异常信息信息
     * @throws IllegalArgumentException msg.length和exceptionMsgTemplate占位符个数不匹配或者exceptionMsgTemplate
     *                                  为blank,或者msg为empty
     */
    public static String buildExceptionMsgWithTemplate(String exceptionMsgTemplate, String... msg) {
        if (StringUtils.isNotBlank(exceptionMsgTemplate) && msg.length > 0) {
            String[] splits = exceptionMsgTemplate.split("#");
            if (splits.length == msg.length + 1) {
                StringBuilder stringBuilder = new StringBuilder(splits[0]);
                for (int i = 0; i < msg.length; i++) {
                    stringBuilder.append(msg[i]).append(splits[i + 1]);
                }
                return stringBuilder.toString();
            }
            throw new IllegalArgumentException();
        }
        throw new IllegalArgumentException();
    }

}
