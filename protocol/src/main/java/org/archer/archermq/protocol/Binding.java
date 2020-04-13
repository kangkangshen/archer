package org.archer.archermq.protocol;

/**
 * 消息队列和交换器之间的关联。
 * 1.绑定器表示消息队列和交换器之间的关联关系（对于实现者和用户而言，binding应该是一个实体，而不是抽象的relationship）。
 * 2.绑定器指定了路由参数，这些参数告诉交换器哪些消息可以发送给队列。
 * 3.绑定器的生存期由使用它的消息队列和交换器决定，当消息队列或者交换器被销毁，绑定器也会被销毁。
 *
 * @author dongyue
 * @date 2020年04月13日21:24:43
 */
public interface Binding {
}
