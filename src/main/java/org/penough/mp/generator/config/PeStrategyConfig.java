package org.penough.mp.generator.config;

import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import lombok.*;
import lombok.experimental.Accessors;
import org.penough.mp.generator.constant.TableConstant;
import org.penough.mp.generator.type.EntityType;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
@Builder
public class PeStrategyConfig {
    /**
     * entity策略配置部分(Table)
     */
    String superEntity = EntityType.SUPER_ENTITY.getVal();
    String[] includeTables = {};
    String tablePrefix = "penough";
    String versionColumn = TableConstant.VERSION_COLUMN;
    String versionProperty = TableConstant.VERSION_COLUMN;
    String logicalDeleteColumn = TableConstant.LOGICAL_DELETE_COLUMN;
    String logicalDeleteProperty = TableConstant.LOGICAL_DELETE_PROPERTY;
    NamingStrategy namingStrategy = NamingStrategy.underline_to_camel;
    NamingStrategy columnNaming = NamingStrategy.underline_to_camel;
    String[] ignoreColumns = TableConstant.IGNORE_COLUMNS;

    /**
     * controller部分配置
     */
    String superController = "com.penough.boot.common.mvc.controller.SuperController";
    /**
     * mapper部分配置
     */
    String superMapper = "com.penough.boot.common.mvc.mapper.SuperMapper";

    /**
     * service部分配置
     */
    String superService = "com.penough.boot.common.mvc.service.SuperService";

    /**
     * impl部分配置
     */
    String superServiceImpl = "com.penough.boot.common.mvc.service.SuperServiceImpl";
}
