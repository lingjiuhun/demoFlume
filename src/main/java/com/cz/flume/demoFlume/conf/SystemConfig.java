package com.cz.flume.demoFlume.conf;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 *
 */

@Configuration
public class SystemConfig {

    @Value("${run.mode}")
    private String runMode;

    public String getRunMode(){
        return this.runMode;
    }
}
