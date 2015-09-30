package org.activiti.distributed.cmd;

import org.activiti.distributed.interceptor.InterceptedCallback;
import org.activiti.distributed.interceptor.InterceptorChain;
import org.activiti.engine.impl.cmd.NeedsActiveExecutionCmd;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;

import java.util.Map;

/**
 * Created by zoupeng on 15/8/11.
 */
public class SpecificSignalCmd extends NeedsActiveExecutionCmd<Object> {
    private static final long serialVersionUID = 1L;
    private InterceptorChain interceptorChain = InterceptorChain.getInstance();
    protected String signalName;
    protected Object signalData;
    protected final Map<String, Object> processVariables;
    protected final InterceptedCallback callback;

    public SpecificSignalCmd(String executionId, String signalName, Object signalData, Map<String, Object> processVariables,InterceptedCallback callback) {
        super(executionId);
        this.signalName = signalName;
        this.signalData = signalData;
        this.processVariables = processVariables;
        this.callback = callback;
    }

    protected Object execute(CommandContext commandContext, ExecutionEntity execution) {
        if(processVariables != null) {
            execution.setVariables(processVariables);
        }
        try {
            if(interceptorChain.intercept(execution)){
                //TODO:when you call interceptorChain.intercept and you get result is true,you can do whatever you want
                callback.callback(execution);
            }
            else{
                execution.signal(signalName, signalData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected String getSuspendedExceptionMessage() {
        return "Cannot signal an execution that is suspended";
    }
}
