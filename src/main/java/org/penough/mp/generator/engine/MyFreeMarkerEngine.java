package org.penough.mp.generator.engine;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.config.ConstVal;
import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.baomidou.mybatisplus.generator.config.po.TableField;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.engine.AbstractTemplateEngine;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.penough.mp.generator.config.PeGlobalConfig;
import org.penough.mp.generator.constant.CommonConstant;
import org.penough.mp.generator.constant.TplConstant;
import org.penough.mp.generator.util.EnumFieldUtil;

import java.io.File;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    public MyFreeMarkerEngine(PeGlobalConfig config) {
        globalConfig = config;
    }

    @Override
    public @NotNull AbstractTemplateEngine batchOutput() {
        var cb = getConfigBuilder();
        var tableInfoList = cb.getTableInfoList();
        tableInfoList.stream().forEach(tableInfo -> {
            tableInfo.getFields().forEach(filed -> {
                try {
                    EnumFieldUtil.generateEnum(tableInfo, filed, globalConfig, this);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        });


        return super.batchOutput();
    }
    
}

