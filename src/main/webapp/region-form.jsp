<%@ include file="header.jsp" %>

<h1>
        <c:choose>
            <c:when test="${region == null}">
                <fmt:message key="msg.region-form.add" />
            </c:when>
            <c:otherwise>
                <fmt:message key="msg.region-form.edit" />
            </c:otherwise>
        </c:choose>
</h1>

<form action="regions" method="post">
    <input type="hidden" name="id" value="${region != null ? region.id : ''}" />
    <input type="hidden" name="action" value="${region == null ? 'insert' : 'update'}" />

    <label for="code"><fmt:message key="msg.region-form.code" />:</label>
    <input type="text" name="code" id="code"
           value="${region != null && region.code != null ? region.code : ''}" required />

    <label for="name"><fmt:message key="msg.region-form.name" />:</label>
    <input type="text" name="name" id="name"
           value="${region != null && region.name != null ? region.name : ''}" required />

    <c:set var="submitLabel" value="${region == null ? 'msg.region-form.create' : 'msg.region-form.update'}" />
    <input type="submit" value="<fmt:message key='${submitLabel}' />" />
</form>

<%@ include file="footer.jsp" %>