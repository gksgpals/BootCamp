package posting.example.posting.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import posting.example.posting.domain.Comment;
import posting.example.posting.domain.Post;
import posting.example.posting.domain.User;
import posting.example.posting.repository.CommentRepository;
import posting.example.posting.repository.PostRepository;
import posting.example.posting.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepo;
    private final PostRepository postRepo;
    private final UserRepository userRepo;

    // 새 댓글 작성
    @Transactional
    public Comment create(String username, Long postId, Comment comment) {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + username));
        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found: " + postId));
        comment.setUser(user);
        comment.setPost(post);
        return commentRepo.save(comment);
    }

    // 댓글 수정: 관리자이거나 작성자만 가능
    @PreAuthorize("hasRole('ADMIN') or @commentService.isOwner(#username, #commentId)")
    @Transactional
    public Comment update(String username, Long commentId, Comment data) {
        Comment comment = commentRepo.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found: " + commentId));
        comment.setContent(data.getContent());
        return commentRepo.save(comment);
    }

    // 댓글 삭제: 관리자이거나 작성자만 가능
    @PreAuthorize("hasRole('ADMIN') or @commentService.isOwner(#username, #commentId)")
    @Transactional
    public void delete(String username, Long commentId) {
        Comment comment = commentRepo.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found: " + commentId));
        commentRepo.delete(comment);
    }

    // 특정 게시글의 모든 댓글 조회
    @Transactional(readOnly = true)
    public List<Comment> listByPost(Long postId) {
        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found: " + postId));
        return commentRepo.findAllByPost(post);
    }

    // 댓글 작성자인지 확인
    public boolean isOwner(String username, Long commentId) {
        return commentRepo.findById(commentId)
                .map(c -> c.getUser().getUsername().equals(username))
                .orElse(false);
    }
}
