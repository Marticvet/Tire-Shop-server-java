package com.tireshop.tiresShop.service.repo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.RowMapper;

import com.tireshop.tiresShop.service.model.Models;

@Repository
public class SearchRepo {

    private JdbcTemplate jdbc;

    public JdbcTemplate getJdbc() {
        return jdbc;
    }

    @Autowired
    public void setJdbc(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<Models> searchedBy(String manufacturer, String modelName) {
        String sql = """
                    SELECT t.id,
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
                where tm.name like ?
                    group by tire_model_id
                                            """;
        // Adding wildcards to the modelName and manufacturer parameters
        modelName = "%" + modelName + "%";

        System.out.println(modelName + " ");

        RowMapper<Models> mapper = (rs, rowNum) -> {

            System.out.println(rowNum + " rowNum");
            Models model = new Models();

            model.setId(rs.getInt("id"));
            model.setModelId(rs.getInt("tire_model_id"));
            model.setTireLoudnessLevel(rs.getString("tire_loudness_level"));
            model.setTirePrice(rs.getDouble("tire_price"));
            model.setTireQuantity(rs.getInt("tire_quantity"));
            model.setTireLoadIndex(rs.getInt("tire_load_index"));
            model.setTireSpeedRating(rs.getString("tire_speed_rating"));
            model.setTireSeason(rs.getString("tire_season"));
            model.setModelName(rs.getString("model_name"));
            model.setModelImageUrl(rs.getString("model_imageUrl"));
            model.setManufacturerName(rs.getString("manufacturer_name"));
            model.setDimensionWidth(rs.getInt("dimention_width"));
            model.setDimensionHeight(rs.getInt("dimention_height"));
            model.setDimensionDiameter(rs.getInt("dimention_diameter"));
            model.setFuelEfficiency(rs.getString("fuel_efficiency"));
            model.setGripRating(rs.getString("grip_rating"));
            model.setCarType(rs.getString("car_type"));
            model.setTireModelId(rs.getInt("tire_model_id"));
            model.setModelDescription(rs.getString("model_description"));

            return model;
        };

        System.out.println(mapper);
        return jdbc.query(sql, mapper, modelName);
    }

}
