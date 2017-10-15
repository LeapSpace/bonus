package com.huilianjk.bonus.handler;

import com.huilianjk.bonus.handler.exceptions.DefException;
import com.huilianjk.bonus.util.common.ErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by space on 2017/10/15.
 */
@ControllerAdvice
public class DefExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(DefExceptionHandler.class);
    public static final String DEFAULT_ERROR_VIEW = "error";

    @ExceptionHandler(DefException.class)
    public ModelAndView defErrorHandler(HttpServletRequest req, DefException e) throws Exception {
        logger.debug("error: ",e);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("exception", e);
        modelAndView.addObject("url", req.getRequestURL());
        modelAndView.setViewName(DEFAULT_ERROR_VIEW);
        return modelAndView;
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView runtimeErrorHandler(HttpServletRequest req, Exception e) throws Exception {
        logger.debug("error: ",e);
        ModelAndView modelAndView = new ModelAndView();
        DefException defException = new DefException(ErrorCode.ERROR);
        modelAndView.addObject("exception", defException);
        logger.warn("error: " + req.getRequestURI());
        modelAndView.addObject("url", req.getRequestURL());
        modelAndView.setViewName(DEFAULT_ERROR_VIEW);
        return modelAndView;
    }
}
