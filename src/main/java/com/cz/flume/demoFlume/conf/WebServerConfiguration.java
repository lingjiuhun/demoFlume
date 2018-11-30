package com.cz.flume.demoFlume.conf;

import org.apache.catalina.connector.Connector;
import org.apache.coyote.http11.Http11NioProtocol;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * @Author: caozhen
 * @Description: tomcat 最大连接数据 和最大线程数配置
 * @date: 2018/11/2 14:39
 */
@Configuration
//如果synchronize在配置文件中并且值为true
@ConditionalOnProperty(name = "serverconfig", havingValue = "true")
public class WebServerConfiguration {
    @Bean
    public ConfigurableServletWebServerFactory createEmbeddedServletContainerFactory() {
        TomcatServletWebServerFactory tomcatFactory = new TomcatServletWebServerFactory();
        tomcatFactory.addConnectorCustomizers(new MyTomcatConnectorCustomizer());
        return tomcatFactory;
    }
    @Value("${tomcat.max-connections}")
    private Integer maxConnections;
    @Value("${tomcat.max-threads}")
    private Integer maxThreads;
    @Value("${tomcat.time-out}")
    private Integer timeout;

    class MyTomcatConnectorCustomizer implements TomcatConnectorCustomizer {
        @Override
        public void customize(Connector connector) {
            Http11NioProtocol protocol = (Http11NioProtocol) connector.getProtocolHandler();
            //设置最大连接数
            protocol.setMaxConnections(maxConnections);
            //设置最大线程数
            protocol.setMaxThreads(maxThreads);
            protocol.setConnectionTimeout(timeout);
        }
    }
}


