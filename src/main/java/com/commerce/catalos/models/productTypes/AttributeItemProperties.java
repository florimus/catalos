package com.commerce.catalos.models.productTypes;

import com.commerce.catalos.core.enums.AttributeTarget;
import com.commerce.catalos.core.enums.AttributeType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AttributeItemProperties {

    private AttributeType type;

    private Object options;

    private Object value;
}
