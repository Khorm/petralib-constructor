package com.petralib.workflow.role_check;

import com.petralib.auth.security.model.SecurityUser;
import com.petralib.project.service.ProjectService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ProjectGrantAspect {

//    @Pointcut("annotation(ProjectGrant)")
//    public void grantPointcut() {
//    }
    private final ProjectService projectService;

    public ProjectGrantAspect(ProjectService projectService) {
//        System.out.println("ProjectService " + projectService);
        this.projectService = projectService;
    }

    @Around(value = "@annotation(projectGrant)")
    public Object callAdvice(ProceedingJoinPoint joinPoint, ProjectGrant projectGrant) throws Throwable {
        Long projectId = (Long) joinPoint.getArgs()[0];
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
        boolean isReadPermitted = projectService.isUserAcceptTo(securityUser.getId(), projectId, projectGrant.userAction());
        if (!isReadPermitted){
            return ResponseEntity.status(HttpStatus.FORBIDDEN.value()).build();
        }else {
            return joinPoint.proceed();
        }
    }
}
