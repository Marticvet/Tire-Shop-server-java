package com.tireshop.tiresShop.service.repo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.tireshop.tiresShop.service.model.Models;
import com.tireshop.tiresShop.service.model.Sizes;

@Repository
public class SizesRepo {

    private JdbcTemplate jdbc;

    public JdbcTemplate getJdbc() {
        return jdbc;
    }

    @Autowired
    public void setJdbc(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<Sizes> getSizesByModelId(String modelId) {

        String sql = """
                        SELECT
                        t.id,
                        t.load_index,
                        t.speed_rating,
                        tm.id as tire_model_id,
                        t.quantity as tire_quantity,
                        t.dimention_id as tire_dimension_id,
                        d.width as dimension_width,
                        d.height as dimension_height,
                        d.diameter as dimension_diameter
                    FROM
                        tire AS t
                            LEFT JOIN
                        model AS tm ON tm.id = t.model_id
                            LEFT JOIN
                        manufacturer AS m ON m.id = tm.manufacturer_id
                            LEFT JOIN
                        dimension AS d ON d.id = t.dimention_id
                where tm.id = ?
                            """;

        RowMapper<Sizes> mapper = (rs, rowNum) -> {
            Sizes size = new Sizes();

            size.setId(rs.getInt("id"));
            size.setLoadIndex(rs.getString("load_index"));
            size.setSpeedRating(rs.getString("speed_rating"));
            size.setTireModelId(rs.getInt("tire_model_id"));
            size.setTireQuantity(rs.getInt("tire_quantity"));
            size.setTireDimensionId(rs.getInt("tire_dimension_id"));
            size.setDimensionWidth(rs.getInt("dimension_width"));
            size.setDimensionHeight(rs.getInt("dimension_height"));
            size.setDimensionDiameter(rs.getInt("dimension_diameter"));

            return size;
        };

        return jdbc.query(sql, mapper, modelId);
    }

    public List<Models> getModelsWithSizes(String width, String height, String diameter, String season,
            String manufacturer) {
        String sql = """
                  SELECT
                      t.id,
                      t.quantity AS tire_quantity,
                t.loudness_level as tire_loudness_level,
                      t.price as tire_price,
                      t.load_index as tire_load_index,
                      t.speed_rating as tire_speed_rating,
                      t.model_id as tire_model_id,
                      s.season as tire_season,
                      tm.name as model_name ,
                      tm.image_url AS model_imageUrl,
                tm.image_url as manufacturer_imageUrl,
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
                      where width = ? and height = ? and diameter = ?
                      and s.season like ?
                      and m.name like ? """;

        // Adding wildcards to the season and manufacturer parameters
        season = "%" + season + "%";
        manufacturer = "%" + manufacturer + "%";

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
            model.setManufacturerName(rs.getString("manufacturer_name"));
            model.setDimensionWidth(rs.getInt("dimention_width"));
            model.setDimensionHeight(rs.getInt("dimention_height"));
            model.setDimensionDiameter(rs.getInt("dimention_diameter"));
            model.setFuelEfficiency(rs.getString("fuel_efficiency"));
            model.setGripRating(rs.getString("grip_rating"));
            model.setCarType(rs.getString("car_type"));
            model.setTireModelId(rs.getInt("tire_model_id"));
            model.setManufacturerImageUrl(rs.getString("manufacturer_imageUrl"));

            return model;
        };

        return jdbc.query(sql, mapper, width, height, diameter, season, manufacturer);

    }
}