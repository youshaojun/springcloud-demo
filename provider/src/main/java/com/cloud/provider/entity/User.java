package com.cloud.provider.entity;

import lombok.Data;
import org.springframework.data.annotation.Transient;

@Data
public class User {

    private Integer id;

    private String username;

    private String address;

    @Transient
    private Integer index;

}
