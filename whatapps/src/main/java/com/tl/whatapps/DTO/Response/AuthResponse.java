package com.tl.whatapps.DTO.Response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthResponse {
    String jwt;
    boolean isAuth;

    @Override
    public String toString() {
        return "AuthResponse [jwt=" + jwt + ", isAuth=" + isAuth + "]";
    }
}
