package com.commerce.catalos.persistence.dtos;

import com.commerce.catalos.core.enums.AddressType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document("cat_address")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Address extends BaseDto {

    @Id
    private String id;

    private String sourceId;

    private AddressType addressType;

    private String phone;

    private String addressLine1;

    private String addressLine2;

    private String country;

    private String state;

    private String city;

    private String area;

    private Integer pinCode;
}
