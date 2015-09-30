package org.activiti.distributed.interceptor;

import org.activiti.engine.impl.persistence.entity.ExecutionEntity;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by zoupeng on 15/9/30.
 */
public class InterceptorChain {
    private static List<Interceptor> interceptors = new LinkedList<>();

    private static final InterceptorChain interceptorChain = new InterceptorChain();

    private InterceptorChain(){

    }

    public static InterceptorChain getInstance(){
        return interceptorChain;
    }

    public boolean intercept(ExecutionEntity execution){
        //TODO:here you can put the business code into this method,
        return false;
    }

    public <E> boolean intercept(E e){
        boolean flag = false;
        for(Interceptor interceptor : interceptors){
            flag = interceptor.intercept(e);
            if(flag){
                break;
            }
        }
        return flag;
    }

    public <E extends Interceptor> void addInterceptor(E interceptor){
        interceptors.add(interceptor);
    }

    public <E extends Interceptor> void remove(E interceptor){
        interceptors.remove(interceptor);
    }

}
