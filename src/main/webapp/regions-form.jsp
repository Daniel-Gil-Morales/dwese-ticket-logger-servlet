<%@ include file="header.jsp" %>


   <h1><c:out value="${region == null ? 'Nueva Comunidad Autónoma' : 'Editar Comunidad Autónoma'}" /></h1>

   <%-- Mostrar mensaje de error si existe --%>
   <c:if test="${not empty errorMessage}">
      <div class="error-message">${errorMessage}</div>
   </c:if>


   <form action="regions" method="post">
       <input type="hidden" name="id" value="${region != null ? region.id : ''}" />
       <input type="hidden" name="action" value="${region == null ? 'insert' : 'update'}" />


       <label for="code">Código:</label>
       <input type="text" name="code" id="code" value="${region != null ? region.code : ''}" required />


       <label for="name">Nombre:</label>
       <input type="text" name="name" id="name" value="${region != null ? region.name : ''}" required />


       <input type="submit" value="${region == null ? 'Crear' : 'Actualizar'}" />
   </form>


   <a href="regions">Volver a la lista</a>


<%@ include file="footer.jsp" %>
