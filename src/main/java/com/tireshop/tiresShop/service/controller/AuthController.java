package com.tireshop.tiresShop.service.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tireshop.tiresShop.service.dto.AuthResponseDTO;
import com.tireshop.tiresShop.service.dto.LoginDto;
import com.tireshop.tiresShop.service.dto.RegisterDto;
import com.tireshop.tiresShop.service.model.UserEntity;
import com.tireshop.tiresShop.service.model.UsersCartItems;
import com.tireshop.tiresShop.service.repo.UserRepo;
import com.tireshop.tiresShop.service.security.JWTGenerator;
import com.tireshop.tiresShop.service.service.UserService;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;

@RestController
@RequestMapping("/users")
public class AuthController {

    @Autowired
    private UserService service;

    private AuthenticationManager authenticationManager;
    private UserRepo userRepository;
    private PasswordEncoder passwordEncoder;
    private JWTGenerator jwtGenerator;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, UserRepo userRepository,
            PasswordEncoder passwordEncoder, JWTGenerator jwtGenerator) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtGenerator = jwtGenerator;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterDto registerDto) {

        // Check if the user already exists
        if (userRepository.existsByUsername(registerDto.getUsername()) == false) {
            // If user exists, return a meaningful response
            return new ResponseEntity<>("Username is already taken!", HttpStatus.BAD_REQUEST);
        }

        // Create a new UserEntity object
        UserEntity user = new UserEntity();
        user.setUsername(registerDto.getUsername());
        user.setFirstName(registerDto.getFirstName());
        user.setLastName(registerDto.getLastName());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));

        // Register the user
        userRepository.registerUser(user);

        try {
            // Authenticate the user
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            registerDto.getUsername(),
                            registerDto.getPassword()));

            // Set the authentication in the security context
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Generate JWT token
            String token = jwtGenerator.generateToken(authentication);

            // Fetch the user details again to ensure they are in the database
            Optional<UserEntity> userOptional = userRepository.findByEmail(registerDto.getUsername());

            if (userOptional.isPresent()) {
                UserEntity registeredUser = userOptional.get();

                // Return the response entity with token and user details
                return new ResponseEntity<>(
                        new AuthResponseDTO(token, registeredUser.getUserId(), registeredUser.getUsername(),
                                registeredUser.getFirstName(),
                                registeredUser.getLastName()),
                        HttpStatus.OK);
            } else {
                // If user details are not found, return an error response
                return new ResponseEntity<>("User not found after registration!", HttpStatus.UNAUTHORIZED);
            }
        } catch (BadCredentialsException e) {
            // Handle incorrect credentials
            return new ResponseEntity<>("Invalid credentials!", HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            // Handle other exceptions
            e.printStackTrace();
            return new ResponseEntity<>("An error occurred during authentication.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginDto loginDto) {
        try {
            // Authenticate the user
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDto.getUsername(),
                            loginDto.getPassword()));

            // Set the authentication in the security context
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Generate JWT token
            String token = jwtGenerator.generateToken(authentication);

            // Find the user details
            Optional<UserEntity> userOptional = userRepository.findByEmail(loginDto.getUsername());

            if (userOptional.isPresent()) {
                UserEntity user = userOptional.get();

                // Return the response entity with token and user details
                return new ResponseEntity<>(
                        new AuthResponseDTO(token, user.getUserId(), user.getUsername(), user.getFirstName(),
                                user.getLastName()),
                        HttpStatus.OK);
            } else {
                // If user details are not found, return an error response
                return new ResponseEntity<>("User not found!", HttpStatus.UNAUTHORIZED);
            }
        } catch (BadCredentialsException e) {
            // Handle incorrect credentials
            return new ResponseEntity<>("Email or password is incorrect!", HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            // Handle other exceptions
            e.printStackTrace();
            return new ResponseEntity<>("An error occurred during authentication.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/shoppingCart/{userId}")
    public ResponseEntity<?> getUserCartItems(@PathVariable int userId, @RequestHeader("Authorization") String token) {
        try {
            // Remove the "Bearer " prefix from the token
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }

            // Validate the token
            if (!jwtGenerator.validateToken(token)) {
                return new ResponseEntity<>("Invalid token!", HttpStatus.UNAUTHORIZED);
            }

            // Extract username from the token
            String username = jwtGenerator.getUsernameFromJWT(token);

            // Get the user details
            Optional<UserEntity> userOptional = userRepository.findByEmail(username);

            System.out.println(userOptional.isPresent() + " userOptional.isPresent()");
            if (userOptional.isPresent()) {
                UserEntity user = userOptional.get();

                // Check if the user ID matches
                if (user.getUserId() == userId) {
                    // Fetch the user's shopping cart items (this is just a placeholder)
                    // You should implement the actual logic to get the shopping cart items
                    List<UsersCartItems> usersCartItems = service.getUserCartItems(userId);

                    // Return the shopping cart items
                    return new ResponseEntity<>(usersCartItems, HttpStatus.OK);
                } else {
                    // If user ID doesn't match, return an unauthorized response
                    return new ResponseEntity<>("User ID does not match!", HttpStatus.UNAUTHORIZED);
                }
            } else {
                // If user details are not found, return an error response
                return new ResponseEntity<>("User not found!", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            // Handle other exceptions
            e.printStackTrace();
            return new ResponseEntity<>("An error occurred while processing the request.",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
