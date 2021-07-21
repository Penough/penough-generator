package org.penough.mp.generator.config;

import lombok.*;
import lombok.experimental.Accessors;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
@Builder
public class PePackageConfig {
    String parentPackage = "";
    String module = "";
    String entityPackage = "entity";
    String servicePackage = "service";
    String serviceImplPackage = "service.impl";
    String mapperPackage = "mapper";
    String xmlPackage = "xml";
    String controllerPackage = "controller";
}
