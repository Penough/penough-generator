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

import java.io.File;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    public final static String INT_FLAG = "$d$";
    public final static String ENUM_INFO_FIELD_REG = "([A-Za-z]+[1-9_-]*)?\\[(.*?)\\]";
    private final static String JAVA_SUFFIX = ".java";

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
        var comment = field.getComment();
        var matcher = matchEnumComment(comment);
        if (matcher == null) return;
        // 排除boolean类型值
        if (Boolean.class.getSimpleName().equals(field.getColumnType().getType())) {
            return;
        }
        // 获取需要枚举属性名
        var propertyName = field.getPropertyName();
        // 获取对象信息Map
        var objectMap = getObjectMap(getConfigBuilder(), tableInfo);
        // 获取packageConfig的packageInfo
        Map<String, String> packageInfo = (Map) objectMap.get("package");
        // 获取实体包信息
        var entityPackage = packageInfo.get(ConstVal.ENTITY);
        // 定义枚举类包,把枚举类包放在实体包同级
        var defEnumerationPackage = replaceLast(entityPackage, CommonConstant.ENTITY, CommonConstant.ENUMERATIONS);


        // 确定存在枚举实体类名
        var enumNameOpt = Optional.ofNullable(matcher.group(1));
        var enumClassName = enumNameOpt.orElseGet(() -> useEntityEnumName(tableInfo.getEntityName(), propertyName))
                                    .replace(upperFirstChar(globalConfig.getTablePrefix()), "");
        // 处理customMap
        var EnumsFieldInfo = resolveEnumsFieldInfo(propertyName, matcher.group(2));
        var enumInfo = new HashMap<>();
        enumInfo.put("enumFieldsInfo", EnumsFieldInfo);
        enumInfo.put("enumClassName", enumClassName);
        enumInfo.put("comment", comment);
        enumInfo.put("package", defEnumerationPackage);

        var customMap = new HashMap(field.getCustomMap());
        customMap.put("enumInfo", enumInfo);
        customMap.put("isEnum", true); // 标记字段为枚举
        field.setCustomMap(customMap);
        objectMap.put("enumInfo", enumInfo);
        // 这里把defEnumerationPackage改成文件分割地址，指定实际输出路径
        var clazPath = getRootBasePath() + defEnumerationPackage.replace(StringPool.DOT, File.separator) + File.separator;
        File outputFile = new File(clazPath + enumClassName + JAVA_SUFFIX);
        if(!outputFile.getParentFile().exists()) outputFile.getParentFile().mkdirs();
        if(!outputFile.exists()) outputFile.createNewFile();
        log.info("正在向文件：{}，输出枚举类...", outputFile.getAbsolutePath());
        writer(objectMap, TplConstant.ENUM, outputFile);
    }



    /**
     * 解析枚举类属性信息
     * @param propertyName
     * @param fieldsInfo
     * @return
     */
    private Map<String, List<String>> resolveEnumsFieldInfo(String propertyName, String fieldsInfo) {
        var res = new LinkedHashMap<String, List<String>>();
        var pattern = Pattern.compile(ENUM_INFO_FIELD_REG);
//        var matcher = pattern.matcher()
        var fieldItems = fieldsInfo.split(StringPool.SEMICOLON);
        Arrays.stream(fieldItems).forEach(i -> {
            var matcher = pattern.matcher(i);
            if(matcher.find()){
                var consFields = resolveItemContent(trimAll(matcher.group(2)));
                // 如果没有写枚举类name的话，就采用类名大写_解析出来的内容域第一位大写
                var name = Optional.ofNullable(trimAll(matcher.group(1)))
                        .orElse(propertyName.toUpperCase() + StringPool.UNDERSCORE + consFields.get(0).toUpperCase());
                res.put(name, consFields);
            }
        });
        return res;
    }
    /**
     * 解析枚举类属性
     * @param content
     * @return
     */
    private List<String> resolveItemContent(String content) {
        var ls = new LinkedList<>(List.of(content.split(StringPool.COMMA)));
        if(ls.size() == 1) return ls; // 如果只有一个配置，说明只有个描述，使用name作为code
        if(ls.size() < 1) throw new RuntimeException("枚举类解析异常，内容数量不满足1个及以上[code, desp, ...]:" + content);
        ls.replaceAll((i) -> isInteger(i)? i+INT_FLAG : i);
        return ls;
    }

    /**
     * 使用枚举类类名
     * 描述例子 eg: 学生性别#genderEnum{FEMALE[0:女];MALE[1:男]}
     * 命名使用实体名+字段名+Enum对该类命名
     * @return
     */
    private String useEntityEnumName(String entityName,String propertyName){
        return entityName + (Character.toUpperCase(propertyName.charAt(0)) + propertyName.substring(1)) + "Enum";
    }

    /**
     * 获取项目根路径，直到main/java
     */
    public static final String SRC_MAIN_JAVA = "src" + File.separator + "main" + File.separator + "java";
    private String getRootBasePath(){
        var rootPath = globalConfig.getProjectRootPath();
        if(!rootPath.endsWith(File.separator)) rootPath += File.separator;
        var sb = new StringBuilder(rootPath);
        sb.append(globalConfig.getServicePrefix()
                + globalConfig.getServiceName()
                + File.separator
                + SRC_MAIN_JAVA
                + File.separator);
        return sb.toString();
    }

    /**
     * 匹配Enum注释信息
     * @return
     */
    private final static String ENUM_INFO_REG = "#([A-Za-z]+[1-9_-]*)?\\{(.*)\\}";
    private static Matcher matchEnumComment(String comment) {
        Pattern pattern = Pattern.compile(ENUM_INFO_REG);
        Matcher matcher = pattern.matcher(comment);
        return matcher.find()?matcher : null;
    }

    public static String replaceLast(String text, String regex, String replacement) {
        return text.replaceFirst("(?s)(.*)" + regex, "$1" + replacement);
    }

    /**
     * 整数判断
     * @param str
     * @return
     */
    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

    /**
     * 清除字符串所有空格
     * @param s
     * @return
     */
    public static String trimAll(String s) {
        if (s == null) return s;
        return s.replace(" ", "");
    }

    /**
     * 大写第一个字符
     * @param s
     * @return
     */
    public static String upperFirstChar(String s){
        char[] chars = s.toCharArray();
        if (chars[0] >= 'a' && chars[0] <= 'z') {
            chars[0] = (char)(chars[0] - 32);
        }
        return new String(chars);
    }
}

