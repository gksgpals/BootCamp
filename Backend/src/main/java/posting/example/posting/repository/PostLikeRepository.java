package posting.example.posting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import posting.example.posting.domain.Like;

public interface PostLikeRepository extends JpaRepository<Like, Long> {
    // 좋아요 상태 조회용 메소드
    boolean existsByUserIdAndPostId(Long userId, Long postId);

    void deleteByUserIdAndPostId(Long userId, Long postId);
}
