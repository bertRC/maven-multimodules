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
            <% if (request.getAttribute("item") != null) { %>
            <h1>Details</h1>

            <div class="row">
                <div class="col-sm-6 mt-3">
                    <% AutoModel item = (AutoModel) request.getAttribute("item"); %>
                    <img src="<%= request.getContextPath() %>/images/<%= item.getImageUrl() %>" class="card-img-top"
                         alt="<%= item.getName() %>">
                </div>
                <div class="col-sm-6 mt-3">
                    <form action="<%= request.getContextPath() %>" method="post" enctype="multipart/form-data" class="mt-3">
                        <div class="form-group">
                            <label for="name">Auto Name</label>
                            <input type="text" id="name" name="name" class="form-control" value="<%= item.getName() %>"
                                   required>
                        </div>

                        <div class="form-group">
                            <label for="description">Description</label>
                            <textarea name="description" class="form-control" id="description"
                                      placeholder="Auto description"><%= item.getDescription() %></textarea>
                        </div>

                        <div class="custom-file">
                            <input type="file" id="file" name="image" class="custom-file-input" accept="image/*">
                            <label class="custom-file-label" for="file">Choose image...</label>
                        </div>

                        <button type="submit" class="btn btn-primary mt-3">Update</button>
                    </form>

                    <form action="<%= request.getContextPath() %>/remove/<%= item.getId() %>" method="post" class="mt-3">
                        <button type="submit" class="btn btn-primary mt-3">Remove</button>
                    </form>
                </div>
            </div>

            <form class="mt-3">
                <a href="/" class="btn btn-secondary btn-sm active" role="button" aria-pressed="true">Return to Homepage</a>
            </form>

            <% } %>
        </div>
    </div>
</div>
<%@ include file="bootstrap-scripts.jsp" %>
</body>
</html>
