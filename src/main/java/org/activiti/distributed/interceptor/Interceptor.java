package org.activiti.distributed.interceptor;

/**
 * Created by zoupeng on 15/9/28.
 */
public interface Interceptor<E> {
    boolean intercept(E e);

    void add(E e);

    void remove(E e);
}
