package org.penough.mp.generator.engine;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.generator.config.ConstVal;
import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.baomidou.mybatisplus.generator.config.po.TableField;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.engine.AbstractTemplateEngine;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import org.jetbrains.annotations.NotNull;
import org.penough.mp.generator.constant.TplConstant;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 自定义FreeMarker引擎
 *
 * 功能1：
 *      s:short, b:byte, i:int, l:long, c:char, d:double, f:float, B:boolean, S:String
 *      {结构}[编号1:定义1;编号2:定义2]
 *      编号部分自动大写
 *      处理枚举类模板，解析数据库注释中模板类信息，注释中写入
 *      如：#enum(FieldEnum{iSB}[CODE1:1,是,true; CODE2:2,否,false])
 * 功能2：
 *  todo 待定
 */
public class MyFreeMarkerEngine extends FreemarkerTemplateEngine {

    /**
     * 注入字段 正则
     * 匹配： @InjectionField(api="", method="") RemoteData<Long, Org>
     * 匹配： @InjectionField(api="", method="" beanClass=Xxx.class)
     * 匹配： @InjectionField(api="orgApi", method="findXx" beanClass=Org.class) RemoteData<Long, com.xx.xx.Org>
     * 匹配： @InjectionField(feign=OrgApi.class, method="findXx" beanClass=Org.class) RemoteData<Long, com.xx.xx.Org>
     */
    private final static Pattern INJECTION_FIELD_PATTERN = Pattern.compile("([@]InjectionField[(](api|feign)? *= *([a-zA-Z0-9\"._]+), method *= *([a-zA-Z0-9\"._]+)(, *beanClass *= *[a-zA-Z0-9._]+)?[)]){1}( *RemoteData(<[a-zA-Z0-9.]+,( *[a-zA-Z0-9.]+)>)?)*");

    /**
     * 枚举类 正则
     * 匹配： #{xxxx} 形式的注释
     */
    public final static String REG_EX_VAL = "#(.*?\\{(.*?)?\\})";
    /**
     * 枚举类型 正则
     * 匹配 xx:xx; 形式的注释
     */
    public final static String REG_EX_KEY = "([A-Za-z1-9_-]+):(.*?)?;";

    @Override
    public @NotNull AbstractTemplateEngine batchOutput() {
        ConfigBuilder cb = getConfigBuilder();
        List<TableInfo> tableInfoList = cb.getTableInfoList();
        tableInfoList.stream().forEach(tableInfo -> {
            tableInfo.getFields().forEach(filed -> {
                try {
                    generateEnum(tableInfo, filed);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        });
        return super.batchOutput();
    }


    /**
     * 生成枚举类型类
     *
     * @throws Exception
     */
    private void generateEnum(TableInfo tableInfo, TableField field) throws Exception {
        String comment = field.getComment();
        if (StringUtils.isBlank(comment) || !comment.contains("{") || !comment.contains("}") || !comment.contains(";")) {
            return;
        }
        // 排除boolean类型值
        if (Boolean.class.getSimpleName().equals(field.getColumnType().getType())) {
            return;
        }
        // 获取需要枚举属性名
        String propertyName = field.getPropertyName();

        // 获取对象信息Map
        Map<String, Object> objectMap = getObjectMap(getConfigBuilder(),tableInfo);
        // 获取packageConfig的packageInfo
        Map<String, String> packageInfo = (Map) objectMap.get("package");
        // 获取实体包信息
        String entityPackage = packageInfo.get(ConstVal.ENTITY);
        // 定义枚举类包,把枚举类包放在实体包同级
        String defEnumerationPackage = replaceLast(entityPackage, "entity","enumeration");
        String enumName = analysisEnumClassName(tableInfo.getEntityName(), propertyName, comment);

        writer(objectMap, TplConstant.ENUM, );
    }

    /**
     * 分析枚举类类名
     * @param comment
     * @return
     */
    private String analysisEnumClassName(String entityName,String propertyName,String comment){
        String enumName = comment.substring(comment.indexOf(StringPool.HASH) + 1, comment.indexOf(StringPool.LEFT_BRACE));
        if ("".equals(enumName)) {
            enumName = entityName + (Character.toUpperCase(propertyName.charAt(0)) + propertyName.substring(1)) + "Enum";
        }
        return enumName;
    }

    private static String trim(String val) {
        return val == null ? StringPool.EMPTY : val.trim();
    }

    public static String replaceLast(String text, String regex, String replacement) {
        return text.replaceFirst("(?s)(.*)" + regex, "$1" + replacement);
    }
}
