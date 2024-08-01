package nl.rgs.kib.service;

import nl.rgs.kib.model.user.User;
import nl.rgs.kib.model.user.dto.CreateUser;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> findAll();

    Optional<User> findById(String id);

    User create(CreateUser createUser);

    Optional<User> update(User user);

    Optional<User> deleteById(String id);

    Boolean emailExists(String email);

    Boolean usernameExists(String username);
}
