<%@ page import="java.util.List" %>
<%@ page import="ru.itpark.web.model.AutoModel" %>
<%@ page import="ru.itpark.web.repository.AutoRepositoryJdbcImpl" %>
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
            <h1>Catalog</h1>

            <form class="mt-3" action="<%= request.getContextPath() %>/search/">
                <input name="text" class="form-control" type="search" placeholder="Search" pattern=".{<%= AutoRepositoryJdbcImpl.minTextLength %>,}" required title="3 characters minimum">
            </form>

            <div class="row">
                <% if (request.getAttribute("items") != null) { %>
                <% for (AutoModel item : (List<AutoModel>) request.getAttribute("items")) { %>
                <div class="col-sm-6 mt-3">
                    <div class="card">
                        <a href="<%= request.getContextPath()%>/details/<%= item.getId() %>">
                            <img src="<%= request.getContextPath() %>/images/<%= item.getImageUrl() %>" class="card-img-top" alt="<%= item.getName() %>">
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

            <form action="<%= request.getContextPath() %>" method="post" enctype="multipart/form-data" class="mt-3">
                <div class="form-group">
                    <label for="name">Auto Name</label>
                    <input type="text" id="name" name="name" class="form-control" required>
                </div>

                <div class="form-group">
                    <label for="description">Description</label>
                    <textarea name="description" class="form-control" id="description" placeholder="Auto description (optional)"></textarea>
                </div>

                <div class="custom-file">
                    <input type="file" id="file" name="image" class="custom-file-input" accept="image/*" required>
                    <label class="custom-file-label" for="file">Choose image...</label>
                </div>

                <button type="submit" class="btn btn-primary mt-3">Create</button>
            </form>
        </div>
    </div>
</div>
<%@ include file="bootstrap-scripts.jsp" %>
</body>
</html>
