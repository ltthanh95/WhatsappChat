package com.tl.whatapps.Entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Integer id;

    String chatName;

    String chatImage;

    boolean isGroup;

    @ManyToMany
    Set<User> admins = new HashSet<>();

    @ManyToOne
    User createdBy;

    @ManyToMany
    Set<User> users = new HashSet<>();

    @OneToMany
    List<Message> messages = new ArrayList<>();

    @Override
    public String toString() {
        return "Chat [id=" + id + ", chatName=" + chatName + ", chatImage=" + chatImage + ", isGroup=" + isGroup
                + ", admins=" + admins + ", createdBy=" + createdBy + ", users=" + users + ", messages=" + messages
                + "]";
    }
}
