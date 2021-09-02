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
    @Builder.Default
    String parentPackage = "";
    @Builder.Default
    String module = "";
    @Builder.Default
    String entityPackage = "entity";
    @Builder.Default
    String servicePackage = "service";
    @Builder.Default
    String serviceImplPackage = "service.impl";
    @Builder.Default
    String mapperPackage = "mapper";
    @Builder.Default
    String xmlPackage = "xml";
    @Builder.Default
    String controllerPackage = "controller";
}
