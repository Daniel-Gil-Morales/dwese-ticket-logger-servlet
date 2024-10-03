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

import static java.lang.Integer.parseInt;

/**
 * Servlet que maneja las operaciones CRUD para la entidad `Province`.
 * Utiliza `ProvinceDAO` para interactuar con la base de datos.
 */
@WebServlet("/provinces")
public class ProvinceServlet extends HttpServlet {

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
                    showEditForm(request, response);
                    break;
                case "delete":
                    deleteProvince(request, response);
                    break;
                default:
                    listProvinces(request, response);
                    break;
            }
        } catch (SQLException | IOException ex) {
            throw new ServletException(ex);
        }
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Province existingProvince = provinceDAO.getProvinceById(id);
        request.setAttribute("province", existingProvince);
        List<Region> listRegions = regionDAO.listAllRegions();
        request.setAttribute("listRegions", listRegions);
        request.getRequestDispatcher("province-form.jsp").forward(request, response);
    }

    private void deleteProvince(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        provinceDAO.deleteProvince(id);
        response.sendRedirect("provinces");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        try {
            switch (action) {
                case "insert":
                    insertProvince(request, response);
                    break;
                case "update":
                    updateProvince(request, response); // Agregar esta línea
                    break;
                case "delete":
                    deleteProvince(request, response);
                    break;
                default:
                    listProvinces(request, response);
                    break;
            }
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }


    private void listProvinces(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        List<Province> listProvinces = provinceDAO.listAllProvinces();
        request.setAttribute("listProvinces", listProvinces);
        request.getRequestDispatcher("province.jsp").forward(request, response); // Redirigir a la página JSP
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        List<Region> listRegions = regionDAO.listAllRegions();
        request.setAttribute("listRegions", listRegions);
        request.getRequestDispatcher("province-form.jsp").forward(request, response); // Redirige a la vista para nueva región
    }

    private void insertProvince(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        String code = request.getParameter("code");
        String name = request.getParameter("name");
        String id_region = request.getParameter("id_region");

        // Validaciones básicas
        if (code.isEmpty() || name.isEmpty() || id_region.isEmpty()) {
            request.setAttribute("errorMessage", "El código y el nombre y la comunidad autonoma no pueden estar vacíos.");
            request.getRequestDispatcher("province-form.jsp").forward(request, response);
            return;
        }

        // Validar si el código ya existe ignorando mayúsculas
        if (provinceDAO.existsProvinceByCode(code)) {
            request.setAttribute("errorMessage", "El código de la provincia ya existe.");
            request.getRequestDispatcher("province-form.jsp").forward(request, response);
            return;
        }

        Province province = new Province();
        province.setCode(code);
        province.setName(name);

        Region region = regionDAO.getRegionById(Integer.parseInt(id_region));
        province.setRegion(region);

        try{
            provinceDAO.insertProvince(province);
        } catch (SQLException e) {
            if (e.getSQLState().equals("23505")) { // Código SQL para unique constraint violation
                request.setAttribute("errorMessage", "El código de la provincia debe ser único.");
                request.getRequestDispatcher("province-form.jsp").forward(request, response);
            } else {
                throw e;
            }
        }
        response.sendRedirect("provinces");

    }

    private void updateProvince(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        int id = Integer.parseInt(request.getParameter("id"));
        String code = request.getParameter("code");
        String name = request.getParameter("name");
        int regionId = Integer.parseInt(request.getParameter("id_region"));

        // Validaciones básicas
        if (code.isEmpty() || name.isEmpty() || regionId <= 0) {
            request.setAttribute("errorMessage", "El código, nombre y la comunidad autónoma no pueden estar vacíos.");
            request.setAttribute("province", provinceDAO.getProvinceById(id)); // Rellenar la provincia
            request.setAttribute("listRegions", regionDAO.listAllRegions()); // Rellenar las regiones
            request.getRequestDispatcher("province-form.jsp").forward(request, response);
            return;
        }

        // Verificar si el código ya existe (excluyendo la provincia que se está actualizando)
        if (provinceDAO.existsProvinceByCodeAndNotId(code, id)) {
            request.setAttribute("errorMessage", "El código de la provincia ya existe.");
            request.setAttribute("province", provinceDAO.getProvinceById(id)); // Rellenar la provincia
            request.setAttribute("listRegions", regionDAO.listAllRegions()); // Rellenar las regiones
            request.getRequestDispatcher("province-form.jsp").forward(request, response);
            return;
        }

        Province province = new Province(id, code, name, new Region(regionId, null, null));
        provinceDAO.updateProvince(province);
        response.sendRedirect("provinces");
    }



}

