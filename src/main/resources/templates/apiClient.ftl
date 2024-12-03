package ${packageName};

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "${api.name}", url = "${api.baseUrl}")
public interface ${api.name}Client {

<#list endpoints as endpoint>
    <#-- Transform the HTTP method to the corresponding annotation -->
    <#if endpoint.method == "GET">
        @GetMapping("${endpoint.path}")
    <#elseif endpoint.method == "POST">
        @PostMapping("${endpoint.path}")
    <#elseif endpoint.method == "PUT">
        @PutMapping("${endpoint.path}")
    <#elseif endpoint.method == "DELETE">
        @DeleteMapping("${endpoint.path}")
    <#else>
        @RequestMapping(method=RequestMethod.${endpoint.method}, path="${endpoint.path}")
    </#if>

    ${endpoint.response.dto} ${endpoint.name}(
    <#list endpoint.params as param>
        <#if param.addTo == "query">
            @RequestParam("${param.name}") ${param.type} ${param.name}
        <#else>
            @PathVariable("${param.name}") ${param.type} ${param.name}
        </#if>
        <#if param_has_next>, </#if>
    </#list>
    );
</#list>
}
