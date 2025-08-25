package com.commerce.catalos.persistence.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document("cat_emailProviders")
@EqualsAndHashCode(callSuper = true)
public class EmailProviderSettings extends BaseDto {

    @Id
    private String id;

    private String connectionUrl;

    private String url;

    private String method;

    private String authenticationType;

    private Map<String, String> credentials;

    private List<String> applicableChannels;

}
