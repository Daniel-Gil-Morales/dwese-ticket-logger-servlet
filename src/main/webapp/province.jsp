<%@ include file="header.jsp" %>

<h1>Listado de Provincias</h1>

<a href="provinces?action=new">Añadir Provincia</a>

<table border="1">
    <thead>
        <th>ID</th>
        <th>Código</th>
        <th>Nombre</th>
        <th>Comunidad Autonoma</th>
    </thead>
    <tbody>
        <c:forEach var="province" items="${listProvinces}">
            <tr>
                <td>${province.id}</td>
                <td>${province.code}</td>
                <td>${province.name}</td>
                <td>${province.region.name}</td>
            </tr>
        </c:forEach>
    </tbody>
</table>

<%@ include file="footer.jsp" %>