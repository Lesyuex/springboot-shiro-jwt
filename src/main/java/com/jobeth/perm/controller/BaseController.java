package com.jobeth.perm.controller;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Desc
 *
 * @author Jobeth
 * @since 2020/7/7 9:54
 */
@RestController
public abstract class BaseController {
    protected HttpServletRequest httpServletRequest;
    protected HttpServletResponse httpServletResponse;

    @ModelAttribute
    public void setReqAndRes(HttpServletRequest request, HttpServletResponse response) {
        this.httpServletRequest = request;
        this.httpServletResponse = response;
    }
}
