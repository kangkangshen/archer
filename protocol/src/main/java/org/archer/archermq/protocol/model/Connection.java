package org.archer.archermq.protocol.model;

import io.netty.channel.Channel;
import org.apache.commons.lang3.StringUtils;
import org.archer.archermq.common.FeatureBased;
import org.archer.archermq.common.log.BizLogUtil;
import org.archer.archermq.common.log.LogConstants;
import org.archer.archermq.common.log.LogInfo;
import org.archer.archermq.common.utils.ApplicationContextHolder;
import org.archer.archermq.protocol.Server;
import org.archer.archermq.protocol.VirtualHost;
import org.archer.archermq.protocol.constants.ClassEnum;
import org.archer.archermq.protocol.constants.ExceptionMessages;
import org.archer.archermq.protocol.constants.FeatureKeys;
import org.archer.archermq.protocol.constants.MethodEnum;
import org.archer.archermq.protocol.transport.ConnectionException;
import org.archer.archermq.protocol.transport.StandardAmqpConnection;
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
    public class Start extends BaseCommand<StartOk> {

        public Start() {
            super(classId, 10);
        }

        @Override
        public String desc() {
            return "start connection negotiation";
        }

        @Override
        public StartOk execute() {
            throw new ConnectionException(ExceptionMessages.ConnectionErrors.COMMAND_INVALID);
        }
    }

    public class StartOk extends BaseCommand<Void> {

        private final String mechanism;
        private final String response;
        private final String locale;

        public StartOk(String mechanism, String response, String locale) {
            super(classId, 11);
            this.mechanism = mechanism;
            this.response = response;
            this.locale = locale;
        }

        @Override
        public String desc() {
            return "select security mechanism and locale";
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

    public class Secure extends BaseCommand<SecureOk> {

        public Secure() {
            super(classId, 20);
        }

        @Override
        public String desc() {
            return "security mechanism challenge";
        }

        @Override
        public SecureOk execute() {
            throw new ConnectionException(ExceptionMessages.ConnectionErrors.COMMAND_INVALID);
        }

    }

    public class SecureOk extends BaseCommand<Void> {

        private final String response;


        public SecureOk(String response) {
            super(classId, 21);
            this.response = response;
        }

        @Override
        public String desc() {
            return "security mechanism response";
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
    public class Tune extends BaseCommand<TuneOk> {

        private final short maxChannel;

        private final int maxFrame;

        private final short heartbeat;

        public Tune(short maxChannel, int maxFrame, short heartbeat) {
            super(classId, 30);
            this.maxChannel = maxChannel;
            this.maxFrame = maxFrame;
            this.heartbeat = heartbeat;
        }

        @Override
        public String desc() {
            return "propose connection tuning parameters";
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

    public class TuneOk extends BaseCommand<Void> {

        private final short maxChannel;

        private final int maxFrame;

        private final short heartbeat;

        public TuneOk(short maxChannel, int maxFrame, short heartbeat) {
            super(classId, 31);
            this.maxChannel = maxChannel;
            this.maxFrame = maxFrame;
            this.heartbeat = heartbeat;
        }

        @Override
        public String desc() {
            return "negotiate connection tuning parameters";
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
    public class Open extends BaseCommand<OpenOk> {

        private final String virtualHost;

        private final String reserved1;

        private final String reserved2;

        public Open(String virtualHost, String reserved1, String reserved2) {
            super(classId, 40);
            this.virtualHost = virtualHost;
            this.reserved1 = reserved1;
            this.reserved2 = reserved2;
        }

        @Override
        public String desc() {
            return "open connection to virtual host";
        }


        @Override
        public OpenOk execute() {
            //检查virtualHost是否存在
            Server server = (Server) getFeature(FeatureKeys.Command.SERVER);
            if (!server.contains(virtualHost)) {
                throw new ConnectionException(ExceptionMessages.ConnectionErrors.INVALID_PATH);
            }
            VirtualHost virtualHostInstance = server.get(virtualHost);
            //白名单身份检查
            if (!authorizationCheck(reserved1)) {
                throw new ConnectionException(ExceptionMessages.ConnectionErrors.NOT_ALLOWED);
            }
            //todo dongyue
            org.archer.archermq.protocol.Connection amqpConn = new StandardAmqpConnection(virtualHostInstance);
            virtualHostInstance.getConnRegistry().register(amqpConn.id(),amqpConn);
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

    public class OpenOk extends BaseCommand<Void> {


        private final String reserved1;

        public OpenOk(String reserved1) {
            super(classId, 41);
            this.reserved1 = reserved1;
        }

        @Override
        public String desc() {
            return null;
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
    public class Close extends BaseCommand<CloseOk> {

        private final String replyCode;

        private final String replyText;

        private final Short classId;

        private final Short methodId;

        public Close(String replyCode, String replyText, Short classId, Short methodId) {
            super(classId, 50);
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
    public class CloseOk extends BaseCommand<Void> {

        public CloseOk() {
            super(classId, 51);
        }

        @Override
        public String desc() {
            return "confirm a connection close";
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
        methodResolver.register(classId, 51, CloseOk.class);
        methodResolver.register(classId, 50, Close.class);
        methodResolver.register(classId, 41, OpenOk.class);
        methodResolver.register(classId, 40, Open.class);
        methodResolver.register(classId, 31, TuneOk.class);
        methodResolver.register(classId, 30, Tune.class);
        methodResolver.register(classId, 21, SecureOk.class);
        methodResolver.register(classId, 20, Secure.class);
        methodResolver.register(classId, 11, StartOk.class);
        methodResolver.register(classId, 10, Start.class);
    }


}
