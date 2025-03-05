package com.tl.whatapps.DTO.Request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SendMessageRequest {
    Integer userId;
    Integer chatId;
    String content;

    @Override
    public String toString() {
        return "SendMessageRequest [userId=" + userId + ", chatId=" + chatId + ", content=" + content + "]";
    }

}
