package org.iesalixar.daw2.dgm.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.iesalixar.daw2.dgm.dao.ProvinceDAO;
import org.iesalixar.daw2.dgm.dao.ProvinceDAOImpl;
import org.iesalixar.daw2.dgm.dao.RegionDAO;
import org.iesalixar.daw2.dgm.dao.RegionDAOImpl;
import org.iesalixar.daw2.dgm.entity.Province;
import org.iesalixar.daw2.dgm.entity.Region;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Servlet que maneja las operaciones CRUD para la entidad `Province`.
 * Utiliza `ProvinceDAO` para interactuar con la base de datos.
 */
@WebServlet("/Provinces")
public class ProvinceServlet extends HttpServlet {


    // DAO para gestionar las operaciones de las Provincees en la base de datos
    private ProvinceDAO provinceDAO;
    private RegionDAO regionDAO;

    @Override
    public void init() throws ServletException {
        try{
            provinceDAO = new ProvinceDAOImpl();
            regionDAO = new RegionDAOImpl();
        } catch (Exception e) {
            throw new ServletException("Error al inicializar el ProvinceDAO", e);
        }
    }



    /**
     * Maneja las solicitudes GET al servlet. Según el parámetro "action", decide qué método invocar.
     * @param request  Solicitud HTTP.
     * @param response Respuesta HTTP.
     * @throws ServletException en caso de errores en el servlet.
     * @throws IOException en caso de errores de E/S.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        String action = request.getParameter("action");
        response.setContentType("text/html;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        try {
            if (action == null) {
                action = "list";
            }

            switch (action) {
                case "new":
                    showNewForm(request, response);
                    break;
                case "edit":
                    break;
                default:
                    listProvinces(request, response);
                    break;
            }
        } catch (SQLException | IOException ex) {
            throw new ServletException(ex);
        }
    }



    /**
     * Lista todas las Provincees y las pasa como atributo a la vista `Province.jsp`.
     * @param request  Solicitud HTTP.
     * @param response Respuesta HTTP.
     * @throws SQLException en caso de error en la consulta SQL.
     * @throws IOException en caso de error de E/S.
     * @throws ServletException en caso de error en el servlet.
     */
    private void listProvinces(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        List<Province> listProvinces = provinceDAO.listAllProvinces();
        request.setAttribute("listProvinces", listProvinces);
        request.getRequestDispatcher("province.jsp").forward(request, response); // Redirigir a la página JSP
    }


    /**
     * Muestra el formulario para crear una nueva región.
     * @param request  Solicitud HTTP.
     * @param response Respuesta HTTP.
     * @throws ServletException en caso de error en el servlet.
     * @throws IOException en caso de error de E/S.
     */
    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        List<Region> listRegions = regionDAO.listAllRegions();
        request.setAttribute("listRegions", listRegions);
        request.getRequestDispatcher("province-form.jsp").forward(request, response); // Redirige a la vista para nueva región
    }


    /**
     * Muestra el formulario para editar una región existente.
     * @param request  Solicitud HTTP.
     * @param response Respuesta HTTP.
     * @throws SQLException en caso de error en la consulta SQL.
     * @throws ServletException en caso de error en el servlet.
     * @throws IOException en caso de error de E/S.
     */
    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Province existingProvince = provinceDAO.getProvinceById(id);   // Obtener región por ID desde el DAO
        request.setAttribute("Province", existingProvince);        // Pasar la región a la vista
        request.getRequestDispatcher("Province-form.jsp").forward(request, response); // Redirigir a la vista para editar
    }


    /**
     * Inserta una nueva región en la base de datos después de realizar validaciones.
     * Verifica que el código de la región sea único (ignorando mayúsculas) y que los campos
     * de código y nombre no estén vacíos.
     *
     * @param request  la solicitud HTTP con los datos del formulario.
     * @param response la respuesta HTTP para redirigir o mostrar errores.
     * @throws SQLException      si ocurre un error en la base de datos.
     * @throws IOException       si ocurre un error de entrada/salida.
     * @throws ServletException  si ocurre un error en el procesamiento del servlet.
     */
    private void insertProvince(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        String code = request.getParameter("code").trim().toUpperCase(); // Convertir a mayúsculas
        String name = request.getParameter("name").trim();
        String id_region = request.getParameter("id_region");


        // Validaciones básicas
        if (code.isEmpty() || name.isEmpty() || id_region.isEmpty()) {
            request.setAttribute("errorMessage", "El código y el nombre y la communidad autónoma no pueden estar vacíos.");
            request.getRequestDispatcher("province-form.jsp").forward(request, response);
            return;
        }


        // Validar si el código ya existe ignorando mayúsculas
        if (provinceDAO.existsProvinceByCode(code)) {
            request.setAttribute("errorMessage", "El código de la provincia ya existe.");
            request.getRequestDispatcher("province-form.jsp").forward(request, response);
            return;
        }


        Province newProvince = new Province(code, name);
        try {
            provinceDAO.insertProvince(newProvince);
        } catch (SQLException e) {
            if (e.getSQLState().equals("23505")) { // Código SQL para unique constraint violation
                request.setAttribute("errorMessage", "El código de la región debe ser único.");
                request.getRequestDispatcher("Province-form.jsp").forward(request, response);
            } else {
                throw e;
            }
        }
        response.sendRedirect("Provinces");
    }


    /**
     * Actualiza una región existente en la base de datos después de realizar validaciones.
     * Verifica que el código de la región sea único para otras Provincees (ignorando mayúsculas)
     * y que los campos de código y nombre no estén vacíos.
     *
     * @param request  la solicitud HTTP con los datos del formulario.
     * @param response la respuesta HTTP para redirigir o mostrar errores.
     * @throws SQLException      si ocurre un error en la base de datos.
     * @throws IOException       si ocurre un error de entrada/salida.
     * @throws ServletException  si ocurre un error en el procesamiento del servlet.
     */
    private void updateProvince(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        int id = Integer.parseInt(request.getParameter("id"));
        String code = request.getParameter("code").trim().toUpperCase(); // Convertir a mayúsculas
        String name = request.getParameter("name").trim();


        // Validaciones básicas
        if (code.isEmpty() || name.isEmpty()) {
            request.setAttribute("errorMessage", "El código y el nombre no pueden estar vacíos.");
            request.getRequestDispatcher("Province-form.jsp").forward(request, response);
            return;
        }


        // Validar si el código ya existe para otra región
        if (provinceDAO.existsProvinceByCodeAndNotId(code, id)) {
            request.setAttribute("errorMessage", "El código de la región ya existe para otra región.");
            request.getRequestDispatcher("Province-form.jsp").forward(request, response);
            return;
        }


        Province updatedProvince = new Province(id, code, name);
        try {
            provinceDAO.updateProvince(updatedProvince);
        } catch (SQLException e) {
            if (e.getSQLState().equals("23505")) { // Código SQL para unique constraint violation
                request.setAttribute("errorMessage", "El código de la región debe ser único.");
                request.getRequestDispatcher("Province-form.jsp").forward(request, response);
            } else {
                throw e;
            }
        }
        response.sendRedirect("Provinces");
    }


    /**
     * Elimina una región de la base de datos según su ID.
     * @param request  Solicitud HTTP.
     * @param response Respuesta HTTP.
     * @throws SQLException en caso de error en la consulta SQL.
     * @throws IOException en caso de error de E/S.
     */
    private void deleteProvince(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        provinceDAO.deleteProvince(id);  // Eliminar región usando el DAO
        response.sendRedirect("Provinces"); // Redirigir al listado de Provincees
    }
}

