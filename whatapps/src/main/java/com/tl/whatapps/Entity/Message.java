package com.tl.whatapps.Entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Integer id;
    String content;
    LocalDateTime timestamp;

    @ManyToOne
    Chat chat;

    @ManyToOne
    User user;

    @Override
    public String toString() {
        return "Message [id=" + id + ", content=" + content + ", timestamp=" + timestamp + ", chat=" + chat + ", user="
                + user + "]";
    }
}
