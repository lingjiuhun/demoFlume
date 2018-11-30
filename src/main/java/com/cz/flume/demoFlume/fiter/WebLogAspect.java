package com.cz.flume.demoFlume.fiter;


import com.alibaba.fastjson.JSON;
import com.cz.flume.demoFlume.conf.RequestWrapper;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Description:
 * <p>
 * 全局的的请求拦截器（拦截所有的控制器）
 * 带有@RequestMapping注解的方法上都会拦截
 * <p>
 * Author: liu
 * Created: 2018/4/3 10:30
 */

@Aspect
@Component
public class WebLogAspect {

    private Logger logger = LoggerFactory.getLogger(this.getClass());


    @Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping)")
    public void webLog() {

    }


    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint) {
        //获取请求
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        long startTime = System.currentTimeMillis();
        request.setAttribute("requestMyStartTime", startTime + "");

        //打印请求信息
        logger.info("ServerName:" + request.getServerName() + "/IP:" + request.getRemoteAddr() + "-->" + request.getMethod() + " " + request.getRequestURL().toString());
        //打印请求参数
        Enumeration enu = request.getParameterNames();
        Map<String, Object> urlParam = new HashMap<>();
        while (enu.hasMoreElements()) {
            String paraName = (String) enu.nextElement();
            urlParam.put(paraName, request.getParameter(paraName));
        }
        logger.info("urlParam: " + JSON.toJSONString(urlParam));
        if (request instanceof RequestWrapper) {
            logger.info("bodyParam: " + ((RequestWrapper) request).getBody());
        } else {
            logger.info("queryString: " + request.getQueryString());
        }

    }

    @After("webLog()")
    public void doAfter() {

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        long startTime = Long.parseLong(request.getAttribute("requestMyStartTime").toString());
        long endTime = System.currentTimeMillis();

        long executeTime = endTime - startTime;

        // log it
        logger.info("[" + request.getRequestURI() + "] 执行耗时 : "
                + executeTime + "ms");
    }
}
