package com.adoumadje.chatapp.user;

import com.adoumadje.chatapp.user.entity.ChatUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<ChatUser, Long> {
    Optional<ChatUser> findByEmail(String email);

    Page<ChatUser> findByIdNot(Long excludedId, Pageable pageable);

    @Query("""
            SELECT u FROM ChatUser u
            WHERE (
                LOWER(u.username) LIKE LOWER(CONCAT('%', :keyword, '%'))
                OR LOWER(u.firstname) LIKE LOWER(CONCAT('%', :keyword, '%'))
                OR LOWER(u.lastname) LIKE LOWER(CONCAT('%', :keyword, '%'))
                OR LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%'))
            )
            AND u.id <> :excludedId
            """)
    Page<ChatUser> searchUsers(@Param("excludedId") Long excludedId, @Param("keyword") String keyword, Pageable pageable);
}
