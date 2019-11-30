package ru.itpark.web.router;

import lombok.val;
import ru.itpark.util.JdbcTemplate;
import ru.itpark.web.Router;
import ru.itpark.web.exception.InitializationException;
import ru.itpark.web.exception.NotFoundException;
import ru.itpark.web.model.AutoModel;
import ru.itpark.web.repository.AutoRepositoryJdbcImpl;
import ru.itpark.web.service.AutoService;
import ru.itpark.web.service.AutoServiceImpl;
import ru.itpark.web.service.FileService;
import ru.itpark.web.service.FileServiceImpl;
import ru.itpark.web.util.ResourcesPaths;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.util.regex.Pattern;

public class AppRouter implements Router {
    private AutoService autoService;
    private FileService fileService;

    public static final Pattern urlPattern = Pattern.compile("^/(.+)/(.*)$");

    public void init() {
        try {
            val context = new InitialContext();
            val dataSource = (DataSource) context.lookup(ResourcesPaths.dbPath);
            val uploadPath = System.getenv(ResourcesPaths.uploadPath);
            fileService = new FileServiceImpl(uploadPath);
            autoService = new AutoServiceImpl(new AutoRepositoryJdbcImpl(dataSource, new JdbcTemplate()), fileService);

            //Demo data initialization
            AutoModel kopeykaAuto = new AutoModel(0, "VAZ-2101", "", "VAZ-2103-DEMO");
            AutoModel moskvitchAuto = new AutoModel(0, "Moskvitch-412", "", "MOSKVITCH-412-DEMO");
            autoService.save(kopeykaAuto, null);
            autoService.save(moskvitchAuto, null);
            fileService.copyFileFromUrl("https://sun9-3.userapi.com/c857216/v857216796/66d14/xVq-eHIvPrU.jpg", "VAZ-2103-DEMO");
            fileService.copyFileFromUrl("https://sun9-36.userapi.com/c857216/v857216796/66d0d/i2nKfr2jif4.jpg", "MOSKVITCH-412-DEMO");
        } catch (NamingException e) {
            throw new InitializationException(e);
        }
    }

    @Override
    public void route(HttpServletRequest request, HttpServletResponse response) {
        try {
            val rootUrl = request.getContextPath().isEmpty() ? "/" : request.getContextPath();
            val url = request.getRequestURI().substring(request.getContextPath().length());

            if (url.equals("/")) {
                if (request.getMethod().equals("GET")) {
                    val items = autoService.getAll();
                    request.setAttribute("items", items);
                    request.getRequestDispatcher(ResourcesPaths.frontpageJsp).forward(request, response);
                    return;
                }

                if (request.getMethod().equals("POST")) {
                    request.setCharacterEncoding("UTF-8");
                    val name = request.getParameter("name");
                    val description = request.getParameter("description");
                    val part = request.getPart("image");
                    autoService.save(new AutoModel(0, name, description, null), part);
                    response.sendRedirect(rootUrl);
                    return;
                }

                throw new NotFoundException();
            }

            val matcher = urlPattern.matcher(url);
            String queryName;
            String attribute;
            // url template: "/queryName/attribute"
            if (matcher.find()) {
                queryName = matcher.group(1);
                attribute = matcher.group(2);
            } else return;

            if (queryName.equals("details")) {
                if (request.getMethod().equals("GET")) {
                    val id = Integer.parseInt(attribute);
                    val item = autoService.getById(id);
                    request.setAttribute("item", item);
                    request.getRequestDispatcher(ResourcesPaths.detailsJsp).forward(request, response);
                    return;
                }
                if (request.getMethod().equals("POST")) {
                    request.setCharacterEncoding("UTF-8");
                    val id = Integer.parseInt(attribute);
                    val name = request.getParameter("name");
                    val description = request.getParameter("description");
                    val part = request.getPart("image");
                    autoService.save(new AutoModel(id, name, description, null), part);
                    response.sendRedirect(rootUrl);
                    return;
                }

                throw new NotFoundException();
            }

            if (queryName.equals("remove")) {
                if (request.getMethod().equals("POST")) {
                    val id = Integer.parseInt(attribute);
                    autoService.removeById(id);
                    response.sendRedirect(rootUrl);
                    return;
                }

                throw new NotFoundException();
            }

            if (queryName.equals("images")) {
                if (request.getMethod().equals("GET")) {
                    val id = attribute;
                    fileService.readFile(id, response.getOutputStream());
                    return;
                }

                throw new NotFoundException();
            }

            if (queryName.equals("search")) {
                if (request.getMethod().equals("GET")) {
                    val text = request.getParameter("text");
                    val items = autoService.search(text);
                    request.setAttribute("items", items);
                    request.getRequestDispatcher(ResourcesPaths.searchResultsJsp).forward(request, response);
                }

                throw new NotFoundException();
            }

        } catch (Exception e) {
            e.printStackTrace();
            try {
                request.getRequestDispatcher(ResourcesPaths.errorJsp).forward(request, response);
            } catch (ServletException | IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
