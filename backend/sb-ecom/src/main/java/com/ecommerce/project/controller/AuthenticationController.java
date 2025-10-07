package com.ecommerce.project.controller;

import com.ecommerce.project.model.AppRole;
import com.ecommerce.project.model.Roles;
import com.ecommerce.project.model.User;
import com.ecommerce.project.repository.RoleRepository;
import com.ecommerce.project.repository.UserRepository;
import com.ecommerce.project.security.jwt.JwtUtils;
import com.ecommerce.project.security.jwt.LoginRequest;
import com.ecommerce.project.security.jwt.UserInfoResponse;
import com.ecommerce.project.security.request.SignupRequest;
import com.ecommerce.project.security.response.MessageResponse;
import com.ecommerce.project.security.service.UserDetailsImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private UserRepository userRepository;

    private PasswordEncoder encoder;

    private RoleRepository roleRepository;

    @PostMapping("/signin")
    private ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        Authentication authenticate;
        try {
            authenticate = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "Bad Request");
            errorResponse.put("status", false);
            return ResponseEntity.badRequest().body(errorResponse);
        }
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        UserDetailsImpl userDetails = (UserDetailsImpl) authenticate.getPrincipal();
        String jwtToken = jwtUtils.generateJwtToken(userDetails);
        List<String> roles = userDetails.getAuthorities().stream().map(role -> role.getAuthority()).toList();
        UserInfoResponse userInfo = new UserInfoResponse(userDetails.getUserId(), jwtToken, userDetails.getUsername(), roles
        );

        return ResponseEntity.ok(userInfo);
    }

    @PostMapping("/signUp")
    ResponseEntity<?> signUpUser(@Valid @RequestBody SignupRequest signupRequest) {

        if (userRepository.existByUsername(signupRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("username already exits"));
        }

        if (userRepository.exitsByEmail(signupRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("username already exits"));
        }
        User user = new User(
                signupRequest.getUsername(),
                signupRequest.getEmail(),
                encoder.encode(signupRequest.getPassword())
        );

        Set<String> userRoles = signupRequest.getRoles();

        Set<Roles> roles = new HashSet<>();
        if (userRoles == null) {
            Roles role = roleRepository.findByRoleName(AppRole.ROLE_USER).orElseThrow(() ->
                    new RuntimeException("Error: Role is not found"));
            roles.add(role);
        } else {
            userRoles.forEach(role -> {
                        switch (role) {
                            case "admin":
                                Roles adminRole = roleRepository.findByRoleName(AppRole.ROLE_ADMIN).orElseThrow(() ->
                                        new RuntimeException("Error: Role is not found"));
                                roles.add(adminRole);
                                break;

                            case "seller":
                                Roles sellerRole = roleRepository.findByRoleName(AppRole.ROLE_SELLER).orElseThrow(() ->
                                        new RuntimeException("Error: Role is not found")
                                );
                                roles.add(sellerRole);
                                break;
                            default:
                                Roles userRole = roleRepository.findByRoleName(AppRole.ROLE_USER).orElseThrow(() ->
                                        new RuntimeException("Error: Role is not found"));

                                roles.add(userRole);
                        }
                    }

            );
        }
        user.setRoles(roles);
        userRepository.save(user);
        return ResponseEntity.ok().body(new MessageResponse("User Registered Successfully"));
    }
}
