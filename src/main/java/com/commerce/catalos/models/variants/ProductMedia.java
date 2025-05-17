package com.commerce.catalos.models.variants;

import com.commerce.catalos.core.enums.MediaType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductMedia {

    private MediaType type;

    private String defaultSrc;

    private String lg;

    private String md;

    private String sm;

    private String alt;
}
