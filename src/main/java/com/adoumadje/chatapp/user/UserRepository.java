package com.adoumadje.chatapp.user;

import com.adoumadje.chatapp.user.entity.ChatUser;
import org.apache.catalina.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<ChatUser, Long> {
    Optional<ChatUser> findByEmail(String email);

    Page<ChatUser> findByIdNot(Long id, Pageable pageable);
}
