package posting.example.posting.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import posting.example.posting.service.UserService;
import posting.example.posting.service.dto.UserDto;

import javax.naming.Binding;

@Controller
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;

    // 회원가입 폼 (GetMappring : 데이터 조회)
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("userDto", new UserDto());
        return "auth/register";
    }

    // 회원가입 처리 (PostMappring : 상태 변경)
    @PostMapping("/register")
    public String register(@Valid UserDto userDto, BindingResult br, Model model){
        if (br.hasErrors()) {
            return "auth/register";
        }
        try{
            userService.register(userDto);
        } catch (IllegalArgumentException ex){
            model.addAttribute("registerError", ex.getMessage());
            return "auth/register";
        }
        return "redirect:/auth/login";
    }

    // 로그인 폼
    @GetMapping("/login")
    public String showLoginForm(){
        return "auth/login";
    }

}
