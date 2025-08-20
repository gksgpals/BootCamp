package posting.example.posting.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import posting.example.posting.domain.Post;
import posting.example.posting.domain.User;
import posting.example.posting.repository.PostRepository;
import posting.example.posting.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepo;
    private final UserRepository userRepo;

    // 게시글 생성
    @Transactional
    public Post create(String username, Post post){
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException(username + " 존재하지 않는 사용자 이름입니다."));
        post.setUser(user);
        return postRepo.save(post);
    }

    // 게시글 수정 : 관리자나 게시글 소유자만 가능
    @PreAuthorize("hasRole('ADMIN') or @postService.isOwner(#username, #postId)")
    @Transactional
    public Post update(String username, Long postId, Post data){
        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException(postId + "에 해당하는 게시글이 없습니다."));
        // 수정된 부분
        post.setTitle(data.getTitle());
        post.setContent(data.getContent());
        return postRepo.save(post);
    }

    // 게시글 삭제 : 관리자나 게시글 소유자만 가능
    @PreAuthorize("hasRole('ADMIN') or @postService.isOwner(#username, #postId)")
    @Transactional
    public void delete(String username, Long postId){
        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException(postId + "에 해당하는 게시글이 없습니다."));
        postRepo.delete(post);
    }

    // 모든 게시글 조회
    @Transactional(readOnly = true)
    public List<Post> listAll(){
        return postRepo.findAll();
    }

    // 해당 게시글이 작성자인지 여부 확인
    public boolean isOwner(String username, Long postId){
        return postRepo.findById(postId)
                .map(p -> p.getUser().getUsername().equals(username))
                .orElse(false);
    }
}
