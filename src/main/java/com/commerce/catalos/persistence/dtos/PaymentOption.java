package com.commerce.catalos.persistence.dtos;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.commerce.catalos.core.enums.PaymentMode;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@Document("cat_payment_options")
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class PaymentOption extends BaseDto {

    @Id
    private String id;

    private String name;

    private List<String> applicableChannels;

    private boolean isExternal;

    private PaymentMode mode;
}
