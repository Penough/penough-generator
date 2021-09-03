package ${enumInfo.package};

import com.fasterxml.jackson.annotation.JsonFormat;
import org.penough.boot.database.enumeration.BaseEnum;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;

<#assign tableComment="${table.comment!}"/>
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
     * ${key?replace("$D$","")?upper_case}=<#list enumInfo.enumFieldsInfo[key] as atr><#if atr?index_of("$d$") gt -1>${atr?replace("$d$","")} <#else>"${atr}"</#if></#list>
     */
    ${key?replace("$D$","")?upper_case}(<#list enumInfo.enumFieldsInfo[key] as atr><#if atr?index_of("$d$") gt -1>${atr?replace("$d$","")}<#else>"${atr}"</#if><#if atr_has_next>,</#if></#list>),
    </#list>
    ;

<#list enumInfo.enumFieldsInfo?keys as key>
<#list enumInfo.enumFieldsInfo[key] as atr>
    <#if atr_index == 0 && enumInfo.enumFieldsInfo[key]?size lt 2 || atr_index == 1>
    @ApiModelProperty(value = "描述")
    private String desp;
    <#elseif atr_index == 0 && enumInfo.enumFieldsInfo[key]?size gte 2>
    @ApiModelProperty(value = "编码")
    private <#if atr?index_of("$d$") gt -1>int code<#else>String code</#if>;
    <#elseif atr_index gt 0 && enumInfo.enumFieldsInfo[key]?size gte 2>
    @ApiModelProperty(value = "属性${atr_index}")
    private <#if atr?index_of("$d$") gt -1>int<#else>String</#if> atr${atr_index};
    </#if>
</#list>
<#break>
</#list>

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

    public boolean eq(String val) {
        return this.name().equalsIgnoreCase(val);
    }

    public boolean eq(${enumInfo.enumClassName} val) {
        if (val == null) {
            return false;
        }
        return eq(val.name());
    }


<#list enumInfo.enumFieldsInfo?keys as key>
    <#if enumInfo.enumFieldsInfo[key]?size lt 2>
    @Override
    @ApiModelProperty(value = "编码", allowableValues = "<#list enumInfo.enumFieldsInfo?keys as key>${key?upper_case}<#if key_has_next>,</#if></#list>", example = "${enumInfo.enumFieldsInfo?keys[0]?upper_case}")
    public String getCode() {
        return this.name();
    }
    </#if>
<#break>
</#list>
}