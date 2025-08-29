package com.petralib.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.UrlPathHelper;

public class SecurityRedirectInterceptor implements HandlerInterceptor {
    //    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//        HttpServletRequest servletRequest = (HttpServletRequest) request;
//        HttpServletResponse servletResponse = (HttpServletResponse) response;
//
//        if (!isAuthenticated() && !"/login".equals(servletRequest.getRequestURI()) && !"/".equals(servletRequest.getRequestURI())) {
//            String encodedRedirectURL = ((HttpServletResponse) response).encodeRedirectURL(
//                    servletRequest.getContextPath() + "/login");
//            servletResponse.setStatus(HttpStatus.TEMPORARY_REDIRECT.value());
//            servletResponse.setHeader("Location", encodedRedirectURL);
//        }
//
//        System.out.println("URI " + servletRequest.getContextPath());
//        System.out.println("EQ " + (!isAuthenticated() && !"/login".equals(servletRequest.getRequestURI()) && !"/".equals(servletRequest.getRequestURI())));
//        if (isAuthenticated() && ("/login".equals(servletRequest.getRequestURI()) || "/".equals(servletRequest.getRequestURI()))) {
//            System.out.println("IN ");
//            String encodedRedirectURL = ((HttpServletResponse) response).encodeRedirectURL(
//                    servletRequest.getContextPath() + "/projects");
//            servletResponse.setStatus(HttpStatus.TEMPORARY_REDIRECT.value());
//            servletResponse.setHeader("Location", encodedRedirectURL);
//        }
//
//        chain.doFilter(servletRequest, servletResponse);
//    }
    private final UrlPathHelper urlPathHelper = new UrlPathHelper();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
//        if ("/loginUser".equals(urlPathHelper.getLookupPathForRequest(request)) && isAuthenticated()) {
//            String encodedRedirectURL = response.encodeRedirectURL(
//                    request.getContextPath() + "/userMainPage");
//            response.setStatus(HttpStatus.TEMPORARY_REDIRECT.value());
//            response.setHeader("Location", encodedRedirectURL);
//
//            return false;
//        } else {
//            return true;
//        }

        if (!isAuthenticated() && !"/login".equals(urlPathHelper.getLookupPathForRequest(request))
                && !"/".equals(urlPathHelper.getLookupPathForRequest(request))) {
            String encodedRedirectURL = response.encodeRedirectURL(
                    request.getContextPath() + "/login");
            response.setStatus(HttpStatus.TEMPORARY_REDIRECT.value());
            response.setHeader("Location", encodedRedirectURL);
            return false;
        } else if (isAuthenticated()
                && ("/login".equals(urlPathHelper.getLookupPathForRequest(request)) || "/".equals(urlPathHelper.getLookupPathForRequest(request)))) {
            String encodedRedirectURL = response.encodeRedirectURL(
                    request.getContextPath() + "/projects");
            response.setStatus(HttpStatus.TEMPORARY_REDIRECT.value());
            response.setHeader("Location", encodedRedirectURL);
            return false;
        }

        return true;
    }

    private boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || AnonymousAuthenticationToken.class.
                isAssignableFrom(authentication.getClass())) {
            return false;
        }
        return authentication.isAuthenticated();
    }
}
