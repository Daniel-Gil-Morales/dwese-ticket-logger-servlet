package org.iesalixar.daw2.dgm.dao;

import org.iesalixar.daw2.dgm.entity.Province;
import org.iesalixar.daw2.dgm.entity.Region;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProvinceDAOImpl implements ProvinceDAO {


    /**
     * Lista todas las Provincees de la base de datos.
     * @return Lista de Provincees
     * @throws SQLException
     */
    public List<Province> listAllProvinces() throws SQLException {
        List<Province> provinces = new ArrayList<>();
        String query = "select p.id id_province, p.code code_province, p.name name_province, r.id id_region, r.code code_region, r.name name_region from provinces p inner join regions r on p.id_region = r.id";

        try (Connection connection = DatabaseConnectionManager.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()){
                int id_province = resultSet.getInt("id_province");
                String code_province = resultSet.getString("code_province");
                String name_province = resultSet.getString("name_province");

                int id_region = resultSet.getInt("id_region");
                String code_region = resultSet.getString("code_region");
                String name_region = resultSet.getString("name_region");

                Region region = new Region(id_region, code_region, name_region);
                Province province = new Province(id_province, code_province, name_province, region);
                provinces.add(province);
            }

        }

        return provinces;
    }

    public void insertProvince(Province province) throws SQLException {
        String query = "INSERT INTO provinces (code, name, id_region) VALUES (?, ?, ?)";

        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, province.getCode());
            preparedStatement.setString(2, province.getName());
            preparedStatement.setInt(3, province.getRegion().getId());
            preparedStatement.executeUpdate();
        }
    }


    /**
     * Actualiza una región existente en la base de datos.
     * @param Province Región a actualizar
     * @throws SQLException
     */
    public void updateProvince(Province province) throws SQLException {
        String query = "UPDATE provinces SET code = ?, name = ? WHERE id = ?"; // Cambiado "Provinces" a "provinces"
        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, province.getCode());
            preparedStatement.setString(2, province.getName());
            preparedStatement.setInt(3, province.getId());
            preparedStatement.executeUpdate();
        }
    }


    /**
     * Elimina una región de la base de datos.
     * @param id ID de la región a eliminar
     * @throws SQLException
     */
    @Override
    public void deleteProvince(int id) throws SQLException {
        String query = "DELETE FROM provinces WHERE id = ?";
        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        }
    }


    /**
     * Verifica si una región con el código especificado ya existe en la base de datos,
     * ignorando mayúsculas.
     *
     * @param code el código de la región a verificar.
     * @return true si una región con el código ya existe, false de lo contrario.
     * @throws SQLException si ocurre un error en la consulta SQL.
     */
    public Province getProvinceById(int id) throws SQLException {
        String query = "SELECT * FROM provinces WHERE id = ?"; // Cambiado "Provinces" a "provinces"
        Province province = null;

        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String code = resultSet.getString("code");
                String name = resultSet.getString("name");
                int regionId = resultSet.getInt("id_region");
                Region region = new Region(regionId, code, name);
                province = new Province(id, code, name, region);
            }
        }
        return province;
    }


    /**
     * Verifica si una región con el código especificado ya existe en la base de datos,
     * ignorando mayúsculas.
     *
     * @param code el código de la región a verificar.
     * @return true si una región con el código ya existe, false de lo contrario.
     * @throws SQLException si ocurre un error en la consulta SQL.
     */
    @Override
    public boolean existsProvinceByCode(String code) throws SQLException {
        String sql = "SELECT COUNT(*) FROM provinces WHERE UPPER(code) = ?";
        try (Connection connection = DatabaseConnectionManager.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, code.toUpperCase());
            try (ResultSet resultSet = statement.executeQuery()) {
                resultSet.next();
                return resultSet.getInt(1) > 0;
            }
        }
    }


    /**
     * Verifica si una región con el código especificado ya existe en la base de datos,
     * ignorando mayúsculas, pero excluyendo una región con un ID específico.
     *
     * @param code el código de la región a verificar.
     * @param id   el ID de la región a excluir de la verificación.
     * @return true si una región con el código ya existe (y no es la región con el ID dado),
     *         false de lo contrario.
     * @throws SQLException si ocurre un error en la consulta SQL.
     */
    @Override
    public boolean existsProvinceByCodeAndNotId(String code, int id) throws SQLException {
        String sql = "SELECT COUNT(*) FROM provinces WHERE UPPER(code) = ? AND id != ?";
        try (Connection connection = DatabaseConnectionManager.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, code.toUpperCase());
            statement.setInt(2, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                resultSet.next();
                return resultSet.getInt(1) > 0;
            }
        }
    }



}
