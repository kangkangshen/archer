package org.archer.archermq.protocol.transport;


import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 标准心跳帧实现
 * 心跳是一种设计用来撤销(undo)TCP/IP功能的技术,也就是说在长时间超时后，它有能力通过关闭broker物理连接来进行恢复．
 * 在某些情景下，我们需要快速知道节点连接是否断开了，或者是由于什么原因不能响应了.
 * 因为心跳可以在较低水平上进行，我们在传输层次上按节点交换的特定帧类型来处理，而不是按类方法.
 * 需要注意：
 * 1.心跳帧的通道编号必须为０. 收到无效心跳帧的节点需使用501回复码(帧错误)来抛出异常.
 * 2.如果节点不支持心跳，它必须在不发出错误或失败信号的情况下丢弃心跳帧．
 * 3.client收到Connection.Tune方法后，必须要开始发送心跳, 并在收到Connection.Open后，必须要开始监控.server在收到Connection.Tune-Ok后，需要开始发送和监控心跳
 * 4.节点应该尽最大努力按固定频率来发送心跳. 心跳可在任何时候发送． 任何发送字节都可作为心跳的有效替代,因此当超过固定频率还没有发送非AMQP心跳时，必须发送心跳.如果节点在两个心跳间隔或更长时间内，未探测到传入的心跳,它可在不遵循Connection.Close/Close-Ok握手的情况下，关闭连接，并记录错误信息.
 * 5.心跳应该具有持续性，除非socket连接已经被关闭, 包括在Connection.Close/Close-Ok 握手期间或之后的时间.
 * @author dongyue
 * @date 2020年04月14日13:00:26
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class StandardHeartbeatFrame extends StandardFrame{

    private long createdTime;

}
