package org.archer.archermq.protocol.model;

import io.netty.channel.Channel;
import org.apache.commons.lang3.StringUtils;
import org.archer.archermq.common.FeatureBased;
import org.archer.archermq.common.log.BizLogUtil;
import org.archer.archermq.common.log.LogConstants;
import org.archer.archermq.common.log.LogInfo;
import org.archer.archermq.common.utils.ApplicationContextHolder;
import org.archer.archermq.protocol.Server;
import org.archer.archermq.protocol.constants.ClassEnum;
import org.archer.archermq.protocol.constants.ExceptionMessages;
import org.archer.archermq.protocol.constants.FeatureKeys;
import org.archer.archermq.protocol.constants.MethodEnum;
import org.archer.archermq.protocol.transport.ConnectionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import java.util.Objects;

/**
 * 对应于amqp的connection类
 *
 * @author dongyue
 * @date 2020年04月20日18:49:31
 */
public final class Connection extends FeatureBased implements Class {

    private static final Logger logger = LoggerFactory.getLogger("");
    private static final int classId = 10;

    @Override
    public int classId() {
        return classId;
    }

    @Override
    public String desc() {
        return "work with socket connections";
    }

    /**
     * 建立连接相关
     */
    public class Start implements Command<StartOk> {

        @Override
        public String desc() {
            return "start connection negotiation";
        }

        @Override
        public int commandId() {
            return 10;
        }

        @Override
        public StartOk execute() {
            throw new ConnectionException(ExceptionMessages.ConnectionErrors.COMMAND_INVALID);
        }
    }

    public class StartOk extends FeatureBased implements Command<Void> {

        private String mechanism;
        private String response;
        private String locale;

        @Override
        public String desc() {
            return "select security mechanism and locale";
        }

        @Override
        public int commandId() {
            return 11;
        }

        @Override
        public Void execute() {
            //1.检查加密算法是否支持
            checkMechanismSupport(mechanism);
            //2.检查locale是否支持
            checkLocaleSupport(locale);
            //3.log response
            LogInfo logInfo = BizLogUtil.start().setLayer(LogConstants.MODEL_LAYER).setType(LogConstants.METHOD_INVOKE);
            logInfo.addContent(LogConstants.RESPONSE, response);
            return null;
        }

        private void checkMechanismSupport(String mechanism) {
        }

        private void checkLocaleSupport(String locale) {
        }


        public String getMechanism() {
            return mechanism;
        }

        public String getResponse() {
            return response;
        }

        public String getLocale() {
            return locale;
        }
    }

    public class Secure implements Command<SecureOk> {

        @Override
        public String desc() {
            return "security mechanism challenge";
        }

        @Override
        public int commandId() {
            return 20;
        }

        @Override
        public SecureOk execute() {
            throw new ConnectionException(ExceptionMessages.ConnectionErrors.COMMAND_INVALID);
        }

    }

    public class SecureOk implements Command<Void> {

        private final String response;

        public SecureOk(String response) {
            this.response = response;
        }

        @Override
        public String desc() {
            return "security mechanism response";
        }

        @Override
        public int commandId() {
            return 21;
        }

        @Override
        public Void execute() {
            LogInfo logInfo = BizLogUtil.start().setLayer(LogConstants.MODEL_LAYER).setType(LogConstants.METHOD_INVOKE);
            logInfo.addContent(LogConstants.RESPONSE, response);
            return null;
        }

        public String getResponse() {
            return response;
        }
    }


    /**
     * 建立连接相关
     */
    public class Tune implements Command<TuneOk> {

        private final short maxChannel;

        private final int maxFrame;

        private final short heartbeat;

        public Tune(short maxChannel, int maxFrame, short heartbeat) {
            this.maxChannel = maxChannel;
            this.maxFrame = maxFrame;
            this.heartbeat = heartbeat;
        }

        @Override
        public String desc() {
            return "propose connection tuning parameters";
        }

        @Override
        public int commandId() {
            return 30;
        }

        @Override
        public TuneOk execute() {
            throw new ConnectionException(ExceptionMessages.ConnectionErrors.COMMAND_INVALID);
        }

        public short getMaxChannel() {
            return maxChannel;
        }

        public int getMaxFrame() {
            return maxFrame;
        }

        public short getHeartbeat() {
            return heartbeat;
        }
    }

    public class TuneOk implements Command<Void> {

        private final short maxChannel;

        private final int maxFrame;

        private final short heartbeat;

        public TuneOk(short maxChannel, int maxFrame, short heartbeat) {
            this.maxChannel = maxChannel;
            this.maxFrame = maxFrame;
            this.heartbeat = heartbeat;
        }

        @Override
        public String desc() {
            return "negotiate connection tuning parameters";
        }

        @Override
        public int commandId() {
            return 31;
        }

        @Override
        public Void execute() {
            return null;
        }

        public short getMaxChannel() {
            return maxChannel;
        }

        public int getMaxFrame() {
            return maxFrame;
        }

        public short getHeartbeat() {
            return heartbeat;
        }
    }

    /**
     * 建立连接相关
     */
    public class Open extends FeatureBased implements Command<OpenOk> {

        private final String virtualHost;

        private final String reserved1;

        private final String reserved2;

        public Open(String virtualHost, String reserved1, String reserved2) {
            this.virtualHost = virtualHost;
            this.reserved1 = reserved1;
            this.reserved2 = reserved2;
        }

        @Override
        public String desc() {
            return "open connection to virtual host";
        }

        @Override
        public int commandId() {
            return 40;
        }

        @Override
        public OpenOk execute() {
            //检查virtualHost是否存在
            Server server = (Server) getFeature(FeatureKeys.Command.SERVER);
            if (!server.contains(virtualHost)) {
                throw new ConnectionException(ExceptionMessages.ConnectionErrors.INVALID_PATH);
            }
            //白名单身份检查
            if (!authorizationCheck(reserved1)) {
                throw new ConnectionException(ExceptionMessages.ConnectionErrors.NOT_ALLOWED);
            }
            return new OpenOk(reserved1);
        }

        /**
         * 身份校验
         *
         * @param identity 身份
         * @return 是否允许该身份登录，使用身份白名单设计
         */
        private boolean authorizationCheck(String identity) {
            return true;
        }

        public String getVirtualHost() {
            return virtualHost;
        }

        public String getReserved1() {
            return reserved1;
        }

        public String getReserved2() {
            return reserved2;
        }
    }

    public class OpenOk implements Command<Void> {


        private final String reserved1;

        public OpenOk(String reserved1) {
            this.reserved1 = reserved1;
        }

        @Override
        public String desc() {
            return null;
        }

        @Override
        public int commandId() {
            return 41;
        }

        @Override
        public Void execute() {
            throw new ConnectionException(ExceptionMessages.ConnectionErrors.COMMAND_INVALID);
        }

        public String getReserved1() {
            return reserved1;
        }
    }

    /**
     * 关闭连接
     */
    public class Close extends FeatureBased implements Command<CloseOk> {

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
            return "request a connection close";
        }

        @Override
        public int commandId() {
            return 50;
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

        public String getReplyCode() {
            return replyCode;
        }

        public String getReplyText() {
            return replyText;
        }

        public Short getClassId() {
            return classId;
        }

        public Short getMethodId() {
            return methodId;
        }
    }

    /**
     * 传输层连接的释放是任何一方接收到CloseOk帧之后完成的，
     * 因为我们使用TCP作为传输层实现，因此任何一方断开连接，都会使连接断开，不会造成连接的泄露
     * This method confirms a Connection.Close method and tells the recipient
     * that it is safe to release resources for the connection and close the socket.
     */
    public class CloseOk implements Command<Void> {

        @Override
        public String desc() {
            return "confirm a connection close";
        }

        @Override
        public int commandId() {
            return 51;
        }

        @Override
        public Void execute() {

            Channel channel = (Channel) getFeature(FeatureKeys.Command.TCP_CHANNEL);
            try {
                channel.close().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;

        }
    }

    static {
        ApplicationContext context = ApplicationContextHolder.getApplicationContext();
        MethodResolver methodResolver = context.getBean(MethodResolver.class);
        methodResolver.register(classId,51);
        methodResolver.register(classId,50);
        methodResolver.register(classId,41);
        methodResolver.register(classId,40);
        methodResolver.register(classId,31);
        methodResolver.register(classId,30);
        methodResolver.register(classId,21);
        methodResolver.register(classId,20);
        methodResolver.register(classId,11);
        methodResolver.register(classId,10);
    }


}
