package org.archer.archermq.protocol.model;

import org.apache.commons.lang3.StringUtils;
import org.archer.archermq.common.FeatureBased;
import org.archer.archermq.common.log.BizLogUtil;
import org.archer.archermq.common.log.LogConstants;
import org.archer.archermq.common.log.LogInfo;
import org.archer.archermq.common.utils.ApplicationContextHolder;
import org.archer.archermq.protocol.constants.ClassEnum;
import org.archer.archermq.protocol.constants.ExceptionMessages;
import org.archer.archermq.protocol.constants.FeatureKeys;
import org.archer.archermq.protocol.constants.MethodEnum;
import org.archer.archermq.protocol.transport.ChannelException;
import org.archer.archermq.protocol.transport.ConnectionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import java.util.Objects;

/**
 * 对应于amqp的channel类
 *
 * @author dongyue
 * @date 2020年04月20日18:51:38
 */
public final class Channel extends FeatureBased implements Class{

    private static final Logger logger = LoggerFactory.getLogger("");

    private static final int classId = 20;

    @Override
    public int classId() {
        return classId;
    }

    @Override
    public String desc() {
        return "work with channels";
    }

    /**
     * 开启信道
     */
    public class Open implements Command<OpenOk> {

        private final String reserved1;

        public Open(String reserved1) {
            this.reserved1 = reserved1;
        }

        @Override
        public String desc() {
            return "open a channel for use";
        }

        @Override
        public int commandId() {
            return 10;
        }

        @Override
        public OpenOk execute() {
            org.archer.archermq.protocol.Channel channel = (org.archer.archermq.protocol.Channel) getFeature(FeatureKeys.Command.AMQP_CHANNEL);
            if(Objects.nonNull(channel)){
                throw new ChannelException(ExceptionMessages.ChannelErrors.PRECONDITION_FAILED);
            }
            String channelId = openNewChannel();
            return new OpenOk(channelId);

        }

        private String openNewChannel() {
            return null;
        }

        public String getReserved1() {
            return reserved1;
        }
    }

    public class OpenOk implements Command<Void>{

        private final String reserved1;

        public OpenOk(String reserved1) {
            this.reserved1 = reserved1;
        }

        @Override
        public String desc() {
            return "signal that the channel is ready";
        }

        @Override
        public int commandId() {
            return 11;
        }

        @Override
        public Void execute() {
            throw new ConnectionException(ExceptionMessages.ConnectionErrors.COMMAND_INVALID);
        }

        public String getReserved1() {
            return reserved1;
        }
    }

    public class Flow extends FeatureBased implements Command<FlowOk>{

        private final boolean active;

        public Flow(boolean active) {
            this.active = active;
        }

        @Override
        public String desc() {
            return "enable/disable flow from peer";
        }

        @Override
        public int commandId() {
            return 20;
        }

        @Override
        public FlowOk execute() {
            org.archer.archermq.protocol.Channel channel = (org.archer.archermq.protocol.Channel) getFeature(FeatureKeys.Command.AMQP_CHANNEL);
            channel.setFlow(active);
            return new FlowOk(active);
        }

        public boolean isActive() {
            return active;
        }
    }

    public class FlowOk implements Command<Void>{

        private final boolean active;

        public FlowOk(boolean active) {
            this.active = active;
        }

        @Override
        public String desc() {
            return "confirm a flow method";
        }

        @Override
        public int commandId() {
            return 21;
        }

        @Override
        public Void execute() {
            throw new ConnectionException(ExceptionMessages.ConnectionErrors.COMMAND_INVALID);
        }

        public boolean isActive() {
            return active;
        }
    }

    /**
     * 关闭信道
     */
    public class Close implements Command<CloseOk> {

        private final String replyCode;

        private final String replyText;

        private final Short classId;

        private final Short methodId;

        public Close(String replyCode, String replyText, Short classId, Short methodId) {
            this.replyCode = replyCode;
            this.replyText = replyText;
            this.classId = classId;
            this.methodId = methodId;
        }

        @Override
        public String desc() {
            return "request a channel close";
        }

        @Override
        public int commandId() {
            return 40;
        }

        @Override
        public CloseOk execute() {
            LogInfo logInfo = BizLogUtil.start().setLayer(LogConstants.MODEL_LAYER).setType(LogConstants.CONNECTION_CLOSED);

            logInfo.addContent(LogConstants.PEER_IP, (String) getFeature(FeatureKeys.Command.PEER_IP));
            logInfo.addContent(LogConstants.PEER_PORT, (String) getFeature(FeatureKeys.Command.PEER_PORT));
            logInfo.addContent(LogConstants.VIRTUALHOST_NAME, (String) getFeature(FeatureKeys.Command.VIRTUALHOST_NAME));

            if (StringUtils.isNotBlank(replyCode)) {
                logInfo.addContent(LogConstants.REPLY_CODE, replyCode);
            }

            if (StringUtils.isNotBlank(replyText)) {
                logInfo.addContent(LogConstants.REPLY_TEXT, replyText);
            }

            ClassEnum clazz = ClassEnum.getByVal(classId.intValue());
            MethodEnum method = MethodEnum.getByVal(methodId.intValue());

            if (Objects.nonNull(clazz) && Objects.nonNull(method) && clazz.allow(method)) {
                logInfo.addContent(LogConstants.CLASS_NAME, clazz.name());
                logInfo.addContent(LogConstants.METHOD_NAME, method.name());
            }

            BizLogUtil.record(logInfo, logger);
            return new CloseOk();
        }
    }

    public class CloseOk implements Command<Void>{

        @Override
        public String desc() {
            return "confirm a channel close";
        }

        @Override
        public int commandId() {
            return 41;
        }

        @Override
        public Void execute() {
            org.archer.archermq.protocol.Channel channel = (org.archer.archermq.protocol.Channel) getFeature(FeatureKeys.Command.AMQP_CHANNEL);
            channel.close();
            return null;
        }
    }

    static {
        ApplicationContext context = ApplicationContextHolder.getApplicationContext();
        MethodResolver methodResolver = context.getBean(MethodResolver.class);
        methodResolver.register(classId,41);
        methodResolver.register(classId,40);
        methodResolver.register(classId,21);
        methodResolver.register(classId,20);
        methodResolver.register(classId,11);
        methodResolver.register(classId,10);
    }

}
