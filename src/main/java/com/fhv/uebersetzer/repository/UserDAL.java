package com.fhv.uebersetzer.repository;

import com.fhv.uebersetzer.model.User;


import java.util.Optional;

public interface UserDAL {
    Optional<User> findByUsername(String username);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
    void save(User user);
}
