package ru.netology.repository;

import ru.netology.exception.NotFoundException;
import ru.netology.model.Post;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

// Stub
public class PostRepository {
  private final ConcurrentHashMap<Long, Post> repo = new ConcurrentHashMap<>();
  private static long idCounter;

  public List<Post> all() {
    return new ArrayList<>(repo.values());
  }

  public Optional<Post> getById(long id) throws NotFoundException {
    if (repo.containsKey(id)) {
      return Optional.ofNullable(repo.get(id));
    } else {
      throw new NotFoundException("Пост не найден!");
    }
  }

  public Post save(Post post) {
    if (post.getId() == 0) {
      // new post
      post.setId(idCounter++);
    }
    // update post
    repo.put(post.getId(), post);
    return post;
  }

  public void removeById(long id) throws NotFoundException {
    if (repo.containsKey(id)) {
      repo.remove(id);
    } else {
      throw new NotFoundException("Пост не найден!");
    }
  }
}
