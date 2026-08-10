package com.nagorikseba.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthRequest {
    private String emailOrPhone;
    private String password;
}
