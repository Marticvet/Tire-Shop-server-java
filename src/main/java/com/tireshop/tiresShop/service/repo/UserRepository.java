package com.tireshop.tiresShop.service.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.tireshop.tiresShop.service.dto.LoginDto;
import com.tireshop.tiresShop.service.dto.RegisterDto;
import com.tireshop.tiresShop.service.model.UserEntity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {
    // Optional<UserEntity> findByUsername(String username);

    // Boolean existsByUsername(String username);

    private JdbcTemplate jdbc;

    public JdbcTemplate getJdbc() {
        return jdbc;
    }

    @Autowired
    public void setJdbc(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public Boolean existsByUsername(String username) {

        String sql = "SELECT * from user where email = ?";

        return jdbc.queryForList(sql, username).size() == 0 ? true : false;
    }

    public ResponseEntity<String> registerUser(UserEntity user) {
        String firstName = user.getFirstName();
        String lastName = user.getLastName();
        String email = user.getUsername();
        String password = user.getPassword();

        String sql = """
                INSERT INTO user (first_name, last_name, email, password)
                VALUES (?, ?, ?, ?)
                """;

        try {
            int rowsAffected = jdbc.update(sql, firstName, lastName, email, password);
            if (rowsAffected > 0) {
                return new ResponseEntity<>("User registered successfully!", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Something went wrong!", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<String> loginUser(UserEntity user) {
        String firstName = user.getFirstName();
        String lastName = user.getLastName();
        String email = user.getUsername();
        String password = user.getPassword();

        String sql = """
                INSERT INTO user (first_name, last_name, email, password)
                VALUES (?, ?, ?, ?)
                """;

        try {
            int rowsAffected = jdbc.update(sql, firstName, lastName, email, password);
            if (rowsAffected > 0) {
                return new ResponseEntity<>("User registered successfully!", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Something went wrong!", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public Optional<UserEntity> findByEmail(String username) {
        String sql = "SELECT * FROM user WHERE email = ?";

        try {
            RowMapper<UserEntity> mapper = (rs, rowNum) -> {
                UserEntity user = new UserEntity();

                user.setUsername(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                return user;
            };

            UserEntity user = jdbc.queryForObject(sql, mapper, username);
            return Optional.ofNullable(user);

        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
