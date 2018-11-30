package com.cz.flume.demoFlume.fiter;

import com.cz.flume.demoFlume.conf.RequestWrapper;
import com.cz.flume.demoFlume.conf.SystemConfig;
import com.cz.flume.demoFlume.tools.AjaxResult;
import com.cz.flume.demoFlume.tools.StrKit;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.cors.CorsUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 类描述: Token过滤器
 *
 * @author Haibo Qing
 * @version V1.0
 * @date 2018-04-25 19:15
 **/
@WebFilter(filterName = "tokenFilter", urlPatterns = "/*", initParams = {
        @WebInitParam(name = "ignores", value = "/,/user/login,/user/logout,/models/**,/dictionary,/customer-settings,/assets,/swagger-ui.html,/v2/api-docs,/webjars/**,/swagger-resources/**,/**/*.*,/h2-console/**,/saleSellInformation/validateEquitment") /**/
})
@Log4j2
public class TokenFilter implements Filter {

    @Autowired
    SystemConfig systemConfig;

    private Set<String> prefixIgnores = new HashSet<String>();

    @Override
    public void init(FilterConfig config) throws ServletException {
        String cp = config.getServletContext().getContextPath();
        String ignoresParam = config.getInitParameter("ignores");
        String[] ignoreArray = ignoresParam.split(",");
        for (String s : ignoreArray) {
            prefixIgnores.add(cp + s);
        }
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        //请求参数获取，request转换,cz 20181123
        ServletRequest requestWrapper = null;
        if (request instanceof HttpServletRequest) {
            requestWrapper = new RequestWrapper(request);
        }
        if (requestWrapper != null) {
            request = (HttpServletRequest) requestWrapper;
        }


        //demo 模式下不进行token 校验
        if (systemConfig.getRunMode().equalsIgnoreCase("demo")) {
            filterChain.doFilter(request, response);
            return;
        }
        if (canIgnore(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        if (RequestMethod.OPTIONS.toString().equals(request.getMethod())) {
            String allowMethod = request.getHeader("Access-Control-Request-Method");
            String allowHeaders = request.getHeader("Access-Control-Request-Headers");
            response.setHeader("Access-Control-Max-Age", "86400");
            response.setHeader("Access-Control-Allow-Methods", allowMethod);
            response.setHeader("Access-Control-Allow-Headers", allowHeaders);
            response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
            response.setHeader("Access-Control-Allow-Credentials", "true");
            return;
        }
        if (CorsUtils.isCorsRequest(request)) {
            response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
            response.setHeader("Access-Control-Allow-Credentials", "true");
        }

        String token = request.getParameter("token");
//        if (StrKit.isNotEmpty(token)) {
//            if (!TokenKit.isValidateToken(token)) {
//                responseFail(response, token);
//                return;
//            }
//        } else {
//            responseFail(response, token);
//            return;
//        }
        refreshTokenLastTime(token);
        filterChain.doFilter(request, response);
    }

    private boolean canIgnore(HttpServletRequest request) {
        String url = request.getRequestURI();
        AntPathMatcher matcher = new AntPathMatcher();
        for (String ignore : prefixIgnores) {
            if (matcher.match(ignore, url)) {
                return true;
            }
        }
        return false;
    }

    private void responseFail(HttpServletResponse response, String token) throws IOException, ServletException {
//        response.setStatus(HttpStatus.UNAUTHORIZED.value());
//        response.setContentType("application/json");
//        OutputStream stream = response.getOutputStream();
//        String result = new AjaxResult(false, "").setData(StrKit.format("非法token {},请重新登录", token)).toString();
//        stream.write(result.getBytes());
//        stream.close();
    }

    /**
     * 更新 token Id 最后一次访问时间
     */
    private void refreshTokenLastTime(final String token) {
//        Map<String, UserCacheInfo> tokenInfo = Config.SYSTEM_USER_CACHE;
//        Predicate<String> predicate = (tk) -> (token.equals(tk));
//        List<String> matchedToken = tokenInfo.keySet().stream().filter(predicate).collect(Collectors.toList());
//        matchedToken.forEach(mtk -> {
//            UserCacheInfo uci = tokenInfo.get(mtk);
//            //更新token last time
//            uci.setCreateTime(System.currentTimeMillis());
//        });
    }

    @Override
    public void destroy() {

    }
}
