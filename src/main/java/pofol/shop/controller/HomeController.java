package pofol.shop.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pofol.shop.domain.Member;
import pofol.shop.dto.MemberDto;
import pofol.shop.dto.UserAdapter;
import pofol.shop.form.LoginForm;
import pofol.shop.form.TestForm;
import pofol.shop.repository.MemberRepository;
import pofol.shop.service.MemberService;

import java.security.Principal;
import java.util.Collection;
import java.util.Optional;

@Controller
@Slf4j
@RequiredArgsConstructor
public class HomeController {
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final AuthenticationManager authenticationManager;

    @GetMapping("/")
    public String home(@AuthenticationPrincipal UserAdapter adapter) {

        return "home";
    }

    @GetMapping("/login")
    public String test1(Model model, @AuthenticationPrincipal UserAdapter adapter, Authentication authentication) {


        model.addAttribute("a", "/orders");
        return "test";
    }

    @GetMapping("/login-form")
    public String loginForm(@RequestParam(value = "error", required = false)boolean error, Model model){
        LoginForm form = new LoginForm();
        model.addAttribute("loginForm", form);
        model.addAttribute("hasError", error);
        return "loginForm";
    }

}
