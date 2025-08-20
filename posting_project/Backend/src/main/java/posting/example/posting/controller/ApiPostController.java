package posting.example.posting.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import posting.example.posting.domain.Post;
import posting.example.posting.service.PostService;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class ApiPostController {

    private final PostService postService;

    /** 전체 게시글 조회 */
    @GetMapping
    public List<Post> getAll() {
        return postService.listAll();
    }

    /** 새 게시글 생성 */
    @PostMapping
    public Post create(
            @RequestParam String username,
            @RequestBody Post post
    ) {
        return postService.create(username, post);
    }

    /** 게시글 수정 */
    @PutMapping("/{id}")
    public Post update(
            @PathVariable Long id,
            @RequestParam String username,
            @RequestBody Post post
    ) {
        return postService.update(username, id, post);
    }

    /** 게시글 삭제 */
    @DeleteMapping("/{id}")
    public void delete(
            @PathVariable Long id,
            @RequestParam String username
    ) {
        postService.delete(username, id);
    }
}
