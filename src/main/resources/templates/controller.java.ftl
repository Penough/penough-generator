package ${package.Controller};

<#if superControllerClass??>
import ${package.Entity}.${entity};
import ${dtoPkg}.${entity}SaveDTO;
import ${dtoPkg}.${entity}UpdateDTO;
import ${dtoPkg}.${entity}PageDTO;
import ${package.Service}.${table.serviceName};
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
</#if>
<#if superControllerClassPackage??>
import ${superControllerClassPackage};
</#if>
import org.penough.boot.mvc.controller.request.ApiResult;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
<#-- 这里应该由一个鉴权注解 -->
<#if restControllerStyle>
import org.springframework.web.bind.annotation.RestController;
<#else>
import org.springframework.stereotype.Controller;
</#if>


<#assign tableComment = "${table.comment!''}"/>
<#if table.comment?? && table.comment!?contains('\n')>
    <#assign tableComment = "${table.comment!?substring(0,table.comment?index_of('\n'))?trim}"/>
</#if>
/**
 * <p>
 * 前端控制器
 * ${table.comment!?replace("\n","\n * ")}
 * </p>
 *
 * @author ${author}
 * @date ${date}
 */
@Slf4j
@Validated
<#if restControllerStyle>
@RestController
<#else>
@Controller
</#if>
@RequestMapping("<#if package.ModuleName??>/${package.ModuleName}</#if>/<#if controllerMappingHyphenStyle??>${controllerMappingHyphen}<#else>${table.entityPath}</#if>")
<#if swagger>
@Api(value = "${entity}", tags = "${tableComment}")
</#if>
<#if kotlin>
    class ${table.controllerName}<#if superControllerClass??> : ${superControllerClass}()</#if>
<#else>
    <#if superControllerClass??>
public class ${table.controllerName} extends ${superControllerClass}<${table.serviceName}, <#list table.commonFields as field><#if field.keyFlag>${field.propertyType}</#if></#list><#list table.fields as field><#if field.keyFlag>${field.propertyType}</#if></#list>, ${entity}, ${entity}PageDTO, ${entity}SaveDTO, ${entity}UpdateDTO> {
    <#else>
public class ${table.controllerName} {
    </#if>

<#if superControllerClass??>
    /**
     * Excel导入后的操作
     *
     * @param list
     */
    @Override
    public ApiResult<Boolean> handlerImport(List<Map<String, String>> list){
        List<${entity}> ${entity?uncap_first}List = list.stream().map((map) -> {
            ${entity} ${entity?uncap_first} = ${entity}.builder().build();
            //TODO 请在这里完成转换
            return ${entity?uncap_first};
        }).collect(Collectors.toList());

        return ApiResult.SUCCESS(baseService.saveBatch(${entity?uncap_first}List));
    }
</#if>
}
</#if>
