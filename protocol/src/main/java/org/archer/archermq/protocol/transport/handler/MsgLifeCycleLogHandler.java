package org.archer.archermq.protocol.transport.handler;

import com.google.common.collect.Lists;
import org.archer.archermq.protocol.LifeCycleEvent;
import org.archer.archermq.protocol.LifeCycleListener;
import org.archer.archermq.protocol.Message;
import org.archer.archermq.protocol.constants.LifeCyclePhases;
import org.archer.archermq.protocol.persistence.domain.MsgContent;
import org.archer.archermq.protocol.persistence.domain.MsgLifeCycle;
import org.archer.archermq.protocol.persistence.repository.MsgContentRepository;
import org.archer.archermq.protocol.persistence.repository.MsgLifeCycleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class MsgLifeCycleLogHandler implements LifeCycleListener {


    @Resource
    private MsgContentRepository msgContentRepository;

    @Resource
    private MsgLifeCycleRepository msgLifeCycleRepository;


    @Override
    public String name() {
        return MsgLifeCycleLogHandler.class.getName();
    }

    @Override
    public String description() {
        return "用以记录msg生命周期";
    }

    @Override
    public boolean interested(LifeCycleEvent<?> event) {
        return event.getTarget() instanceof Message;
    }

    @Override
    public void responseEvent(LifeCycleEvent<?> event) {

        Message message = (Message) event.getTarget();
        if(isNewMsg(message.currPhase())){
            msgContentRepository.save(createMsgContent(message));
        }
        msgLifeCycleRepository.save(createMsgLifeCycle(message));


    }

    private MsgContent createMsgContent(Message message){
        return null;
    }

    private MsgLifeCycle createMsgLifeCycle(Message message){
        return null;
    }

    private boolean isNewMsg(String msgPhase){
        List<String> newMsgPhase = Lists.newArrayList(LifeCyclePhases.Message.ACCEPT,LifeCyclePhases.Message.CREATE);
        return newMsgPhase.contains(msgPhase);
    }

}
