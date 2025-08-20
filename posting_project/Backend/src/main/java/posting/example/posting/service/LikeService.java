package posting.example.posting.service;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import posting.example.posting.domain.Like;
import posting.example.posting.domain.Post;
import posting.example.posting.domain.Role;
import posting.example.posting.domain.User;
import posting.example.posting.repository.PostLikeRepository;
import posting.example.posting.repository.PostRepository;
import posting.example.posting.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.function.Supplier;

@Service
public class LikeService {
    private final PostLikeRepository likeRepo;
    private final PostRepository postRepo;
    private final UserRepository userRepo;

    public LikeService(PostLikeRepository likeRepo,
                       PostRepository postRepo,
                       UserRepository userRepo) {
        this.likeRepo = likeRepo;
        this.postRepo = postRepo;
        this.userRepo = userRepo;
    }

    /**
     * 토글 좋아요:
     * - 이미 좋아요가 있으면 삭제(false)
     * - 없으면 생성(true)
     * 권한: ADMIN 이거나 post가 존재할 때만 가능
     */
    @PreAuthorize("hasRole('ADMIN') or @likeService.canToggle(#username, #postId)")
    @Transactional
    public boolean toggleLike(String username, Long postId) {
        // 1) 사용자 존재 확인
        User user = userRepo.findByUsername(username)
                .orElseThrow(new Supplier<EntityNotFoundException>() {
                    @Override
                    public EntityNotFoundException get() {
                        return new EntityNotFoundException("User not found: " + username);
                    }
                });

        // 2) 게시글 존재 확인
        Post post = postRepo.findById(postId)
                .orElseThrow(new Supplier<EntityNotFoundException>() {
                    @Override
                    public EntityNotFoundException get() {
                        return new EntityNotFoundException("Post not found: " + postId);
                    }
                });

        // 3) 좋아요 여부 체크
        if (likeRepo.existsByUserIdAndPostId(user.getId(), post.getId())) {
            likeRepo.deleteByUserIdAndPostId(user.getId(), post.getId());
            return false;
        } else {
            Like like = new Like();
            like.setUser(user);
            like.setPost(post);
            like.setLikedAt(LocalDateTime.now());
            likeRepo.save(like);
            return true;
        }
    }

    /**
     * ADMIN 이거나, 해당 post가 실제로 존재할 때만 좋아요 토글 허용
     */
    public boolean canToggle(String username, Long postId) {
        User u = userRepo.findByUsername(username)
                .orElseThrow(new Supplier<EntityNotFoundException>() {
                    @Override
                    public EntityNotFoundException get() {
                        return new EntityNotFoundException("User not found: " + username);
                    }
                });
        return u.getRole() == Role.ADMIN || postRepo.existsById(postId);
    }
}
