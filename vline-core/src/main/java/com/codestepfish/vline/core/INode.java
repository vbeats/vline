package com.codestepfish.vline.core;

public interface INode<T> {

    /**
     * 节点通信初始化
     */
    void init();

    /**
     * 节点通信资源销毁
     */
    void destroy() throws Exception;

    /**
     * 推送data
     */
    void sendData(T data);
}
