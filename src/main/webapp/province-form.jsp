<%@ include file="header.jsp" %>


    <c:choose>
        <c:when test="${province == null}">
            <h1>Crear nueva provincia</h1>
        </c:when>
        <c:otherwise>
            <h1>Actualizar provincia</h1>
        </c:otherwise>
    </c:choose>

    <form action="provinces" method="post">
        <input type="hidden" name="id" value="${province != null ? province.id : ''}" />
        <input type="hidden" name="action" value="${province == null ? 'insert' : 'update'}" />

        <label for="code">Código:</label>
        <input type="text" name="code" id="code" value="${province != null ? province.code : ''}" required />

        <label for="name">Nombre:</label>
        <input type="text" name="name" id="name" value="${province != null ? province.name : ''}" required />

        <select name="id_region">
            <option value=""></option>
            <c:forEach var="region" items="${listRegions}">
                <option value="${region.id}">${region.name}</option>
            </c:forEach>
        </select>

        <!-- Cuando una cadena de caracteres incluye a otra esta debe llevar comillas simples -->
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