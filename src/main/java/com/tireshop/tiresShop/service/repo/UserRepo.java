package com.tireshop.tiresShop.service.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.tireshop.tiresShop.service.dto.UpdateDto;
import com.tireshop.tiresShop.service.model.UserEntity;
import com.tireshop.tiresShop.service.model.UsersCartItems;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepo {

    private JdbcTemplate jdbcTemplate;

    public JdbcTemplate getJdbc() {
        return jdbcTemplate;
    }

    @Autowired
    public void setJdbc(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Boolean existsByUsername(String username) {

        String sql = "SELECT * from user where email = ?";

        return jdbcTemplate.queryForList(sql, username).size() == 0 ? true : false;
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
            int rowsAffected = jdbcTemplate.update(sql, firstName, lastName, email, password);
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
            int rowsAffected = jdbcTemplate.update(sql, firstName, lastName, email, password);
            if (rowsAffected > 0) {
                return new ResponseEntity<>("User Logged in successfully!", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Something went wrong!", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public Optional<UserEntity> findByEmail(String email) {
        String sql = "SELECT * FROM user WHERE email = ?";

        try {
            RowMapper<UserEntity> mapper = (rs, rowNum) -> {
                UserEntity user = new UserEntity();
                user.setUserId(rs.getLong("user_id"));
                user.setUsername(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setFirstName(rs.getString("first_name"));
                user.setLastName(rs.getString("last_name"));
                return user;
            };

            List<UserEntity> users = jdbcTemplate.query(sql, mapper, email);
            if (users.isEmpty()) {
                return Optional.empty();
            } else {
                return Optional.of(users.get(0));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public List<UsersCartItems> getUserCartItems(int userId) {
        String sql = """
                    SELECT
                    user_basket.id,
                    user_basket.user_id,
                    user_basket.quantity,
                    user_basket.tire_id,
                    user.email,
                    user.first_name,
                    user.last_name,
                    t.id AS tire_id,
                    t.loudness_level AS tire_loudness_level,
                    t.price AS tire_price,
                    t.quantity AS tire_quantity,
                    t.load_index AS tire_load_index,
                    t.speed_rating AS tire_speed_rating,
                    t.model_id AS tire_model_id,
                    s.season AS tire_season,
                    tm.name AS model_name,
                    tm.image_url AS model_imageUrl,
                    tm.description AS model_description,
                    m.name AS manufacturer_name,
                    d.width AS dimention_width,
                    d.height AS dimention_height,
                    d.diameter AS dimention_diameter,
                    fe.class AS fuel_efficiency,
                    g.rating AS grip_rating,
                    ct.name AS car_type
                FROM
                    user_basket AS user_basket
                        LEFT JOIN
                    user ON user.user_id = user_basket.user_id
                        LEFT JOIN
                    tire AS t ON t.id = user_basket.tire_id
                        LEFT JOIN
                    season AS s ON s.id = t.season_id
                        LEFT JOIN
                    model AS tm ON tm.id = t.model_id
                        LEFT JOIN
                    manufacturer AS m ON m.id = tm.manufacturer_id
                        LEFT JOIN
                    dimension AS d ON d.id = t.dimention_id
                        LEFT JOIN
                    fuel_efficiency AS fe ON fe.id = t.fuel_efficiency_id
                        LEFT JOIN
                    grip AS g ON g.id = t.wet_traction_rating
                        LEFT JOIN
                    car_type AS ct ON ct.id = t.car_type

                    where user.user_id = ?
                                """;

        RowMapper<UsersCartItems> mapper = (rs, rowNum) -> {
            UsersCartItems item = new UsersCartItems();

            item.setId(rs.getInt("id"));
            item.setTireLoudnessLevel(rs.getInt("tire_loudness_level"));
            item.setTirePrice(rs.getDouble("tire_price"));
            item.setTireQuantity(rs.getInt("tire_quantity"));
            item.setTireLoadIndex(rs.getInt("tire_load_index"));
            item.setTireSpeedRating(rs.getString("tire_speed_rating"));
            item.setTireSeason(rs.getString("tire_season"));
            item.setModelName(rs.getString("model_name"));
            item.setModelImageUrl(rs.getString("model_imageUrl"));
            item.setModelDescription(rs.getString("model_description"));
            item.setManufacturerName(rs.getString("manufacturer_name"));
            item.setDimensionWidth(rs.getInt("dimention_width"));
            item.setDimensionHeight(rs.getInt("dimention_height"));
            item.setDimensionDiameter(rs.getInt("dimention_diameter"));
            item.setFuelEfficiency(rs.getString("fuel_efficiency"));
            item.setGripRating(rs.getString("grip_rating"));
            item.setCarType(rs.getString("car_type"));
            item.setEmail(rs.getString("email"));
            item.setFirstName(rs.getString("first_name"));
            item.setLastName(rs.getString("last_name"));
            item.setUserId(rs.getInt("user_id"));
            item.setQuantity(rs.getInt("quantity"));
            item.setTireId(rs.getInt("tire_id"));
            item.setTireModelId(rs.getInt("tire_model_id"));

            return item;
        };

        return jdbcTemplate.query(sql, mapper, userId);
    }

    public ResponseEntity<String> addItemInShoppingCart(int userId, int quantity, int tireId) {
        String sql = "INSERT INTO user_basket (user_id, quantity, tire_id) VALUES (?, ?, ?)";

        int rowsAffected = jdbcTemplate.update(sql, userId, quantity, tireId);

        try {
            if (rowsAffected > 0) {
                return new ResponseEntity<>("The item has been successfully added!", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Something went wrong!", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public List<UsersCartItems> getUserCartItemById(int id) {
        String sql = """
                    SELECT
                    user_basket.id,
                    user_basket.user_id,
                    user_basket.quantity,
                    user_basket.tire_id,
                    user.email,
                    user.first_name,
                    user.last_name,
                    t.id AS tire_id,
                    t.loudness_level AS tire_loudness_level,
                    t.price AS tire_price,
                    t.quantity AS tire_quantity,
                    t.load_index AS tire_load_index,
                    t.speed_rating AS tire_speed_rating,
                    t.model_id AS tire_model_id,
                    s.season AS tire_season,
                    tm.name AS model_name,
                    tm.image_url AS model_imageUrl,
                    tm.description AS model_description,
                    m.name AS manufacturer_name,
                    d.width AS dimention_width,
                    d.height AS dimention_height,
                    d.diameter AS dimention_diameter,
                    fe.class AS fuel_efficiency,
                    g.rating AS grip_rating,
                    ct.name AS car_type
                FROM
                    user_basket AS user_basket
                        LEFT JOIN
                    user ON user.user_id = user_basket.user_id
                        LEFT JOIN
                    tire AS t ON t.id = user_basket.tire_id
                        LEFT JOIN
                    season AS s ON s.id = t.season_id
                        LEFT JOIN
                    model AS tm ON tm.id = t.model_id
                        LEFT JOIN
                    manufacturer AS m ON m.id = tm.manufacturer_id
                        LEFT JOIN
                    dimension AS d ON d.id = t.dimention_id
                        LEFT JOIN
                    fuel_efficiency AS fe ON fe.id = t.fuel_efficiency_id
                        LEFT JOIN
                    grip AS g ON g.id = t.wet_traction_rating
                        LEFT JOIN
                    car_type AS ct ON ct.id = t.car_type

                    where user_basket.id = ?
                        """;

        RowMapper<UsersCartItems> mapper = (rs, rowNum) -> {
            UsersCartItems item = new UsersCartItems();

            item.setId(rs.getInt("id"));
            item.setTireLoudnessLevel(rs.getInt("tire_loudness_level"));
            item.setTirePrice(rs.getDouble("tire_price"));
            item.setTireQuantity(rs.getInt("tire_quantity"));
            item.setTireLoadIndex(rs.getInt("tire_load_index"));
            item.setTireSpeedRating(rs.getString("tire_speed_rating"));
            item.setTireSeason(rs.getString("tire_season"));
            item.setModelName(rs.getString("model_name"));
            item.setModelImageUrl(rs.getString("model_imageUrl"));
            item.setModelDescription(rs.getString("model_description"));
            item.setManufacturerName(rs.getString("manufacturer_name"));
            item.setDimensionWidth(rs.getInt("dimention_width"));
            item.setDimensionHeight(rs.getInt("dimention_height"));
            item.setDimensionDiameter(rs.getInt("dimention_diameter"));
            item.setFuelEfficiency(rs.getString("fuel_efficiency"));
            item.setGripRating(rs.getString("grip_rating"));
            item.setCarType(rs.getString("car_type"));
            item.setEmail(rs.getString("email"));
            item.setFirstName(rs.getString("first_name"));
            item.setLastName(rs.getString("last_name"));
            item.setUserId(rs.getInt("user_id"));
            item.setQuantity(rs.getInt("quantity"));
            item.setTireId(rs.getInt("tire_id"));
            item.setTireModelId(rs.getInt("tire_model_id"));

            return item;
        };

        return jdbcTemplate.query(sql, mapper, id);
    }

    public ResponseEntity<String> updateUser(UpdateDto user) {
        String firstName = user.getFirstName();
        String lastName = user.getLastName();
        String email = user.getUsername();
        String password = user.getPassword();

        String sql = """
                INSERT INTO user (first_name, last_name, email, password)
                VALUES (?, ?, ?, ?)
                """;

        try {
            int rowsAffected = jdbcTemplate.update(sql, firstName, lastName, email, password);
            if (rowsAffected > 0) {
                return new ResponseEntity<>("User Logged in successfully!", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Something went wrong!", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<String> deleteItemInShoppingCart(Long userId, int tireId) {
        // Check if user exists
        String checkUserSql = "SELECT * FROM user WHERE user_id = ?";
        boolean userCount = jdbcTemplate.queryForList(checkUserSql, userId).size() > 0;

        if (userCount == false) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }

        // Delete item from shopping cart
        String deleteSql = "DELETE FROM user_basket WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(deleteSql, tireId);

        if (rowsAffected > 0) {
            return new ResponseEntity<>("Item deleted successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Item not found in shopping cart",
                    HttpStatus.NOT_FOUND);
        }
    }
}
