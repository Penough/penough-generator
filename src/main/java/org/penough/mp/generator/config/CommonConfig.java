package org.penough.mp.generator.config;

import lombok.*;

/**
 * 常规配置
 */

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CommonConfig {
    PeGlobalConfig globalConfig = new PeGlobalConfig();
    PePackageConfig packageConfig = new PePackageConfig();
    PeStrategyConfig stratrgyConfig = new PeStrategyConfig();
    PeTplConfig tplConfig = new PeTplConfig();

    public CommonConfig(String databaseUrl, String userName, String pwd){
        globalConfig = new PeGlobalConfig(databaseUrl, userName, pwd);
        packageConfig = new PePackageConfig();
        stratrgyConfig = new PeStrategyConfig();
        tplConfig = new PeTplConfig();
    }
}
