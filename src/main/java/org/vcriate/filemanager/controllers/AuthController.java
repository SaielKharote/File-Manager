package org.vcriate.filemanager.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.vcriate.filemanager.config.JwtGenerator;
import org.vcriate.filemanager.entities.User;
import org.vcriate.filemanager.exceptions.UserException;
import org.vcriate.filemanager.repositories.UserRepository;
import org.vcriate.filemanager.requests.LoginRequest;
import org.vcriate.filemanager.requests.SignupRequest;
import org.vcriate.filemanager.responses.JwtResponse;
import org.vcriate.filemanager.services.CustomUserDetailsService;

@RestController
@RequestMapping("/fm/auth")
public class AuthController {
    @Autowired
    private JwtGenerator jwtGenerator;
    @Autowired
    private CustomUserDetailsService userDetailsService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @PostMapping("/signup")
    public ResponseEntity<JwtResponse> signupUser(@RequestBody SignupRequest req) throws UserException {
        if (userRepository.findByEmail(req.getEmail()) != null) {
            throw new UserException("User with email " + req.getEmail() + " already exists");
        }
        String email = req.getEmail();
        String password = req.getPassword();
        String username = req.getUsername();

        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setUsername(username);
        User savedUser = userRepository.save(user);

        Authentication authentication = authenticate(email, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtGenerator.generateJwtToken(authentication);
        JwtResponse jwtResponse = new JwtResponse();
        jwtResponse.setJwt(jwt);
        jwtResponse.setAuthenticated(true);
        jwtResponse.setError(false);
        jwtResponse.setErrorDetails(null);
        jwtResponse.setMessage(username + " signed up successfully");

        return new ResponseEntity<>(jwtResponse, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> loginUser(@RequestBody LoginRequest req) throws UserException {
        UserDetails userDetails = userDetailsService.loadUserByUsername(req.getEmail());
        if (userDetails == null) {
            throw new UserException("User with email " + req.getEmail() + " not found!!");
        }

        String email = req.getEmail();
        String password = req.getPassword();
        Authentication authentication = authenticate(email, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtGenerator.generateJwtToken(authentication);
        JwtResponse jwtResponse = new JwtResponse();
        jwtResponse.setJwt(jwt);
        jwtResponse.setAuthenticated(true);
        jwtResponse.setError(false);
        jwtResponse.setErrorDetails(null);
        jwtResponse.setMessage("Logged in successfully!!");

        return new ResponseEntity<>(jwtResponse, HttpStatus.ACCEPTED);
    }

    private Authentication authenticate(String email, String password) throws UserException {
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        if (userDetails == null) {
            throw new UserException("User with email " + email + " not found (authenticate)");
        }
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new UserException("Invalid password (authenticate)");
        }

        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }


}
