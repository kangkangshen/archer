package org.archer.archermq.protocol.transport.handler;


import com.alibaba.fastjson.JSON;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.archer.archermq.common.annotation.Log;
import org.archer.archermq.common.log.BizLogUtil;
import org.archer.archermq.common.log.LogConstants;
import org.archer.archermq.common.log.LogInfo;
import org.archer.archermq.protocol.constants.ExceptionMessages;
import org.archer.archermq.protocol.constants.FrameTypeEnum;
import org.archer.archermq.protocol.transport.ConnectionException;
import org.archer.archermq.protocol.transport.Frame;
import org.archer.archermq.protocol.transport.FrameDispatcher;
import org.archer.archermq.protocol.transport.FrameHandler;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 帧分派器的默认实现，默认根据帧的类型找到特定的帧处理器进行分派
 */
@Component
public class StandardFrameDispatcher extends ChannelInboundHandlerAdapter implements FrameDispatcher, ApplicationContextAware, InitializingBean {

    private ApplicationContext applicationContext;

    private final Map<FrameTypeEnum, FrameHandler> frameHandlerMap = Maps.newConcurrentMap();

    private final Multimap<FrameTypeEnum, FrameHandler> backupFrameHandlerMap = ArrayListMultimap.create();


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Frame returnedFrame = dispatchFrame((Frame) msg);
        if (Objects.nonNull(returnedFrame)) {
            ctx.writeAndFlush(returnedFrame);
        }
    }

    @Override
    @Log(layer = LogConstants.TRANSPORT_LAYER)
    public Frame dispatchFrame(Frame rawFrame) {



        //目前仅支持直接分派，未来会加进去更多的分派规则
        FrameTypeEnum frameTypeEnum = rawFrame.type();
        if (CollectionUtils.isEmpty(frameHandlerMap)) {
            throw new UnsupportedOperationException("there is no one frame handler,may be some code change this frameHandlerMap");
        }

        FrameHandler frameHandler = frameHandlerMap.get(frameTypeEnum);
        //double check
        if (Objects.nonNull(frameHandler) && frameHandler.canHandle(frameTypeEnum)) {
            Assert.isTrue(frameHandler.validate(rawFrame), "frame validate failed" + JSON.toJSONString(rawFrame));
            return frameHandler.handleFrame(rawFrame);
        }
        throw new UnsupportedOperationException("there is no ANY  frame handler can handle this frame.check you frame content.");
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        LogInfo logInfo = BizLogUtil.start().setLayer(LogConstants.TRANSPORT_LAYER);
        try {
            Map<String, FrameHandler> frameHandlerFromSpring = applicationContext.getBeansOfType(FrameHandler.class);
            logInfo.addContent(LogConstants.REGISTERED_FRAME_HANDLER, JSON.toJSONString(frameHandlerFromSpring.keySet()));

            //过滤
            List<String> usingFrameHandler = Lists.newLinkedList();
            List<String> backupFrameHandler = Lists.newArrayList();
            FrameTypeEnum[] frameTypes = FrameTypeEnum.values();
            for (FrameTypeEnum frameType : frameTypes) {
                frameHandlerFromSpring.forEach((beanName, handler) -> {
                    if (handler.canHandle(frameType)) {
                        if (Objects.isNull(frameHandlerMap.putIfAbsent(frameType, handler))) {
                            usingFrameHandler.add(beanName);
                        } else {
                            backupFrameHandlerMap.put(frameType, handler);
                            backupFrameHandler.add(beanName);
                        }
                    }
                });
            }
            //再次进行检查以便每种frame都能得到处理 todo dongyue
            List<String> unsupportedFrameTypes = Lists.newArrayList();
            for (FrameTypeEnum frameType : frameTypes) {
                if (!frameHandlerMap.containsKey(frameType)) {
                    unsupportedFrameTypes.add(frameType.getDesc());
                }
            }
            if (unsupportedFrameTypes.size() > 0) {
                throw new UnsupportedOperationException(JSON.toJSONString(unsupportedFrameTypes));
            }
            logInfo.addContent(LogConstants.USING_FRAME_HANDLER, JSON.toJSONString(usingFrameHandler));
            logInfo.addContent(LogConstants.BACKUP_FRAME_HANDLER, JSON.toJSONString(backupFrameHandler));
            BizLogUtil.end();
        } catch (Exception e) {
            logInfo.setType(LogConstants.EXCEPTION_THROW);
            logInfo.addContent(LogConstants.EXCEPTION_STACK, JSON.toJSONString(e.getStackTrace()));
        }


    }

    public Map<FrameTypeEnum, FrameHandler> getFrameHandlerMap() {
        return frameHandlerMap;
    }

    public Multimap<FrameTypeEnum, FrameHandler> getBackupFrameHandlerMap() {
        return backupFrameHandlerMap;
    }
}
