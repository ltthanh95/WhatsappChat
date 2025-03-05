package com.tl.whatapps.DTO;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ErrorDetail {

    String error;
    String message;
    LocalDateTime timestamp;
    @Override
    public String toString() {
        return "ErrorDetail [error=" + error + ", message=" + message + ", timeStamp=" + timestamp + "]";
    }

}
