package ru.netology.servlet;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.netology.controller.PostController;
import ru.netology.repository.PostRepository;
import ru.netology.service.PostService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MainServlet extends HttpServlet {
  private PostController controller;
  public static final String GET = "GET";
  public static final String POST = "POST";
  public static final String DELETE = "DELETE";

  @Override
  public void init() {
    final var context = new AnnotationConfigApplicationContext("ru.netology");
    controller = context.getBean(PostController.class);
  }

  @Override
  protected void service(HttpServletRequest req, HttpServletResponse resp) {
    // если деплоились в root context, то достаточно этого
    try {
      final var path = req.getRequestURI();
      final var method = req.getMethod();

      if (method.equals(GET)) {
        handleGet(path, resp);
        return;
      }

      if (method.equals(POST)) {
        handlePost(path, req, resp);
        return;
      }

      if (method.equals(DELETE)) {
        handleDelete(path, resp);
        return;
      }

      resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    } catch (Exception e) {
      e.printStackTrace();
      resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
  }

  private void handleDelete(String path, HttpServletResponse resp) {
    if (path.matches("/api/posts/\\d+")) {
      final var id = Long.parseLong(path.substring(path.lastIndexOf("/")+1));
      controller.removeById(id, resp);
      return;
    }
    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
  }

  private void handlePost(String path, HttpServletRequest req, HttpServletResponse resp) throws IOException {
    if (path.equals("/api/posts")) {
        controller.save(req.getReader(), resp);
        return;
    }
    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
  }

  private void handleGet(String path, HttpServletResponse resp) throws IOException {
    if (path.equals("/api/posts")) {
      controller.all(resp);
      return;
    } else if (path.matches("/api/posts/\\d+")) {
      // easy way
      final var id = Long.parseLong(path.substring(path.lastIndexOf("/")+1));
      controller.getById(id, resp);
      return;
    }
    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
  }
}

