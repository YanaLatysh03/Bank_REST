package com.example.bankcards.repository;

import com.example.bankcards.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    @Query("""
        select u from User u
        where (:name is null or u.name = :name)
        and (:email is null or u.email = :email)
    """)
    Page<User> findAllByFilter(
            @Param("name") String name,
            @Param("email") String email,
            Pageable pageable
    );
}
