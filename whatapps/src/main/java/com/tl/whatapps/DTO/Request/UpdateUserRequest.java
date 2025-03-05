package com.tl.whatapps.DTO.Request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateUserRequest {
    String name;
    String profile;

    @Override
    public String toString() {
        return "UpdateUserRequest [name=" + name + ", profile=" + profile + "]";
    }
}
