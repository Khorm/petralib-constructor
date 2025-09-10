package com.petralib.project;

import com.petralib.auth.UserAction;
import com.petralib.block.enums.BlockType;
import com.petralib.project.service.ProjectService;
import com.petralib.service.role_check.ProjectGrant;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/projects")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ProjectController {

    ProjectService projectService;

    @GetMapping
    public String getProjects(){
        return "projects";
    }

    @ProjectGrant(userAction = UserAction.READ)
    @GetMapping("{projectId}/constructor")
    public String getConstructor(@PathVariable Long projectId, Model model)  {
        model.addAttribute("projectId", projectId);
        return "constructor";
    }

    @ProjectGrant(userAction = UserAction.WRITE)
    @GetMapping("{projectId}/constructor/block/workflow")
    public String getWorkflowCreate(@PathVariable Long projectId, Model model)  {
        model.addAttribute("projectId", projectId);
        model.addAttribute("blockId", null);
        model.addAttribute("blockType", BlockType.WORKFLOW.name());
        return "block";
    }

    @ProjectGrant(userAction = UserAction.WRITE)
    @GetMapping("{projectId}/constructor/block/workflow/{workflowId}")
    public String getWorkflowEdit(@PathVariable Long projectId, @PathVariable Long workflowId, Model model)  {
        model.addAttribute("projectId", projectId);
        model.addAttribute("blockId", workflowId);
        model.addAttribute("blockType", BlockType.WORKFLOW.name());
        return "block";
    }

    @ProjectGrant(userAction = UserAction.WRITE)
    @GetMapping("{projectId}/constructor/block/action")
    public String getActionCreate(@PathVariable Long projectId, Model model)  {
        model.addAttribute("projectId", projectId);
        model.addAttribute("blockId", null);
        model.addAttribute("blockType", BlockType.ACTION.name());
        return "block";
    }

    @ProjectGrant(userAction = UserAction.WRITE)
    @GetMapping("{projectId}/constructor/block/action/{actionId}")
    public String getActionEdit(@PathVariable Long projectId, @PathVariable Long actionId, Model model)  {
        model.addAttribute("projectId", projectId);
        model.addAttribute("blockId", actionId);
        model.addAttribute("blockType", BlockType.ACTION.name());

        return "block";
    }


    @ProjectGrant(userAction = UserAction.WRITE)
    @GetMapping("{projectId}/constructor/service")
    public String getServiceCreate(@PathVariable Long projectId, Model model)  {
        model.addAttribute("projectId", projectId);
        model.addAttribute("serviceId", null);
        return "service";
    }

    @ProjectGrant(userAction = UserAction.WRITE)
    @GetMapping("{projectId}/constructor/service/{serviceId}")
    public String getServiceEdit(@PathVariable Long projectId, @PathVariable Long serviceId, Model model)  {
        model.addAttribute("projectId", projectId);
        model.addAttribute("serviceId", serviceId);
        return "service";
    }
}
