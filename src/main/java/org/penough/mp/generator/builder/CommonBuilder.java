package org.penough.mp.generator.builder;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.SimpleAutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.querys.MySqlQuery;
import com.baomidou.mybatisplus.generator.keywords.MySqlKeyWordsHandler;
import lombok.extern.slf4j.Slf4j;
import org.penough.mp.generator.config.CommonConfig;
import org.penough.mp.generator.engine.MyFreeMarkerEngine;
import org.penough.mp.generator.processor.AutoGeneratorProcessor;

import javax.sql.DataSource;

@Slf4j
public class CommonBuilder {
    public static void buildProject(){}

    public static void buildModel(){}

    public static void buildComponentInExistingSource(final CommonConfig config){
        if(StrUtil.isBlank(config.getGlobalConfig().getDatabaseUrl())){
            log.error("未设置数据库连接，无法进行创建...");
            return;
        }
        // 代码生成器
        AutoGeneratorProcessor processor = new AutoGeneratorProcessor();
        AutoGenerator mpg = processor.generatorCommonAutoGenerator(config);
        // 使用FreeMarker引擎自动生成枚举类
        mpg.execute(new MyFreeMarkerEngine());
    }


}
