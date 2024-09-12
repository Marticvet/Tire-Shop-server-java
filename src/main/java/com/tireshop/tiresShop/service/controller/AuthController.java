package com.tireshop.tiresShop.service.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tireshop.tiresShop.service.dto.AuthResponseDTO;
import com.tireshop.tiresShop.service.dto.LoginDto;
import com.tireshop.tiresShop.service.dto.RegisterDto;
import com.tireshop.tiresShop.service.dto.UpdateDto;
import com.tireshop.tiresShop.service.model.UserEntity;
import com.tireshop.tiresShop.service.model.UsersCartItems;
import com.tireshop.tiresShop.service.repo.UserRepo;
import com.tireshop.tiresShop.service.security.JWTGenerator;
import com.tireshop.tiresShop.service.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;

@RestController
@RequestMapping("/users")
@CrossOrigin
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
        if (userRepository.existsByUsername(registerDto.getUsername()) == false) {
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

            // Generate JWT token with additional user information
            String token = jwtGenerator.generateToken(authentication, user);

            // Return the response entity with token and user details
            return new ResponseEntity<>(
                    new AuthResponseDTO(token, user.getUserId(), user.getUsername(), user.getFirstName(),
                            user.getLastName()),
                    HttpStatus.OK);
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>("Invalid credentials!", HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
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

            // Find the user details from the database using the email/username
            Optional<UserEntity> userOptional = userRepository.findByEmail(loginDto.getUsername());

            if (userOptional.isPresent()) {
                UserEntity user = userOptional.get(); // Get user details

                // Generate JWT token, passing both authentication and the user entity
                String token = jwtGenerator.generateToken(authentication, user);

                // Return the response entity with the token and user details
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
            return new ResponseEntity<>("Invalid user's token!", HttpStatus.FORBIDDEN);
        }
    }

    @PutMapping("/update/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable int userId, @RequestBody UpdateDto updateDto,
            @RequestHeader("Authorization") String token) {
        try {
            // Remove the "Bearer " prefix from the token
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }

            // Validate the token
            if (!jwtGenerator.validateToken(token)) {
                return new ResponseEntity<>("Invalid token!", HttpStatus.UNAUTHORIZED);
            }

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            updateDto.getUsername(),
                            updateDto.getPassword()));

            // Set the authentication in the security context
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Create a new UserEntity object
            UserEntity oldUser = new UserEntity();
            oldUser.setUsername(updateDto.getUsername());
            oldUser.setFirstName(updateDto.getFirstName());
            oldUser.setLastName(updateDto.getLastName());
            oldUser.setPassword(passwordEncoder.encode(updateDto.getNewPassword()));

            // Get the user details
            Optional<UserEntity> userOptional = userRepository.findByEmail(oldUser.getUsername());

            if (userOptional.isPresent()) {
                UserEntity user = userOptional.get();

                // Generate JWT token
                String newToken = jwtGenerator.generateToken(authentication, user);

                // Call the service to update the user
                ResponseEntity<String> updateResponse = service.updateUser(oldUser);

                // Get status code from the update response
                HttpStatusCode statusCode = updateResponse.getStatusCode();

                // Check if the user ID matches and if the update was successful (200 OK)
                if (user.getUserId() == userId && statusCode.is2xxSuccessful()) {

                    // Return the token and updated user information
                    return new ResponseEntity<>(
                            new AuthResponseDTO(newToken, user.getUserId(), oldUser.getUsername(),
                                    oldUser.getFirstName(),
                                    oldUser.getLastName()),
                            HttpStatus.OK);
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
            return new ResponseEntity<>("Invalid user's token!", HttpStatus.FORBIDDEN);
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
            return new ResponseEntity<>("Invalid user's token!",
                    HttpStatus.FORBIDDEN);
        }
    }

    @DeleteMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "Logged out!";
    }

    @PostMapping("/shoppingCart/{userId}")
    public ResponseEntity<?> addItemInShoppingCart(@PathVariable int userId,
            @RequestHeader("Authorization") String token, @RequestBody UsersCartItems usersCartItems) {
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

            if (userOptional.isPresent()) {
                UserEntity user = userOptional.get();

                // Check if the user ID matches
                if (user.getUserId() == userId) {
                    // Fetch the user's shopping cart items (this is just a placeholder)
                    // You should implement the actual logic to get the shopping cart items
                    ResponseEntity<String> usersCartItem = service.addItemInShoppingCart(usersCartItems.getUserId(),
                            usersCartItems.getQuantity(), usersCartItems.getTireId());

                    // Return the shopping cart items
                    return new ResponseEntity<>(usersCartItem, HttpStatus.OK);
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
            return new ResponseEntity<>("Invalid user's token!",
                    HttpStatus.FORBIDDEN);
        }
    }

    @DeleteMapping("/shoppingCart/{itemId}")
    public ResponseEntity<?> deleteItemInShoppingCart(@PathVariable int itemId,
            @RequestHeader("Authorization") String token) {
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

            if (userOptional.isPresent()) {
                UserEntity user = userOptional.get();

                // Delete the item from the shopping cart
                ResponseEntity<String> response = service.deleteItemInShoppingCart(user.getUserId(),
                        itemId);

                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                // If user details are not found, return an error response
                return new ResponseEntity<>("User not found!", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            // Handle other exceptions
            e.printStackTrace();
            return new ResponseEntity<>("Invalid user's token!",
                    HttpStatus.FORBIDDEN);
        }
    }

    @PutMapping("/shoppingCart/{itemId}")
    public ResponseEntity<?> updateItemInShoppingCart(@PathVariable int itemId,
            @RequestHeader("Authorization") String token, @RequestBody UsersCartItems usersCartItems) {
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

            if (userOptional.isPresent()) {
                UserEntity user = userOptional.get();

                // Update the item's quantity from the shopping cart
                return service.updateItemInShoppingCart(user.getUserId(),
                        itemId, usersCartItems.getQuantity());
            } else {
                // If user details are not found, return an error response
                return new ResponseEntity<>("User not found!", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            // Handle other exceptions
            e.printStackTrace();
            return new ResponseEntity<>("Invalid user's token!",
                    HttpStatus.FORBIDDEN);
        }
    }
}
