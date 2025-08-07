package posting.example.posting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import posting.example.posting.domain.Comment;
import posting.example.posting.domain.Post;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByPost(Post post);
}
