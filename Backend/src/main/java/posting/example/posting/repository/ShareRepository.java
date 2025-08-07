package posting.example.posting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import posting.example.posting.domain.Post;
import posting.example.posting.domain.Share;

import java.util.List;

public interface ShareRepository extends JpaRepository<Share, Long> {
    List<Share> findAllByPost(Post post);
}
