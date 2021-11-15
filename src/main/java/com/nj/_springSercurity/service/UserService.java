package com.nj._springSercurity.service;

import com.nj._springSercurity.domain.Role;
import com.nj._springSercurity.domain.User;

import java.util.List;

public interface UserService {

     User saveUser(User user);
     Role saveRole(Role role);
     void addRoleToUser(String username, String role);
     User getUser(String username);
     List<User> getUsers();
}
