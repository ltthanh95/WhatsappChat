package com.tl.whatapps.DTO.Response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ApiResponse {
    String message;
    boolean status;

    @Override
    public String toString() {
        return "ApiResponse [message=" + message + ", status=" + status + "]";
    }


}
