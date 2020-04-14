package org.archer.archermq.protocol.transport;


/**
 * 标准方法帧实现
 * 方法帧可以携带高级协议命令(我们称之为方法(methods)).一个方法帧携带一个命令
 *
 * @author dongyue
 * @date 2020年04月14日13:04:44
 */
public class StandardMethodFrame extends StandardFrame {

    private short rawClassId;

    private short rawMethodId;

    public short getRawClassId() {
        return rawClassId;
    }

    public void setRawClassId(short rawClassId) {
        this.rawClassId = rawClassId;
    }

    public short getRawMethodId() {
        return rawMethodId;
    }

    public void setRawMethodId(short rawMethodId) {
        this.rawMethodId = rawMethodId;
    }
}
