package com.commerce.catalos.resolvers;

import com.commerce.catalos.models.modules.ModuleResponse;
import com.commerce.catalos.services.ModuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ModuleResolvers {

    private final ModuleService moduleService;

    @QueryMapping
    public ModuleResponse getModuleByResourceId(@Argument("id") final String id) {
        return moduleService.getModuleByResourceId(id);
    }

}
