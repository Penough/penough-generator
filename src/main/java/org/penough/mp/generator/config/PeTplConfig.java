package org.penough.mp.generator.config;

import lombok.*;
import lombok.experimental.Accessors;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
@Builder
public class PeTplConfig {
    String entity = "/templates/entity.java";
    String mapper = "/templates/mapper.java";
    String mapperXml = "/templates/mapper.xml";
    String service = "/templates/service.java";
    String serviceImpl = "/templates/serviceImpl.java";
    String controller = "/templates/controller.java";
}
