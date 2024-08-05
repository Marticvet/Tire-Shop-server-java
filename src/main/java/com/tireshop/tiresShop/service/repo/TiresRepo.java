package com.tireshop.tiresShop.service.repo;

import org.springframework.stereotype.Repository;

import com.tireshop.tiresShop.service.model.Manufacturers;
import com.tireshop.tiresShop.service.model.Models;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

@Repository
public class TiresRepo {

    private JdbcTemplate jdbc;

    public JdbcTemplate getJdbc() {
        return jdbc;
    }

    @Autowired
    public void setJdbc(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<Manufacturers> getAllTiresManufacturers() {

        String sql = "SELECT * FROM manufacturer";

        RowMapper<Manufacturers> mapper = (rs, rowNum) -> {

            Manufacturers t = new Manufacturers();
            t.setId(rs.getInt("id"));
            t.setName(rs.getString("name"));
            t.setImage_url(rs.getString("image_url"));

            return t;

        };

        return jdbc.query(sql, mapper);
    }

    public Manufacturers getTireManufacturerByName(String manufacturerName) {

        String sql = "SELECT * FROM manufacturer where name = ?";

        RowMapper<Manufacturers> mapper = (rs, rowNum) -> {

            Manufacturers manufacturer = new Manufacturers();
            manufacturer.setId(rs.getInt("id"));
            manufacturer.setName(rs.getString("name"));
            manufacturer.setImage_url(rs.getString("image_url"));

            return manufacturer;
        };

        return jdbc.queryForObject(sql, mapper, manufacturerName);
    }

    public List<Models> getAllModelsByManufacturerName(String manufacturerName) {
        String sql = """
                SELECT
                    t.id,
                    t.model_id AS tire_model_id,
                    t.loudness_level AS tire_loudness_level,
                    t.price AS tire_price,
                    t.quantity AS tire_quantity,
                    t.load_index AS tire_load_index,
                    t.speed_rating AS tire_speed_rating,
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
                    tire AS t
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
                WHERE
                    m.name = ?
                GROUP BY
                    tire_model_id
                """;

        RowMapper<Models> mapper = (rs, rowNum) -> {
            Models model = new Models();

            model.setId(rs.getInt("id"));
            model.setTireLoudnessLevel(rs.getString("tire_loudness_level"));
            model.setTirePrice(rs.getDouble("tire_price"));
            model.setTireQuantity(rs.getInt("tire_quantity"));
            model.setTireLoadIndex(rs.getInt("tire_load_index"));
            model.setTireSpeedRating(rs.getString("tire_speed_rating"));
            model.setTireSeason(rs.getString("tire_season"));
            model.setModelName(rs.getString("model_name"));
            model.setModelImageUrl(rs.getString("model_imageUrl"));
            model.setModelDescription(rs.getString("model_description"));
            model.setManufacturerName(rs.getString("manufacturer_name"));
            model.setDimensionWidth(rs.getInt("dimention_width"));
            model.setDimensionHeight(rs.getInt("dimention_height"));
            model.setDimensionDiameter(rs.getInt("dimention_diameter"));
            model.setFuelEfficiency(rs.getString("fuel_efficiency"));
            model.setGripRating(rs.getString("grip_rating"));
            model.setCarType(rs.getString("car_type"));
            model.setTireModelId(rs.getInt("tire_model_id"));

            return model;
        };

        return jdbc.query(sql, mapper, manufacturerName);
    }

    public List<Models> getManufacturerModelByTireId(String manufacturerName, String tireId) {
        String sql = """
                SELECT
                t.id,
                t.model_id as tire_model_id,
                t.loudness_level as tire_loudness_level,
                t.price as tire_price,
                t.quantity as tire_quantity,
                t.load_index as tire_load_index,
                t.speed_rating as tire_speed_rating,
                s.season as tire_season,
                tm.name as model_name,
                tm.image_url as model_imageUrl,
                tm.description as model_description,
                m.name as manufacturer_name,
                d.width as dimention_width,
                d.height as dimention_height,
                d.diameter as dimention_diameter,
                fe.class as fuel_efficiency,
                g.rating as grip_rating,
                ct.name as car_type
                FROM
                tire AS t
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
                left join
                car_type as ct on ct.id = t.car_type
                where m.name = ? and t.id = ?
                    """;

        RowMapper<Models> mapper = (rs, rowNum) -> {
            Models model = new Models();

            model.setId(rs.getInt("id"));
            model.setTireLoudnessLevel(rs.getString("tire_loudness_level"));
            model.setTirePrice(rs.getDouble("tire_price"));
            model.setTireQuantity(rs.getInt("tire_quantity"));
            model.setTireLoadIndex(rs.getInt("tire_load_index"));
            model.setTireSpeedRating(rs.getString("tire_speed_rating"));
            model.setTireSeason(rs.getString("tire_season"));
            model.setModelName(rs.getString("model_name"));
            model.setModelImageUrl(rs.getString("model_imageUrl"));
            model.setModelDescription(rs.getString("model_description"));
            model.setManufacturerName(rs.getString("manufacturer_name"));
            model.setDimensionWidth(rs.getInt("dimention_width"));
            model.setDimensionHeight(rs.getInt("dimention_height"));
            model.setDimensionDiameter(rs.getInt("dimention_diameter"));
            model.setFuelEfficiency(rs.getString("fuel_efficiency"));
            model.setGripRating(rs.getString("grip_rating"));
            model.setCarType(rs.getString("car_type"));
            model.setTireModelId(rs.getInt("tire_model_id"));

            return model;
        };

        return jdbc.query(sql, mapper, manufacturerName, tireId);
    }

}