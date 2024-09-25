<%@ include file="header.jsp" %>


   <h1>Listado de Comunidades Autónomas</h1>
   <a href="regions?action=new">Agregar Nueva Comunidad Autónoma</a>
   <table border="1">
       <thead>
           <tr>
               <th>ID</th>
               <th>Código</th>
               <th>Nombre</th>
               <th>Acciones</th>
           </tr>
       </thead>
       <tbody>
           <c:forEach var="region" items="${listRegions}">
               <tr>
                   <td>${region.id}</td>
                   <td>${region.code}</td>
                   <td>${region.name}</td>
                   <td>
                       <a href="regions?action=edit&id=${region.id}">Editar</a>
                       <form action="regions" method="post" style="display:inline;">
                           <input type="hidden" name="action" value="delete" />
                           <input type="hidden" name="id" value="${region.id}" />
                           <input type="submit" value="Eliminar" onclick="return confirm('¿Estás seguro?')" />
                       </form>
                   </td>
               </tr>
           </c:forEach>
       </tbody>
   </table>
<%@ include file="footer.jsp" %>
