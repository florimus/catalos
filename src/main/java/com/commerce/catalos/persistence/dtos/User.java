package com.commerce.catalos.persistence.dtos;

import com.commerce.catalos.core.enums.GrandType;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document("cat_users")
public class User extends BaseDto {

    @Id
    private String id;

    private String userGroupId;

    private String firstName;

    private String lastName;

    private String email;

    private String avatar;

    private String password;

    private Map<String, String> token;

    private GrandType grandType;

    private String roleId;

    private boolean verified;
}
