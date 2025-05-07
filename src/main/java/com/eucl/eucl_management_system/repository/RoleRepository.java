package com.eucl.eucl_management_system.repository;

import com.eucl.eucl_management_system.entity.Erole;
import com.eucl.eucl_management_system.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long>{
    Optional<Role> findByName(Erole name);
}
