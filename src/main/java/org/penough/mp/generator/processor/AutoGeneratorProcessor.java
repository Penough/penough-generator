package org.penough.mp.generator.processor;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.builder.GeneratorBuilder;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.function.ConverterFileName;
import com.baomidou.mybatisplus.generator.keywords.MySqlKeyWordsHandler;
import org.penough.mp.generator.config.*;
import org.penough.mp.generator.constant.TableConstant;
import org.penough.mp.generator.util.EnumFieldUtil;
import org.penough.mp.generator.util.PathUtil;

import java.io.File;

/**
 * 自动生成器处理器器
 *
 * @author Penough
 * @date 2021-09-02
 */
public class AutoGeneratorProcessor {


    /**
     * 生成代码器
     * @param config
     * @return
     */
    public AutoGenerator generatorCommonAutoGenerator(CommonConfig config){
        // 链接数据源
        AutoGenerator generator = new AutoGenerator(generateDSConfig(config.getGlobalConfig()));
        // 全局配置
        generator.global(generateGlobalConfig(config.getGlobalConfig()));
        // 包路径配置
        generator.packageInfo(generatePackageConfig(config.getPackageConfig()));
        // 模板文件配置
        generator.template(generateTplConfig(config.getTplConfig()));
        // 生成策略配置
        generator.strategy(generateStrategyConfig(config.getStratrgyConfig()));
        return generator;
    }

    /**
     * 数据源配置
     * @param config
     * @return
     */
    private DataSourceConfig generateDSConfig(PeGlobalConfig config){
        return new DataSourceConfig.Builder(config.getDatabaseUrl(), config.getUserName(), config.getPwd())
                .typeConvert(new MySqlTypeConvert())
                .keyWordsHandler(new MySqlKeyWordsHandler())
                .dbQuery(new MySqlQueryExt()) // 可以客制化覆盖表信息查询语句
//                .schema("mybatis-plus") // 部分数据库适用，mysql和database等效，url带有了，不需要另外写
                .build();
    }

    /**
     * 全局设置
     * dateType，commentDate，
     * @param config
     * @return
     */
    private GlobalConfig generateGlobalConfig(PeGlobalConfig config){
        String fileDir = config.getProjectRootPath()
                + File.separator + config.getServicePrefix() + config.getServiceName()
                + File.separator + PathUtil.SRC_MAIN_JAVA;
        GlobalConfig.Builder builder = GeneratorBuilder.globalConfigBuilder()
                .openDir(config.getOpenDir())
                .author(config.getAuthor())
                .outputDir(fileDir)
                .dateType(config.getDateType())
                .commentDate(config.getCommentDate());
        fileOverride(builder, config.getFileOverride());
        kotlin(builder, config.getKotlin());
        swagger(builder, config.getSwagger());
        return builder.build();
    }
    private GlobalConfig.Builder fileOverride(GlobalConfig.Builder builder, Boolean flag){ return flag?builder.fileOverride():builder;}
    private GlobalConfig.Builder kotlin(GlobalConfig.Builder builder, Boolean flag){ return flag?builder.enableKotlin():builder;}
    private GlobalConfig.Builder swagger(GlobalConfig.Builder builder, Boolean flag){ return flag?builder.enableSwagger():builder;}

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
//                    .addIgnoreColumns(TableConstant.IGNORE_COLUMNS)
                    .addTableFills(config.getTableFields())
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
