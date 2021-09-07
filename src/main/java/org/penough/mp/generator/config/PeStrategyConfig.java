package org.penough.mp.generator.config;

import com.baomidou.mybatisplus.generator.IFill;
import com.baomidou.mybatisplus.generator.config.INameConvert;
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
    @Builder.Default
    String superEntity = EntityType.SUPER_ENTITY.getVal();
    @Builder.Default
    String[] includeTables = {};
    @Builder.Default
    String tablePrefix = "penough";
    @Builder.Default
    String versionColumn = TableConstant.VERSION_COLUMN;
    @Builder.Default
    String versionProperty = TableConstant.VERSION_COLUMN;
    @Builder.Default
    String logicalDeleteColumn = TableConstant.LOGICAL_DELETE_COLUMN;
    @Builder.Default
    String logicalDeleteProperty = TableConstant.LOGICAL_DELETE_PROPERTY;
    @Builder.Default
    NamingStrategy namingStrategy = NamingStrategy.underline_to_camel;
    @Builder.Default
    NamingStrategy columnNaming = NamingStrategy.underline_to_camel;
    @Builder.Default
    String[] ignoreColumns = TableConstant.IGNORE_COLUMNS;
    @Builder.Default
    IFill[] tableFields = {};

    /**
     * controller部分配置
     */
    String superController = "com.penough.boot.mvc.controller.SuperController";
    /**
     * mapper部分配置
     */
    String superMapper = "com.penough.boot.database.mapper.SuperMapper";

    /**
     * service部分配置
     */
    String superService = "com.penough.boot.mvc.service.SuperService";

    /**
     * impl部分配置
     */
    String superServiceImpl = "com.penough.boot.mvc.service.SuperServiceImpl";
}
