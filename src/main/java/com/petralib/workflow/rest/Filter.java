package com.petralib.workflow.rest;

import com.petralib.project.service.ProjectService;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.io.IOException;


@WebFilter
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Deprecated
public class Filter implements jakarta.servlet.Filter {

    ProjectService projectService;

    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("FILTER!!!!!!");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

    }
}
