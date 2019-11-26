<%@ page import="java.util.List" %>
<%@ page import="ru.itpark.web.model.AutoModel" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <%@ include file="bootstrap-css.jsp" %>
</head>
<body>
<div class="container">
    <div class="row">
        <div class="col">
            <h1>Search Results</h1>

            <div class="row">
                <% if (request.getAttribute("items") != null) { %>
                <% for (AutoModel item : (List<AutoModel>) request.getAttribute("items")) { %>
                <div class="col-sm-6 mt-3">
                    <div class="card">
                        <a href="<%= request.getContextPath()%>/details/<%= item.getId() %>">
                            <img src="<%= request.getContextPath() %>/images/<%= item.getImageUrl() %>"
                                 class="card-img-top" alt="<%= item.getName() %>">
                        </a>
                        <div class="card-body">
                            <h5 class="card-title"><%= item.getName() %>
                            </h5>
                            <p class="card-text"><%= item.getDescription()%>
                            </p>
                        </div>
                    </div>
                </div>
                <% } %>
                <% } %>
            </div>

            <form class="mt-3">
                <a href="/" class="btn btn-secondary btn-sm active" role="button" aria-pressed="true">Return To Homepage</a>
            </form>

        </div>
    </div>
</div>
<%@ include file="bootstrap-scripts.jsp" %>
</body>
</html>
