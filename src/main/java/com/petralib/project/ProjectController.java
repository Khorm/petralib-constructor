package com.petralib.project;

import com.petralib.auth.UserAction;
import com.petralib.auth.security.model.LoginModel;
import com.petralib.auth.security.model.SecurityUser;
import com.petralib.project.service.ProjectService;
import com.petralib.workflow.role_check.ProjectGrant;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.nio.file.AccessDeniedException;

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
    public String getConstructor(@PathVariable Long projectId)  {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
//        boolean isReadPermitted = projectService.isUserAcceptTo(securityUser.getId(), projectId, UserAction.READ);
//        if (!isReadPermitted){
//            throw new AccessDeniedException("Access denied");
//        }
        return "constructor";
    }
}
