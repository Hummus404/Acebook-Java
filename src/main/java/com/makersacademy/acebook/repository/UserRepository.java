package com.makersacademy.acebook.repository;

import com.makersacademy.acebook.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    public Optional<User> findUserByUsername(String username);
    public Optional<User> findUserByEmailAddress(String emailAddress);
//    I NEED THE BELOW FOR THE SEARCH RESULTS UMUT >
    @Query(
            value = """
               SELECT *
               FROM users
               WHERE LOWER(username) LIKE LOWER(CONCAT('%', :search, '%'))
                OR LOWER(first_name) LIKE LOWER(CONCAT('%', :search, '%'))
                OR LOWER(surname) LIKE LOWER(CONCAT('%', :search, '%'))
            """,
            nativeQuery = true
    )
    public List<User> searchUsers(@Param("search") String search);
//    UMUT <
}
