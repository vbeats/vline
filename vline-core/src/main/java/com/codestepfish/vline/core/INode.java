package com.codestepfish.vline.core;

public interface INode<T> {

    /**
     * 节点初始化
     */
    void init();

    /**
     * 节点资源销毁
     */
    void destroy();

    /**
     * 当前节点 接收data
     */
    void receiveData(T data);
}
