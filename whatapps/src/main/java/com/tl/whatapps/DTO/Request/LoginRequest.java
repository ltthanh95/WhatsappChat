package com.tl.whatapps.DTO.Request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoginRequest {
    String email;
    String password;

    @Override
    public String toString() {
        return "LoginRequest [email=" + email + ", password=" + password + "]";
    }
}
