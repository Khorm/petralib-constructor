package com.petralib.workflow.rest;

import com.petralib.auth.UserAction;
import com.petralib.workflow.dto.WorkflowDto;
import com.petralib.workflow.role_check.ProjectGrant;
import com.petralib.workflow.service.WorkflowService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.parser.Entity;
import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/workflow")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class WorkflowRestController {

    WorkflowService workflowService;

//    @ProjectGrant(userAction = UserAction.READ)
//    @GetMapping
//    public ResponseEntity<?> getWorkflowPage(@RequestParam Long projectId, @RequestParam Integer pageNumber,
//                                             @RequestParam String workflowName, @RequestParam Integer pageElementsCount) {
//        WorkflowPage page;
//        if (workflowName == null || workflowName.isBlank()) {
//            page = workflowService.getWorkflowsByProject(pageElementsCount,
//                    pageNumber,
//                    projectId);
//        }else {
//            page = workflowService.getWorkflowsByProjectAndName(pageElementsCount,
//                    pageNumber,
//                    projectId,
//                    workflowName);
//        }
//        return ResponseEntity.ok(page);
//    }
//
//    @ProjectGrant(userAction = UserAction.READ)
//    @GetMapping("high-level")
//    public ResponseEntity<?> getTopWorkflowPage(@RequestParam Long projectId, @RequestParam Integer pageNumber, @RequestParam Integer pageElementsCount) {
//        System.out.println("getTopWorkflowPage");
//        WorkflowPage page =  workflowService.getHighLevelWorkflowsByProject(pageElementsCount,
//                pageNumber,
//                projectId);
//        return ResponseEntity.ok(page);
//    }
//
//    @ProjectGrant(userAction = UserAction.READ)
//    @GetMapping("children")
//    public Collection<WorkflowCollectionObjectDto> getChildrenWorkflows(@RequestParam Long projectId, @RequestParam Long parentId){
//        return workflowService.getChildrenWorkflows(parentId);
//    }

    @ProjectGrant(userAction = UserAction.WRITE)
    @PostMapping
    public ResponseEntity<?> save(@RequestParam Long projectId, @Valid @RequestBody WorkflowDto workflowDto, Errors errors) {
        if (errors.hasErrors()) {
            Collection<String> validationErrors = errors.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage).toList();
            return new ResponseEntity<>(validationErrors, HttpStatus.BAD_REQUEST);
        }
        workflowService.editWorkflow(workflowDto, projectId);
        return ResponseEntity.ok("ok");
    }

    @ProjectGrant(userAction = UserAction.WRITE)
    @GetMapping("{workflowId}")
    public WorkflowDto getWorkflow(@RequestParam Long projectId,@PathVariable Long workflowId){
        return workflowService.getWorkflowWithVariables(workflowId);
    }

    @DeleteMapping("{workflowId}")
    public ResponseEntity<?> delete(@RequestParam Long projectId,@PathVariable Long workflowId){
        workflowService.deleteWorkflow(workflowId);
        return ResponseEntity.ok(workflowId);
    }

}
