package com.nj._springSercurity.controller;


import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nj._springSercurity.domain.Role;
import com.nj._springSercurity.domain.User;
import com.nj._springSercurity.dto.AddRoleToUser;
import com.nj._springSercurity.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import com.auth0.jwt.JWT;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    @RequestMapping("/user")
    public ResponseEntity<User> addUser(@RequestBody User user){
        User addedUser = userService.saveUser(user);
        return new ResponseEntity<User>(addedUser,HttpStatus.CREATED);
    }

    @PostMapping
    @RequestMapping("/role")
    public ResponseEntity<Role> addRole(@RequestBody Role role){
        Role addedRole = userService.saveRole(role);
        return new ResponseEntity<Role>(addedRole,HttpStatus.CREATED);
    }

    @PutMapping
    @RequestMapping("/roletouser")
    public ResponseEntity<String> addRole(@RequestBody AddRoleToUser addRoleToUser){
        userService.addRoleToUser(addRoleToUser.getUserName(), addRoleToUser.getRoleName());
        return new ResponseEntity<String>("Role added to the User",HttpStatus.OK);
    }

    @GetMapping
    @RequestMapping("/user/{userName}")
    public ResponseEntity<User> getUser(@PathVariable String userName){
        User user = userService.getUser(userName);
        return new ResponseEntity<User>(user,HttpStatus.OK);
    }

    @GetMapping
    @RequestMapping("/users")
    public ResponseEntity<List<User>> getUser(){
        List<User> users = userService.getUsers();
        return new ResponseEntity<List<User>>(users,HttpStatus.OK);
    }

    @GetMapping("/token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                String refresh_token = authorizationHeader.substring("Bearer ".length());
                Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(refresh_token);
                String username = decodedJWT.getSubject();
                User user = userService.getUser(username);
                String access_token = JWT.create()
                        .withSubject(user.getUsername())
                        .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
                        .withIssuer(request.getRequestURL().toString())
                        .withClaim("roles", user.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
                        .sign(algorithm);
                Map<String, String> tokens = new HashMap<>();
                tokens.put("access_token", access_token);
                tokens.put("refresh_token", refresh_token);
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), tokens);
            }catch (Exception exception) {
                response.setHeader("error", exception.getMessage());
                response.setStatus(FORBIDDEN.value());
                //response.sendError(FORBIDDEN.value());
                Map<String, String> error = new HashMap<>();
                error.put("error_message", exception.getMessage());
                response.setContentType(MimeTypeUtils.APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), error);
            }
        } else {
            throw new RuntimeException("Refresh token is missing");
        }
    }
}
