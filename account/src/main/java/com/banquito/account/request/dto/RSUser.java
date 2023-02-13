package com.banquito.account.request.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RSUser {
    String userName;
    String password;
    String type;
    String status;
    Date creationDate;
    Date lastLoginDate;
}
