package com.nj._springSercurity.repo;

import com.nj._springSercurity.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User,Long> {

    public User findByUsername(String username);
}
