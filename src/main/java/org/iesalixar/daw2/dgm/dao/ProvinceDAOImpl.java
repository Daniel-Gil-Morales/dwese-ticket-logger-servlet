package org.iesalixar.daw2.dgm.dao;

import org.iesalixar.daw2.dgm.entity.Province;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProvinceDAOImpl implements ProvinceDAO {
    private static final Logger logger = LoggerFactory.getLogger(ProvinceDAOImpl.class);

    @Override
    public List<Province> listAllProvinces() throws SQLException {
        List<Province> provinces = new ArrayList<>();
        String query = "SELECT * FROM provincias";

        try (Connection connection = DatabaseConnectionManager.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                provinces.add(new Province(
                        resultSet.getInt("id"),
                        resultSet.getString("code"),
                        resultSet.getString("name"),
                        resultSet.getInt("id_region")
                ));
            }
        }
        return provinces;
    }

    @Override
    public void insertProvince(Province province) throws SQLException {
        String query = "INSERT INTO provincias (code, name, id_region) VALUES (?, ?, ?)";
        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, province.getCode());
            statement.setString(2, province.getName());
            statement.setInt(3, province.getIdRegion());
            statement.executeUpdate();
            logger.info("Provincia insertada: {}", province);
        } catch (SQLException e) {
            logger.error("Error al insertar provincia: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public void updateProvince(Province province) throws SQLException {
        String query = "UPDATE provincias SET code = ?, name = ?, id_region = ? WHERE id = ?";
        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, province.getCode());
            statement.setString(2, province.getName());
            statement.setInt(3, province.getIdRegion());
            statement.setInt(4, province.getId());
            statement.executeUpdate();
            logger.info("Provincia actualizada: {}", province);
        } catch (SQLException e) {
            logger.error("Error al actualizar provincia: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public void deleteProvince(int id) throws SQLException {
        String query = "DELETE FROM provincias WHERE id = ?";
        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            statement.executeUpdate();
            logger.info("Provincia eliminada con ID: {}", id);
        } catch (SQLException e) {
            logger.error("Error al eliminar provincia: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public Province getProvinceById(int id) throws SQLException {
        Province province = null;
        String query = "SELECT * FROM provincias WHERE id = ?";
        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                province = new Province(
                        resultSet.getInt("id"),
                        resultSet.getString("code"),
                        resultSet.getString("name"),
                        resultSet.getInt("id_region")
                );
                logger.info("Provincia obtenida: {}", province);
            }
        } catch (SQLException e) {
            logger.error("Error al obtener provincia: {}", e.getMessage());
            throw e;
        }
        return province;
    }

    @Override
    public boolean existsProvinceByCode(String code) throws SQLException {
        String query = "SELECT COUNT(*) FROM provincias WHERE code = ?";
        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, code);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
        } catch (SQLException e) {
            logger.error("Error al verificar existencia de provincia por código: {}", e.getMessage());
            throw e;
        }
        return false;
    }

    @Override
    public boolean existsProvinceByCodeAndNotId(String code, int id) throws SQLException {
        String sql = "SELECT COUNT(*) FROM provinces WHERE code = ? AND id <> ?";
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, code);
            stmt.setInt(2, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; // Retorna true si existe otra provincia con el mismo código
            }
        }
        return false; // No existe otra provincia con el mismo código
    }


}
