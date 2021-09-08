package org.penough.mp.generator.config;

import com.baomidou.mybatisplus.generator.config.rules.DateType;
import lombok.*;
import lombok.experimental.Accessors;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
@Builder
public class PeGlobalConfig {
    @Builder.Default
    String projectRootPath = System.getProperty("user.dir");
    @Builder.Default
    String servicePrefix = "penough-";
    @Builder.Default
    String serviceName = "";
    @Builder.Default
    String databaseUrl = "";
    @Builder.Default
    String userName = "root";
    @Builder.Default
    String pwd = "123456";
    @Builder.Default
    String author = "author";
    @Builder.Default
    String commentDate = "yyyy-MM-dd";
    @Builder.Default
    DateType dateType = DateType.TIME_PACK;
    @Builder.Default
    Boolean kotlin = false;
    @Builder.Default
    Boolean fileOverride = false;
    @Builder.Default
    Boolean openDir = true;
    @Builder.Default
    Boolean swagger = true;

    public PeGlobalConfig(String databaseUrl, String userName, String pwd) {
        this();
        this.databaseUrl = databaseUrl;
        this.userName = userName;
        this.pwd = pwd;
    }
}
