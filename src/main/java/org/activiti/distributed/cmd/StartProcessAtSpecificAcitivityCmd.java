package org.activiti.distributed.cmd;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.ActivitiIllegalArgumentException;
import org.activiti.engine.ActivitiObjectNotFoundException;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.deploy.DeploymentManager;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;

import java.io.Serializable;
import java.util.Map;

/**
 * start process at the specific activity
 * Created by zoupeng on 15/7/29.
 */
public class StartProcessAtSpecificAcitivityCmd implements Command<ProcessInstance>,Serializable{
    protected String processDefinitionKey;
    private String processDefinitionId;
    private String businessKey;
    private String activityId;
    protected String tenantId;
    private Map<String,Object> variables;

    public StartProcessAtSpecificAcitivityCmd(String processDefinitionKey, String processDefinitionId, String businessKey, String activityId, Map<String, Object> variables) {
        this.processDefinitionId = processDefinitionId;
        this.businessKey = businessKey;
        this.activityId = activityId;
        this.variables = variables;
        this.processDefinitionKey = processDefinitionKey;
    }

    @Override
    public ProcessInstance execute(CommandContext commandContext) {
        DeploymentManager deploymentCache = commandContext
                .getProcessEngineConfiguration()
                .getDeploymentManager();
        ProcessDefinitionEntity processDefinition = null;
        if (processDefinitionId!=null) {
            processDefinition = deploymentCache.findDeployedProcessDefinitionById(processDefinitionId);
            if (processDefinition == null) {
                throw new ActivitiObjectNotFoundException("No process definition found for id = '" + processDefinitionId + "'", ProcessDefinition.class);
            }
        } else if (processDefinitionKey != null && (tenantId == null || ProcessEngineConfiguration.NO_TENANT_ID.equals(tenantId))){
            processDefinition = deploymentCache.findDeployedLatestProcessDefinitionByKey(processDefinitionKey);
            if (processDefinition == null) {
                throw new ActivitiObjectNotFoundException("No process definition found for key '" + processDefinitionKey +"'", ProcessDefinition.class);
            }
        } else if (processDefinitionKey != null && tenantId != null && !ProcessEngineConfiguration.NO_TENANT_ID.equals(tenantId)) {
            processDefinition = deploymentCache.findDeployedLatestProcessDefinitionByKeyAndTenantId(processDefinitionKey, tenantId);
            if (processDefinition == null) {
                throw new ActivitiObjectNotFoundException("No process definition found for key '" + processDefinitionKey +"' for tenant identifier " + tenantId, ProcessDefinition.class);
            }
        } else {
            throw new ActivitiIllegalArgumentException("processDefinitionKey and processDefinitionId are null");
        }

        if (processDefinition.isSuspended()) {
            throw new ActivitiException("Cannot start process instance. Process definition "
                    + processDefinition.getName() + " (id = " + processDefinition.getId() + ") is suspended");
        }

        ActivityImpl activity = processDefinition.findActivity(activityId);
        ExecutionEntity processInstance = processDefinition.createProcessInstance(businessKey,activity);

        if (variables != null) {
            processInstance.setVariables(variables);
        }
        processInstance.start();

        return processInstance;
    }
}
