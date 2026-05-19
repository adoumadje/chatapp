package com.adoumadje.chatapp.user.entity;

import com.adoumadje.chatapp.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Entity
@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatUser extends BaseEntity {
    @Column(unique = true, updatable = false)
    private String username;
    private String firstname;
    private String lastname;
    @Column(unique = true, updatable = false)
    private String email;
    private String profilePictureUrl;
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(unique = true, updatable = false)
    private UUID mailBoxId;
}
