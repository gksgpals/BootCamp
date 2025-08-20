package posting.example.posting.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import posting.example.posting.domain.Post;
import posting.example.posting.domain.Share;
import posting.example.posting.domain.User;
import posting.example.posting.repository.PostRepository;
import posting.example.posting.repository.ShareRepository;
import posting.example.posting.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;  // ← 수정

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ShareService {
    private final ShareRepository shareRepo;
    private final PostRepository postRepo;
    private final UserRepository userRepo;

    /**
     * Create a new share record for a post
     */
    @Transactional
    public Share share(String username, Long postId) {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + username));
        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found: " + postId));
        Share share = new Share();
        share.setUser(user);
        share.setPost(post);
        share.setSharedAt(LocalDateTime.now());
        return shareRepo.save(share);
    }

    /**
     * Revoke an existing share by share ID
     */
    @Transactional
    @PreAuthorize("hasRole('ADMIN') or @shareService.isOwner(#username, #shareId)")
    public void revoke(String username, Long shareId) {
        Share share = shareRepo.findById(shareId)
                .orElseThrow(() -> new EntityNotFoundException("Share not found: " + shareId));
        shareRepo.delete(share);
    }

    /**
     * List all shares for a specific post
     */
    @Transactional(readOnly = true)
    public List<Share> listByPost(Long postId) {
        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found: " + postId));
        return shareRepo.findAllByPost(post);
    }

    /**
     * Check if the given username owns the share
     */
    public boolean isOwner(String username, Long shareId) {
        return shareRepo.findById(shareId)
                .map(s -> s.getUser().getUsername().equals(username))
                .orElse(false);
    }
}
