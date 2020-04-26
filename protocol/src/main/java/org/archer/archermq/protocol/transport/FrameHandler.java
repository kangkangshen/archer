package org.archer.archermq.protocol.transport;

import org.archer.archermq.protocol.constants.FrameTypeEnum;

/**
 * 用于解析特定的帧，如方法帧，内容帧，心跳帧等，当然也可能仅根据帧type进行处理，依赖于多种分派规则
 * 最典型的就是依赖帧类型进行分别处理
 *
 * @author dongyue
 * @date 2020年04月20日15:06:22
 */
public interface FrameHandler {

    /**
     * 检查当前逻辑能够处理指定类型的frame
     *
     * @param targetType 欲交付当前handler处理的frame
     * @return true if当前handler能够处理该frame
     */
    boolean canHandle(FrameTypeEnum targetType);


    /**
     * 检查当前frame的各项参数是否合法
     * 因为每种帧差别很大，校验逻辑相对复杂，因此将其抽出成一个接口来做
     *
     * @param targetFrame 欲处理的frame
     * @return true if当前frame合法
     */
    boolean validate(Frame targetFrame);

    /**
     * 处理该帧
     *
     * @param frame 欲处理的帧
     * @return 处理结果，返回响应，如果有的话；返回结果可能为空，取决于具体的帧处理逻辑
     * @throws FrameHandleException 如果真处理过程中出现异常
     */
    Frame handleFrame(Frame frame);

}
