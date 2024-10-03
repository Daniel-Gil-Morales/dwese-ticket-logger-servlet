<%@ include file="header.jsp" %>

<h1>Listado de Provincias</h1>

<a href="provinces?action=new">Agregar Provincia</a>

<table border="1">
    <thead>
        <tr>
            <th><fmt:message key="msg.province.id" /></th>
            <th><fmt:message key="msg.province.code" /></th>
            <th><fmt:message key="msg.province.name" /></th>
            <th><fmt:message key="msg.province.region" /></th>
            <th><fmt:message key="msg.province.actions" /></th>
        </tr>
    </thead>
    <tbody>
        <c:forEach var="province" items="${listProvinces}">
            <tr>
                <td>${province.id}</td>
                <td>${province.code}</td>
                <td>${province.name}</td>
                <td>${province.region.name}</td>
                <td>
                    <a href="provinces?action=edit&id=${province.id}">Editar</a>
                    <form action="provinces" method="post" style="display:inline;">
                        <input type="hidden" name="action" value="delete" />
                        <input type="hidden" name="id" value="${province.id}" />
                        <input type="submit" value="Eliminar" onclick="return confirm('<fmt:message key='msg.province.confirm' />')" />
                    </form>
                </td>
            </tr>
        </c:forEach>
    </tbody>
</table>

<%@ include file="footer.jsp" %>
