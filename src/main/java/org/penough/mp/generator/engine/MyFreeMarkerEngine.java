package org.penough.mp.generator.engine;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.config.ConstVal;
import com.baomidou.mybatisplus.generator.config.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.engine.AbstractTemplateEngine;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.penough.mp.generator.config.CommonConfig;
import org.penough.mp.generator.config.PeGlobalConfig;
import org.penough.mp.generator.config.PeStrategyConfig;
import org.penough.mp.generator.constant.CommonConstant;
import org.penough.mp.generator.constant.TplConstant;
import org.penough.mp.generator.util.EnumFieldUtil;
import org.penough.mp.generator.util.PathUtil;

import java.io.File;
import java.util.Map;
import java.util.function.BiConsumer;

import static com.baomidou.mybatisplus.generator.config.ConstVal.JAVA_SUFFIX;

/**
 * 自定义FreeMarker引擎
 *
 * 功能1：
 *      编号部分自动大写
 *      处理枚举类模板，解析数据库注释中模板类信息，注释中写入
 *      如：#cname{[male,男];[female,女]}
 * 功能2：
 *  todo 待定
 */
@Slf4j
public class MyFreeMarkerEngine extends FreemarkerTemplateEngine {

    private PeGlobalConfig globalConfig;
    private PeStrategyConfig strategyConfig;
    public MyFreeMarkerEngine(CommonConfig config) {
        globalConfig = config.getGlobalConfig();
        strategyConfig = config.getStratrgyConfig();
    }

    @Override
    public @NotNull AbstractTemplateEngine batchOutput() {
        var cb = getConfigBuilder();
        var tableInfoList = cb.getTableInfoList();
        tableInfoList.stream().forEach(tableInfo -> {
            tableInfo.getFields().forEach(filed -> {
                try {
                    EnumFieldUtil.generateEnum(tableInfo, filed, globalConfig, strategyConfig, this);
                    boolean flag = true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            injectCfg(cb, tableInfo);
        });
        // 不需要使用tableInfo的DTO类采用InjectionConfig类进行配置注入
//        InjectionConfig injCfg = new InjectionConfig();
//        injCfg.
//        cb.setInjectionConfig(injCfg);

        return super.batchOutput();
    }

    private void injectCfg(ConfigBuilder cb, TableInfo tableInfo) {
//        InjectionConfig cfg = cb.getInjectionConfig();
        BiConsumer<TableInfo, Map<String, Object>> biConsumer = new BiConsumer<TableInfo, Map<String, Object>>() {
            @Override
            public void accept(TableInfo tableInfo, Map<String, Object> stringObjectMap) {
                // todo 处理输出文件
                Map<String, String> packageInfo = (Map)stringObjectMap.get(CommonConstant.PACKAGE);
                // 获取实体包信息
                var entityPackage = packageInfo.get(ConstVal.ENTITY);
                var dtoPkg = entityPackage + StringPool.DOT + CommonConstant.DTO;
                var dtoPkgPath = PathUtil.getRootBasePath(globalConfig) + dtoPkg.replace(StringPool.DOT, File.separator)
                        + File.separator + tableInfo.getEntityName().toUpperCase();
                var entityName = stringObjectMap.get(CommonConstant.ENTITY);
                stringObjectMap.put(CommonConstant.DTO_PKG, dtoPkg);
                File outputFile = new File(dtoPkgPath + File.separator + entityName + CommonConstant.PAGE_DTO + JAVA_SUFFIX);
                outputFile(outputFile, stringObjectMap, TplConstant.PAGE_DTO);
                outputFile = new File(dtoPkgPath + File.separator + entityName + CommonConstant.UPDATE_DTO + JAVA_SUFFIX);
                outputFile(outputFile, stringObjectMap, TplConstant.UPDATE_DTO);
                outputFile = new File(dtoPkgPath + File.separator + entityName + CommonConstant.SAVE_DTO + JAVA_SUFFIX);
                outputFile(outputFile, stringObjectMap, TplConstant.SAVE_DTO);
            }
        };
        InjectionConfig cfg = new InjectionConfig.Builder().beforeOutputFile(biConsumer).build();
        cb.setInjectionConfig(cfg);
    }

}

