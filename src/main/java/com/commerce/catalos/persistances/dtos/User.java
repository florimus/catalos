package com.commerce.catalos.persistances.dtos;

import com.commerce.catalos.core.enums.GrandType;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Document("cat_users")
public class User extends BaseDto {

    @Id
    private String id;

    private String userGroupId;

    private String firstName;

    private String lastName;

    private String email;

    private String password;

    private GrandType grandType;

    private String roleId;

    private boolean verified;
}
