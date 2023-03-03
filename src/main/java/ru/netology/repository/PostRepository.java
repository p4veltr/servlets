package ru.netology.repository;

import org.springframework.stereotype.Repository;
import ru.netology.exception.NotFoundException;
import ru.netology.model.Post;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

// Stub
@Repository
public class PostRepository {
  private final ConcurrentHashMap<Long, Post> repo = new ConcurrentHashMap<>();
  private static final AtomicLong idCounter = new AtomicLong(0);

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
      post.setId(idCounter.incrementAndGet());
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
