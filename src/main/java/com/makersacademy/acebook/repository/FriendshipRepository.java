package com.makersacademy.acebook.repository;

import com.makersacademy.acebook.model.Friendship;
import com.makersacademy.acebook.model.FriendshipStatus;
import com.makersacademy.acebook.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface FriendshipRepository extends CrudRepository<Friendship, Long> {
    Optional<Friendship> findByRequestorAndAddressee(User Requestor, User Addressee);
    List<Friendship> findByAddresseeAndStatus(User Addressee, FriendshipStatus status);
    List<Friendship> findByRequestorAndStatus(User Requestor, FriendshipStatus status);
}
