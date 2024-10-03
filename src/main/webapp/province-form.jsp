<%@ include file="header.jsp" %>

<c:choose>
    <c:when test="${province == null}">
        <h1>Crear nueva provincia</h1>
    </c:when>
    <c:otherwise>
        <h1>Actualizar provincia</h1>
    </c:otherwise>
</c:choose>

<c:if test="${not empty errorMessage}">
    <div class="error-message">${errorMessage}</div>
</c:if>

<form action="provinces" method="post">
    <input type="hidden" name="id" value="${province != null ? province.id : ''}" />
    <input type="hidden" name="action" value="${province == null ? 'insert' : 'update'}" />

    <label for="code">Código:</label>
    <input type="text" name="code" id="code" value="${province != null ? province.code : ''}" required />

    <label for="name">Nombre:</label>
    <input type="text" name="name" id="name" value="${province != null ? province.name : ''}" required />

    <label for="id_region">Comunidad Autónoma: </label>
    <select id="id_region" name="id_region" required>
        <option value=""></option>
        <c:forEach var="region" items="${listRegions}">
            <option value="${region.id}" <c:if test="${province != null && province.region.id == region.id}">selected</c:if>>${region.name}</option>
        </c:forEach>
    </select>

    <c:choose>
        <c:when test="${province == null}">
            <input type="submit" value="Crear Provincia" />
        </c:when>
        <c:otherwise>
            <input type="submit" value="Actualizar Provincia" />
        </c:otherwise>
    </c:choose>
</form>

<%@ include file="footer.jsp" %>
