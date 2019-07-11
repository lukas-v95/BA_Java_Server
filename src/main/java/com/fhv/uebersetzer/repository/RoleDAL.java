package com.fhv.uebersetzer.repository;

import com.fhv.uebersetzer.model.Role;
import com.fhv.uebersetzer.model.RoleName;

import java.util.Optional;

public interface RoleDAL {
    Optional<Role> findByName(RoleName roleName);
}
