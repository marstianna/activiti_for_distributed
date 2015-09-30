package org.activiti.distributed.utils;

import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.RuntimeServiceImpl;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.runtime.Execution;

import java.util.List;

/**
 * Created by zoupeng on 15/9/28.
 */
public final class ActivitiUtils {

    /**
     * execute the command
     * @param runtimeService
     * @param cmd
     * @param <T>
     * @return
     */
    public static <T> T execute(RuntimeServiceImpl runtimeService,Command<T> cmd){
        return runtimeService.getCommandExecutor().execute(cmd);
    }

    /**
     * get ProcessDefinitionEntity by procoss definition id
     * @param repositoryService
     * @param processDefinitionId
     * @return
     */
    public static ProcessDefinitionEntity getProcessDefinitionById(RepositoryServiceImpl repositoryService,String processDefinitionId){
        return (ProcessDefinitionEntity)repositoryService.getDeployedProcessDefinition(processDefinitionId);
    }

    /**
     * get Execution by businessKey which you put into process when you started
     * @param runtimeService
     * @param businessKey
     * @return
     */
    public static Execution getExecutionByBusinessKey(RuntimeServiceImpl runtimeService,String businessKey){
        return runtimeService.createExecutionQuery().processInstanceBusinessKey(businessKey).singleResult();
    }

    /**
     * get a ActivityImpl by activityId in processDefinition which id is processDefinitionId
     * @param repositoryService
     * @param activityId
     * @param processDefinitionId
     * @return
     */
    public static ActivityImpl getActivity(RepositoryServiceImpl repositoryService,String activityId,String processDefinitionId){
        ProcessDefinitionEntity pde = getProcessDefinitionById(repositoryService, processDefinitionId);
        return (ActivityImpl) pde.findActivity(activityId);
    }

    /**
     * get all ActivityImpl in processDefinition which id is processDefinitionId
     * @param repositoryService
     * @param processDefinitionId
     * @return
     */
    public static List<ActivityImpl> getActivities(RepositoryServiceImpl repositoryService,String processDefinitionId){
        ProcessDefinitionEntity pde = getProcessDefinitionById(repositoryService,processDefinitionId);
        return pde.getActivities();
    }
}
