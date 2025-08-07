package posting.example.posting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import posting.example.posting.domain.Post;

public interface PostRepository extends JpaRepository<Post, Long> {
}
