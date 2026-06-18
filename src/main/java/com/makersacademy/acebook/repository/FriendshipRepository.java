package com.makersacademy.acebook.repository;

import com.makersacademy.acebook.model.Friendship;
import com.makersacademy.acebook.model.FriendshipStatus;
import com.makersacademy.acebook.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface FriendshipRepository extends CrudRepository<Friendship, Long> {
    Optional<Friendship> findByRequesterAndAddressee(User me, User profileUser);
    List<Friendship> findByAddresseeAndStatus(User addressee, FriendshipStatus status);
    List<Friendship> findByRequesterAndStatus(User requester, FriendshipStatus status);
}
