package ${enumInfo.package};

import com.fasterxml.jackson.annotation.JsonFormat;
import org.penough.boot.database.enumeration.BaseEnum;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import cn.afterturn.easypoi.excel.annotation.ExcelIgnore;
import cn.afterturn.easypoi.excel.annotation.Excel;

<#assign tableComment="${table.comment!}"/>
<#assign intFlag="$D$"/>
<#if table.comment!?length gt 0>
    <#if table.comment!?contains("\n") >
        <#assign tableComment="${table.comment!?substring(0,table.comment?index_of('\n'))?trim}"/>
    </#if>
</#if>
/**
 * <p>
 * 实体注释中生成的类型枚举
 * ${table.comment!?replace("\n","\n * ")}
 * </p>
 *
 * @author ${author}
 * @date ${date}
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "${enumInfo.enumClassName}", description = "${enumInfo.comment}-枚举")
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ${enumInfo.enumClassName} implements BaseEnum {

    <#list enumInfo.enumFieldsInfo?keys as key>
    /**
     * ${key?replace(intFlag,"")?upper_case}=<#list enumInfo.enumFieldsInfo[key] as atr><#if atr?index_of(intFlag) gt -1>${atr?replace(intFlag,"")} <#else>"${atr}"</#if></#list>
     */
    ${key?replace(intFlag,"")?upper_case}(<#list enumInfo.enumFieldsInfo[key] as atr><#if atr?index_of(intFlag) gt -1>${atr?replace(intFlag,"")}<#else>"${atr}"</#if><#if atr_has_next>,</#if></#list>),
    </#list>
    ;

<#list enumInfo.enumFieldsInfo?keys as key>
<#list enumInfo.enumFieldsInfo[key] as atr>
    <#if atr_index == 0 && enumInfo.enumFieldsInfo[key]?size lt 2 || atr_index == 1>
    @Excel(name = "${enumInfo.comment}")
    @ApiModelProperty(value = "描述")
    private String desp;
    <#elseif atr_index == 0 && enumInfo.enumFieldsInfo[key]?size gte 2>
    @ExcelIgnore
    @ApiModelProperty(value = "编码")
    private <#if atr?index_of(intFlag) gt -1>Integer<#else>String</#if> code;
    <#elseif atr_index gt 0 && enumInfo.enumFieldsInfo[key]?size gte 2>
    @ExcelIgnore
    @ApiModelProperty(value = "属性${atr_index}")
    private <#if atr?index_of(intFlag) gt -1>Integer<#else>String</#if> atr${atr_index};
    </#if>
</#list>
<#break>
</#list>

    public String getDesp(){
        return this.desp;
    }

    public static ${enumInfo.enumClassName} matchByDesp(String val, ${enumInfo.enumClassName} def) {
        for (${enumInfo.enumClassName} enm : ${enumInfo.enumClassName}.values()) {
            if (enm.getDesp().equalsIgnoreCase(val)) {
                return enm;
            }
        }
        return def;
    }

    public static ${enumInfo.enumClassName} getByDesp(String val) {
        return matchByDesp(val, null);
    }

    public boolean eq(${enumInfo.enumClassName} val) {
        if (val == null) {
            return false;
        }
        return this.name().equalsIgnoreCase(val.name());
    }

<#list enumInfo.enumFieldsInfo?keys as key>
    <#if enumInfo.enumFieldsInfo[key]?size lt 2>
    public boolean eq(String val) {
        return this.name().equalsIgnoreCase(val);
    }

    public static ${enumInfo.enumClassName} match(String val, ${enumInfo.enumClassName} def) {
        for (${enumInfo.enumClassName} enm : ${enumInfo.enumClassName}.values()) {
            if (enm.name().equalsIgnoreCase(val)) {
                return enm;
            }
        }
        return def;
    }

    public static ${enumInfo.enumClassName} get(String val) {
        return match(val, null);
    }

    @Override
    @ApiModelProperty(value = "编码", allowableValues = "<#list enumInfo.enumFieldsInfo?keys as key>${key?upper_case}<#if key_has_next>,</#if></#list>", example = "${enumInfo.enumFieldsInfo?keys[0]?upper_case}")
    public String getCode() {
        return this.name();
    }
    <#else>
    <#list enumInfo.enumFieldsInfo?keys as key>
    <#if enumInfo.enumFieldsInfo[key][0]?index_of(intFlag) gt -1>
    public boolean eq(Integer code) {
        return this.code.equals(code);
    }

    public static ${enumInfo.enumClassName} match(Integer val, ${enumInfo.enumClassName} def) {
        for (${enumInfo.enumClassName} enm : ${enumInfo.enumClassName}.values()) {
            if (enm.eq(val)) {
                return enm;
            }
        }
        return def;
    }

    public static ${enumInfo.enumClassName} get(Integer val) {
        return match(val, null);
    }

    @Override
    @ApiModelProperty(value = "编码", allowableValues = "<#list enumInfo.enumFieldsInfo?keys as k>${enumInfo.enumFieldsInfo[k][0]?replace(intFlag,"")}<#if k_has_next>,</#if></#list>", example = "${enumInfo.enumFieldsInfo[key][0]?replace(intFlag,"")}")
    public Integer getCode() {
        return this.code;
    }
    <#else>
    public boolean eq(String code) {
        return this.code.equalsIgnoreCase(val);
    }

    public static ${enumInfo.enumClassName} match(String val, ${enumInfo.enumClassName} def) {
        for (${enumInfo.enumClassName} enm : ${enumInfo.enumClassName}.values()) {
            if (enm.eq(val)) {
                return enm;
            }
        }
        return def;
    }

    public static ${enumInfo.enumClassName} get(String val) {
        return match(val, null);
    }

    @Override
    @ApiModelProperty(value = "编码", allowableValues = "<#list enumInfo.enumFieldsInfo?keys as k>${enumInfo.enumFieldsInfo[k][0]?replace(intFlag,"")}<#if k_has_next>,</#if></#list>", example = "${enumInfo.enumFieldsInfo[key][0]}")
    public String getCode() {
        return this.code;
    }
    </#if>
    <#break>
    </#list>


    </#if>
<#break>
</#list>
}