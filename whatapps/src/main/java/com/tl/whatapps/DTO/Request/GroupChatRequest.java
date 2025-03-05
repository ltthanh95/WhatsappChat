package com.tl.whatapps.DTO.Request;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GroupChatRequest {
    List<Integer> userIds;
    String chatName;
    String chatImage;

    @Override
    public String toString() {
        return "GroupChatRequest [userIds=" + userIds + ", chatName=" + chatName + ", chatImage=" + chatImage + "]";
    }
}
