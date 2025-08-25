package com.commerce.catalos.models.emails;

import com.commerce.catalos.core.enums.EmailNotificationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailRequest {

    private List<String> to;

    private EmailNotificationType type;

    private String applicableChannel;

    private Map<String, Object> payload;
}
