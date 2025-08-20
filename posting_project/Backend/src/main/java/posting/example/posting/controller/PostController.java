package posting.example.posting.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import posting.example.posting.domain.Post;
import posting.example.posting.service.PostService;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;

    // 게시글 목록
    @GetMapping
    public String list(Model model) {
        model.addAttribute("posts", postService.listAll());
        return "posts/list";
    }

    // 게시글 작성 폼
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("post", new Post());
        return "posts/form";
    }

    // 게시글 저장
    @PostMapping
    public String create(@Valid @ModelAttribute("post") Post post,
                         BindingResult br,
                         Principal principal) {
        if (br.hasErrors()) {
            return "posts/form";
        }
        postService.create(principal.getName(), post);
        return "redirect:/posts";
    }

    // 게시글 수정 폼
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id,
                               Model model,
                               Principal principal) {
        Post post = postService.update(principal.getName(), id, null);
        model.addAttribute("post", post);
        return "posts/form";
    }

    // 게시글 수정
    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("post") Post post,
                         BindingResult br,
                         Principal principal) {
        if (br.hasErrors()) {
            return "posts/form";
        }
        postService.update(principal.getName(), id, post);
        return "redirect:/posts";
    }

    // 게시글 삭제
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id,
                         Principal principal) {
        postService.delete(principal.getName(), id);
        return "redirect:/posts";
    }
}
