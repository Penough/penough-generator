package org.penough.mp.generator.processor;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.TemplateConfig;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.querys.MySqlQuery;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.keywords.MySqlKeyWordsHandler;
import org.penough.mp.generator.config.*;
import org.penough.mp.generator.constant.TableConstant;
import org.penough.mp.generator.constant.TplConstant;

public class AutoGeneratorProcessor {


    /**
     * 生成代码器
     * @param config
     * @return
     */
    public AutoGenerator generatorCommonAutoGenerator(CommonConfig config){
        AutoGenerator generator = new AutoGenerator(generateDSConfig(config.getGlobalConfig()));
        generator.packageInfo(generatePackageConfig(config.getPackageConfig()));
        generator.template(generateTplConfig(config.getTplConfig()));
        generator.strategy(generateStrategyConfig(config.getStratrgyConfig()));
        return generator;
    }

    /**
     * 数据源配置
     * @param config
     * @return
     */
    private DataSourceConfig generateDSConfig(PeGlobalConfig config){
        return new DataSourceConfig.Builder(config.getDatabaseUrl(), config.getDatabaseUrl(), config.getPwd())
                .typeConvert(new MySqlTypeConvert())
                .keyWordsHandler(new MySqlKeyWordsHandler())
                .dbQuery(new MySqlQuery())
                .schema("mybatis-plus")
                .build();
    }

    /**
     * 包配置
     * @param config
     * @return
     */
    private PackageConfig generatePackageConfig(PePackageConfig config){
        return new PackageConfig.Builder().parent(config.getParentPackage())
                .moduleName(config.getModule())
                .entity(config.getEntityPackage())
                .service(config.getServicePackage())
                .serviceImpl(config.getServiceImplPackage())
                .mapper(config.getMapperPackage())
                .xml(config.getXmlPackage())
                .controller(config.getControllerPackage())
                .build();
    }

    /**
     * 模板配置
     * @return
     */
    private TemplateConfig generateTplConfig(PeTplConfig config){
        return new TemplateConfig.Builder()
                .entity(config.getEntity())
                .service(config.getService(), config.getServiceImpl())
                .mapper(config.getMapper())
                .mapperXml(config.getMapperXml())
                .controller(config.getController())
                .build();
    }

    /**
     * 策略配置
     * @return
     */
    private StrategyConfig generateStrategyConfig(PeStrategyConfig config){
        return new StrategyConfig.Builder()
                .addInclude(config.getIncludeTables())
                .addTablePrefix(config.getTablePrefix())
                .entityBuilder()// 实体配置构建者
                    .superClass(config.getSuperEntity())
                    .enableLombok()// 开启lombok模型
                    .versionColumnName(TableConstant.VERSION_COLUMN) //乐观锁数据库表字段
                    .versionPropertyName(TableConstant.VERSION_COLUMN)
                    .logicDeleteColumnName(TableConstant.LOGICAL_DELETE_COLUMN)//逻辑删除字段处理
                    .logicDeletePropertyName(TableConstant.LOGICAL_DELETE_PROPERTY)
                    .naming(NamingStrategy.underline_to_camel)// 数据库表映射到实体的命名策略
                    .columnNaming(NamingStrategy.underline_to_camel)// 字段到实体属性命名策略
                    .addIgnoreColumns(TableConstant.IGNORE_COLUMNS)
                .controllerBuilder()
                    .superClass(config.getSuperController())
                    .enableRestStyle()
                .mapperBuilder()
                    .superClass(config.getSuperMapper())
                    .enableBaseColumnList()
                    .enableBaseResultMap()
                .serviceBuilder()
                    .superServiceClass(config.getSuperService())
                    .superServiceImplClass(config.getSuperServiceImpl())
                .build();
    }
}
