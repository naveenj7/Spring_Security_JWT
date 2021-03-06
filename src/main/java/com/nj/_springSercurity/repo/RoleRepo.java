package com.nj._springSercurity.repo;

import com.nj._springSercurity.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepo extends JpaRepository<Role, Long> {

    public Role findByName(String role);


}
