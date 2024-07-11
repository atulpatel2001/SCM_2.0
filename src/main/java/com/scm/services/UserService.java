package com.scm.services;

import java.util.List;
import java.util.Optional;

import com.scm.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

    User saveUser(User user);

    Optional<User> getUserById(String id);

    Optional<User> updateUser(User user);

    void deleteUser(String id);

    boolean isUserExist(String userId);

    boolean isUserExistByEmail(String email);

    Page<User> getAllUsers(Pageable pageable);

    User getUserByEmail(String email);


    Long countAllUser();
}
