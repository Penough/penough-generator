package org.penough.mp.generator.config;

import lombok.*;
import lombok.experimental.Accessors;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
@Builder
public class PeGlobalConfig {
    String projectRootPath = System.getProperty("user.dir");
    String databaseUrl = "";
    String userName = "root";
    String pwd = "123456";

    public PeGlobalConfig(String databaseUrl, String userName, String pwd) {
        this();
        this.databaseUrl = databaseUrl;
        this.userName = userName;
        this.pwd = pwd;
    }
}
