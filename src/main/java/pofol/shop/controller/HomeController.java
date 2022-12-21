package pofol.shop.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.data.convert.DtoInstantiatingConverter;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pofol.shop.domain.Member;
import pofol.shop.dto.TestDto;
import pofol.shop.dto.UserAdapter;
import pofol.shop.repository.MemberRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@Slf4j
@RequiredArgsConstructor
public class HomeController {
    private final Environment env;
    private final MemberRepository memberRepository;

    @Value("${server.port}")
    private int port;

    @GetMapping("/")
    public String home(@AuthenticationPrincipal UserAdapter adapter) {

        //OAuth로그인을 했는데 회원가입은 안한 경우
        if(adapter != null && adapter.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_GUEST"))){
            SecurityContextHolder.getContext().setAuthentication(null);
        }
        return "home";
    }

    @GetMapping("/login")
    public String goToLoginForm(){
        return "redirect:/login-form";
    }

    @ResponseBody
    @GetMapping("/profile")
    public String profile(){
        List<String> profiles = Arrays.asList(env.getActiveProfiles());
        List<String> realProfiles = Arrays.asList("real1", "real2");
        String defaultProfile = profiles.isEmpty() ? "default" : profiles.get(0);
        return profiles.stream().filter(realProfiles::contains).findAny().orElse(defaultProfile);
    }

    @ResponseBody
    @GetMapping("/info")
    public String info(){
        List<String> profiles = Arrays.asList(env.getActiveProfiles());
        List<String> realProfiles = Arrays.asList("real1", "real2");
        String defaultProfile = profiles.isEmpty() ? "default" : profiles.get(0);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
        Date date = new Date();
        TimeZone tz = TimeZone.getTimeZone("Asia/Seoul");
        sdf.setTimeZone(tz);
        String text = sdf.format(date);

        return profiles.stream().filter(realProfiles::contains).findAny().orElse(defaultProfile) + " / port:" + port + " / time:" + text;

    }

    @GetMapping("/test")
    public String test(Model model, HttpServletRequest request){
        Member member = memberRepository.findByUserName("user").get();
        HttpSession session = request.getSession();
        session.setAttribute("user", new TestDto(member));

        TestDto dd = new TestDto("dd");
        model.addAttribute("test", dd);
        return "test";
    }

}
