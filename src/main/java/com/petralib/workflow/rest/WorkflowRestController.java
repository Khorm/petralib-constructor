package com.petralib.workflow.rest;

import com.petralib.auth.UserAction;
import com.petralib.workflow.service.WorkflowService;
import com.petralib.workflow.dto.WorkflowCollectionObjectDto;
import com.petralib.workflow.dto.WorkflowPage;
import com.petralib.workflow.role_check.ProjectGrant;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/api/v1/workflow")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class WorkflowRestController {

    WorkflowService workflowService;

    @ProjectGrant(userAction = UserAction.READ)
    @GetMapping
    public ResponseEntity<?> getWorkflowPage(@RequestParam Long projectId, @RequestParam Integer pageNumber,
                                             @RequestParam String workflowName, @RequestParam Integer pageElementsCount) {
        WorkflowPage page;
        if (workflowName == null || workflowName.isBlank()) {
            page = workflowService.getWorkflowsByProject(pageElementsCount,
                    pageNumber,
                    projectId);
        }else {
            page = workflowService.getWorkflowsByProjectAndName(pageElementsCount,
                    pageNumber,
                    projectId,
                    workflowName);
        }
        return ResponseEntity.ok(page);
    }

    @ProjectGrant(userAction = UserAction.READ)
    @GetMapping("high-level")
    public ResponseEntity<?> getTopWorkflowPage(@RequestParam Long projectId, @RequestParam Integer pageNumber, @RequestParam Integer pageElementsCount) {
        System.out.println("getTopWorkflowPage");
        WorkflowPage page =  workflowService.getHighLevelWorkflowsByProject(pageElementsCount,
                pageNumber,
                projectId);
        return ResponseEntity.ok(page);
    }

    @ProjectGrant(userAction = UserAction.READ)
    @GetMapping("children")
    public Collection<WorkflowCollectionObjectDto> getChildrenWorkflows(@RequestParam Long projectId, @RequestParam Long parentId){
        return workflowService.getChildrenWorkflows(parentId);
    }

//    public ConstructorWorkflowChildrenDto getWorkdfowChilds
}
