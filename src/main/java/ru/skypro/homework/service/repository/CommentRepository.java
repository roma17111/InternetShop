package ru.skypro.homework.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.homework.models.Comment;

//@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {
}
