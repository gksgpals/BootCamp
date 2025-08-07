package posting.example.posting.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import posting.example.posting.domain.Comment;
import posting.example.posting.domain.Share;
import posting.example.posting.service.CommentService;
import posting.example.posting.service.LikeService;
import posting.example.posting.service.ShareService;
import posting.example.posting.service.UserService;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class InteractionController {
    private final LikeService likeService;
    private final CommentService commentService;
    private final ShareService shareService;

    // 좋아요 토글
    @PostMapping("/posts/{postId}/like")
    public ResponseEntity<Void> toggleLike(@PathVariable Long postId, Principal principal){
        likeService.toggleLike(principal.getName(), postId);
        return ResponseEntity.ok().build();
    }

    // 댓글 작성
    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<Comment> addComment(@PathVariable Long postId,
                                              @RequestBody Comment comment,
                                              Principal principal){
        Comment saved = commentService.create(principal.getName(),  postId, comment);
        return ResponseEntity.ok(saved);
    }

    // 댓글 수정
    @PostMapping("/posts/{postId}/comments/{commentId}")
    public ResponseEntity<Comment> editComment(@PathVariable Long postId,
                                               @PathVariable Long commentId,
                                               @RequestBody Comment comment,
                                               Principal principal){
        Comment updated = commentService.update(principal.getName(),  commentId, comment);
        return ResponseEntity.ok(updated);
    }

    // 댓글 삭제
    @DeleteMapping("/posts/{postId}/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long postId,
                                              @PathVariable Long commentId,
                                              Principal principal) {
        commentService.delete(principal.getName(), commentId);
        return ResponseEntity.noContent().build();
    }

    // 공유 생성
    @PostMapping("/posts/{postId}/share")
    public ResponseEntity<Share> sharePost(@PathVariable Long postId, Principal principal) {
        Share share = shareService.share(principal.getName(), postId);
        return ResponseEntity.ok(share);
    }

    // 공유 취소
    @DeleteMapping("/shares/{shareId}")
    public ResponseEntity<Void> revokeShare(@PathVariable Long shareId, Principal principal) {
        shareService.revoke(principal.getName(), shareId);
        return ResponseEntity.noContent().build();
    }
}
