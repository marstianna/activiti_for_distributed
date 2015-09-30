package org.activiti.distributed.flowcontrol;

import org.activiti.distributed.cmd.StartProcessAtSpecificAcitivityCmd;
import org.activiti.distributed.utils.ActivitiUtils;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.RuntimeServiceImpl;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.delegate.ActivityExecution;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.process.TransitionImpl;
import org.activiti.engine.runtime.ProcessInstance;

import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by zoupeng on 15/9/28.
 */
public class FlowControlService{

    private ProcessEngine processEngine;

    public FlowControlService(ProcessEngine processEngine){
        this.processEngine = processEngine;
    }

    public ProcessInstance startProcessInstanceByKeyAtSpecificActivityId(String processDefinitionKey, String activityId){
        return startProcessInstanceByKeyAtSpecificActivityId(processDefinitionKey,null,null,activityId);
    }

    public ProcessInstance startProcessInstanceByKeyAtSpecificActivityId(String processDefinitionKey, Map<String, Object> params, String activityId){
        return startProcessInstanceByKeyAtSpecificActivityId(processDefinitionKey,null,params,activityId);
    }

    public ProcessInstance startProcessInstanceByKeyAtSpecificActivityId(String processDefinitionKey, String businessKey, String activityId){
        return startProcessInstanceByKeyAtSpecificActivityId( processDefinitionKey, businessKey, null, activityId);
    }

    public ProcessInstance startProcessInstanceByKeyAtSpecificActivityId(String processDefinitionKey, String businessKey, Map<String, Object> params, String activityId){
        checkNotNull(processDefinitionKey, "input processDefinitionKey can not be null");

        return ActivitiUtils.execute((RuntimeServiceImpl)processEngine.getRuntimeService(),new StartProcessAtSpecificAcitivityCmd(processDefinitionKey, null, businessKey, activityId, params));
    }

    public void moveTo(String targetActivityId, ActivityExecution execution) throws Exception {
        moveTo(targetActivityId, execution, null);
    }

    public void moveTo(String targetActivityId, DelegateExecution execution) throws Exception {
        moveTo(targetActivityId, (ActivityExecution) execution);
    }

    public void moveTo(String targetActivityId, ActivityExecution execution, Map<String, Object> variables) throws Exception {
        checkArgument(!targetActivityId.equals(execution.getCurrentActivityId()),"target activity is not allowed to be the current one");

        ProcessDefinitionEntity processDefinitionEntity = ActivitiUtils.getProcessDefinitionById((RepositoryServiceImpl)processEngine.getRepositoryService(),execution.getProcessDefinitionId());
        ActivityImpl activity = processDefinitionEntity.findActivity(execution.getCurrentActivityId());
        TransitionImpl transition = activity.createOutgoingTransition();
        ActivityImpl target = processDefinitionEntity.findActivity(targetActivityId);
        transition.setDestination(target);
        execution.setVariables(variables);
        execution.take(transition);
    }

    public void moveTo(String targetActivityId, DelegateExecution execution, Map<String, Object> variables) throws Exception {
        moveTo(targetActivityId, (ActivityExecution) execution, variables);
    }

}
