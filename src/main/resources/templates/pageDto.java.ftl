package ${dtoPkg};

import java.time.LocalDateTime;
<#list table.importPackages as pkg>
import ${pkg};
</#list>
<#if swagger>
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
</#if>
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import lombok.*;
import lombok.experimental.Accessors;
import java.io.Serializable;

/**
 * <p>
 * 实体Page模型
 * ${table.comment!?replace("\n","\n * ")}
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@Builder
<#if swagger>
@ApiModel(value = "${entity}PageDTO", description = "${table.comment!?replace("\r\n"," ")?replace("\r"," ")?replace("\n"," ")}")
</#if>
public class ${entity}PageDTO implements Serializable {

    private static final long serialVersionUID = 1L;

<#list table.fields as field>
<#if field.propertyName !="createTime" && field.propertyName != "modifyTime" && field.propertyName !="createUser" && field.propertyName !="modifyUser">
    <#if field.keyFlag>
        <#assign keyPropertyName="${field.propertyName}"/>
    </#if>
    <#assign fieldComment="${field.comment!}"/>
    <#if field.comment!?length gt 0>
    /**
     * ${field.comment!?replace("\n","\n     * ")}
     */
        <#if field.comment!?contains("\n") >
            <#assign fieldComment="${field.comment!?substring(0,field.comment?index_of('\n'))?replace('\r\n','')?replace('\r','')?replace('\n','')?trim}"/>
        </#if>
    </#if>
    <#if swagger>
    @ApiModelProperty(value = "${fieldComment}")
    </#if>
    <#assign myPropertyType="${field.propertyType}"/>
    <#assign isEnumType=field.customMap.isEnum!false/>
    <#if field.customMap.Null == "NO" >
        <#if (field.columnType!"") == "STRING" && !isEnumType>
    @NotEmpty(message = "${fieldComment}不能为空")
        <#else>
    @NotNull(message = "${fieldComment}不能为空")
        </#if>
    </#if>
    <#if isEnumType>
    @EnumValidation(${field.customMap.enumInfo.enumClassName}.class)
    <#elseif (field.columnType!"") == "STRING">
        <#assign max = 255/>
        <#if field.type?starts_with("varchar") || field.type?starts_with("char")>
            <#if field.type?contains("(")>
                <#assign max = field.type?substring(field.type?index_of("(") + 1, field.type?index_of(")"))/>
            </#if>
    @Length(max = ${max}, message = "${fieldComment}长度不能超过${max}")
        <#elseif field.type?starts_with("text")>
            <#assign max = 65535/>
    @Length(max = ${max?string["0"]}, message = "${fieldComment}长度不能超过${max}")
        <#elseif field.type?starts_with("mediumtext")>
            <#assign max = 16777215/>
    @Length(max = ${max?string["0"]}, message = "${fieldComment}长度不能超过${max}")
        <#elseif field.type?starts_with("longtext")>
        </#if>
    <#else>
        <#if field.propertyType?starts_with("Short")>
    @Range(min = Short.MIN_VALUE, max = Short.MAX_VALUE, message = "${fieldComment}长度不能超过"+Short.MAX_VALUE)
        </#if>
        <#if field.propertyType?starts_with("Byte")>
    @Range(min = Byte.MIN_VALUE, max = Byte.MAX_VALUE, message = "${fieldComment}长度不能超过"+Byte.MAX_VALUE)
        </#if>
        <#if field.propertyType?starts_with("Short")>
    @Range(min = Short.MIN_VALUE, max = Short.MAX_VALUE, message = "${fieldComment}长度不能超过"+Short.MAX_VALUE)
        </#if>
    </#if>
    <#assign myPropertyName="${field.propertyName}"/>
    private ${myPropertyType} ${myPropertyName};
</#if>
</#list>

}
