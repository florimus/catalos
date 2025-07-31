package com.commerce.catalos.security;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@Data
@Component
@RequestScope
public class RequestContext {

    private String language;

    private String channel;

    public void populateRequestContext(final HttpServletRequest request) {
        String language = request.getHeader("language");
        String channel = request.getHeader("channel");
        if(null != channel) {
            this.setChannel(channel);
        }
        if ((null != language)) {
            this.setLanguage(language);
        }
    }
}
