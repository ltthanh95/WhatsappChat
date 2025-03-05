package com.tl.whatapps.DTO.Request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SingleChatRequest {
    Integer userId;

    @Override
    public String toString() {
        return "SingleChatRequest [userId=" + userId + "]";
    }
}
