package org.activiti.distributed.interceptor;

import org.activiti.engine.impl.persistence.entity.ExecutionEntity;

/**
 * this is a hook,when you execute SpecificSignalCmd and you are intercepted,this hook method would be executed.
 * Created by zoupeng on 15/9/30.
 */
public interface InterceptedCallback {
    void callback(ExecutionEntity execution);
}
