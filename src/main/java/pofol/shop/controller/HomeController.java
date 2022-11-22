package pofol.shop.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pofol.shop.domain.Member;
import pofol.shop.form.TestForm;
import pofol.shop.service.MemberService;

import java.security.Principal;

@Controller
@Slf4j
@RequiredArgsConstructor
public class HomeController {
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;
    @RequestMapping("/")
    public String home(){
        return "home";
    }

    @GetMapping("/password")
    public String test1(){
        return "test";
    }

    @PostMapping("/password")
    public String test(TestForm form, Principal principal) throws Exception {
        Member member = memberService.findOneByName(principal.getName());
        if(passwordEncoder.matches(form.getPassword(), member.getPassword())){
            System.out.println("######" + "true");
        }
        else System.out.println("#######" + "false");

        return "test";
    }
}
