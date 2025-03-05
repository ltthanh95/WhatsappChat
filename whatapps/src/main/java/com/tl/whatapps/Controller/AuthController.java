package com.tl.whatapps.Controller;

import com.tl.whatapps.Config.TokenProvider;
import com.tl.whatapps.DTO.Request.LoginRequest;
import com.tl.whatapps.DTO.Response.AuthResponse;
import com.tl.whatapps.Entity.User;
import com.tl.whatapps.Exception.UserException;
import com.tl.whatapps.Repository.UserRepository;
import com.tl.whatapps.Service.CustomUserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private CustomUserService customUserService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/signup")

    public ResponseEntity<AuthResponse> createUserHandler(@RequestBody User user) throws UserException {
        String email = user.getEmail();
        String name = user.getName();
        String password = user.getPassword();

        // Check if email is already used
        if (userRepository.findByEmail(email) != null) {
            throw new UserException("Email is already used with another account");
        }

        // Hash the password before saving
        User createdUser = new User();
        createdUser.setEmail(email);
        createdUser.setName(name);
        createdUser.setPassword(passwordEncoder.encode(password));

        userRepository.save(createdUser);

        // No need to authenticate during sign-up
        String jwt = tokenProvider.generateToken(createdUser.getEmail());

        return new ResponseEntity<>(new AuthResponse("Bearer " + jwt, true), HttpStatus.CREATED);
    }



    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> loginHandler(@RequestBody LoginRequest request) {
        String email = request.getEmail();
        String password = request.getPassword();

        // Check if the user exists before authentication
        User existingUser = userRepository.findByEmail(email);
        if (existingUser == null) {
            throw new BadCredentialsException("User not found");
        }

        Authentication authentication = this.authenticate(email, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(email);

        return new ResponseEntity<>(new AuthResponse(jwt, true), HttpStatus.OK);
    }



    public Authentication authenticate(String username, String password) {
        try {
            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Invalid username or password");
        }
    }
}
