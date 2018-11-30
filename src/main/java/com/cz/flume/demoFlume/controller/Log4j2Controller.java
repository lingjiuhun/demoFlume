package com.cz.flume.demoFlume.controller;

import com.alibaba.fastjson.JSON;
import com.cz.flume.demoFlume.entity.UserInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/logconfig")
@Api(value = "日志配置管理 相关api controller", description = "日志配置服务相关API")
public class Log4j2Controller {


    private Logger log = LogManager.getFormatterLogger(Log4j2Controller.class.getName());

    @RequestMapping(value = "/log4j2", method = RequestMethod.GET)
    @ApiOperation(value = "日志级别测试", notes = "日志级别测试")
    public String logback(){
        log.error("我是error");
        log.warn("我是warn");
        log.info("我是info");
        log.debug("我是debug");
        return "success";
    }

    /**
     * LOG4j2动态修改包名的日志级别
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "LOG4j2动态修改包名的日志级别", notes = "LOG4j2动态修改包名的日志级别")
    @RequestMapping(value = "/levelb", method = RequestMethod.GET)
    public String updateLogbackLevelj(@RequestParam(value="level") String level) throws Exception {
        String msg = "success";
        level = level.toLowerCase();
/* 日志级别动态设置

        org.apache.logging.log4j.core.LoggerContext ctx = (org.apache.logging.log4j.core.LoggerContext) LogManager.getContext(false);
        Configuration config = ctx.getConfiguration();
        LoggerConfig loggerConfig = config.getLoggerConfig(LogManager.ROOT_LOGGER_NAME);
        switch (level) {
            case "trace":
                loggerConfig.setLevel(org.apache.logging.log4j.Level.TRACE);
                break;
            case "debug":
                loggerConfig.setLevel(org.apache.logging.log4j.Level.DEBUG);
                break;
            case "info":
                loggerConfig.setLevel(org.apache.logging.log4j.Level.INFO);
                break;
            case "warn":
                loggerConfig.setLevel(org.apache.logging.log4j.Level.WARN);
                break;
            case "error":
                loggerConfig.setLevel(org.apache.logging.log4j.Level.ERROR);
                break;
            default:
                msg = "日志级别修改失败！";
                break;
        }
        ctx.updateLoggers();
*/

        Collection<org.apache.logging.log4j.core.Logger> current = LoggerContext.getContext(false).getLoggers();
        Collection<org.apache.logging.log4j.core.Logger> notcurrent = LoggerContext.getContext().getLoggers();
        Collection<org.apache.logging.log4j.core.Logger> allConfig = current;
        allConfig.addAll(notcurrent);
        for (org.apache.logging.log4j.core.Logger log:allConfig){
            switch (level) {
                case "trace":
                    log.setLevel(Level.TRACE);
                    break;
                case "debug":
                    log.setLevel(Level.DEBUG);
                    break;
                case "info":
                    log.setLevel(Level.INFO);
                    break;
                case "warn":
                    log.setLevel(Level.WARN);
                    break;
                case "error":
                    log.setLevel(Level.ERROR);
                    break;
                default:
                    msg = "日志级别修改失败！";
                    break;
            }
        }
        return msg;
    }


    @RequestMapping(value = "/users", method = RequestMethod.POST)
    @ApiOperation(value = "post方式添加用户，参数打印测试", notes = "post方式添加用户，参数打印测试")
    public String addUser(@RequestBody UserInfo userInfo,@RequestParam(value="token", required=false) String token){

        return JSON.toJSONString(userInfo);
    }

}
