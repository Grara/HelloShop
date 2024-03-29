package pofol.shop.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.DispatcherServlet;
import pofol.shop.dto.security.UserAdapter;
import pofol.shop.repository.MemberRepository;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 홈과 기타화면에 해당하는 뷰를 반환하는 Controller입니다.
 *
 * @createdBy : 노민준(nomj18@gmail.com)
 * @createdDate : 2022-10-21
 * @lastModifiedBy : 노민준(nomj18@gmail.com)
 * @lastModifiedDate : 2022-12-28
 */
@Controller
@Slf4j
@RequiredArgsConstructor
public class HomeController {
    private final Environment env;
    private final MemberRepository memberRepository;
    private final HttpSessionRequestCache cache;
    private final HttpSession session;
    private final DispatcherServlet ds;

    Logger rootLogger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    @Value("${server.port}")
    private int port; //현재 포트 번호


    /**
     * 홈 화면을 반환합니다. 또한 세션의 권한이 게스트일 경우 세션을 없앱니다. (OAuth때문에 추가)
     *
     * @param principal 로그인 세션 정보
     * @createdBy : 노민준(nomj18@gmail.com)
     * @createdDate : 2022-10-21
     * @lastModifiedBy : 노민준(nomj18@gmail.com)
     * @lastModifiedDate : 2022-12-28
     */
    @GetMapping("/")
    public String home(@AuthenticationPrincipal UserAdapter principal, Model model) {

        ds.getClass();
        //OAuth로그인을 했는데 회원가입은 안한 경우
        if (principal != null && principal.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_GUEST"))) {
            SecurityContextHolder.getContext().setAuthentication(null); //로그인 세션을 없애버림

        }
        return "home";
    }

    /**
     * 어드민 메뉴 홈 화면을 반환합니다.
     *
     * @createdBy : 노민준(nomj18@gmail.com)
     * @createdDate : 2022-12-23
     * @lastModifiedBy : 노민준(nomj18@gmail.com)
     * @lastModifiedDate : 2022-12-23
     */
    @GetMapping("/admin") //어드민 메뉴 홈
    public String adminHome() {
        return "admin/home";
    }

    /**
     * 올바른 로그인 화면으로 리다이렉션해줍니다.
     *
     * @createdBy : 노민준(nomj18@gmail.com)
     * @createdDate : 2022-12-21
     * @lastModifiedBy : 노민준(nomj18@gmail.com)
     * @lastModifiedDate : 2022-12-21
     */
    @GetMapping("/login")
    public String loginForm() {
        return "redirect:/login-form";
    }


    /**
     * 현재 실행중인 WAS의 프로파일을 반환합니다.
     *
     * @createdBy : 노민준(nomj18@gmail.com)
     * @createdDate : 2022-12-16
     * @lastModifiedBy : 노민준(nomj18@gmail.com)
     * @lastModifiedDate : 2022-12-16
     */
    @ResponseBody
    @GetMapping("/profile")
    public String profile() {
        List<String> profiles = Arrays.asList(env.getActiveProfiles());
        List<String> realProfiles = Arrays.asList("real1", "real2");
        String defaultProfile = profiles.isEmpty() ? "default" : profiles.get(0);
        return profiles.stream().filter(realProfiles::contains).findAny().orElse(defaultProfile);
    }

    /**
     * 현재 실행중인 WAS의 프로파일, 포트번호, 현재시간을 반환합니다.
     *
     * @createdBy : 노민준(nomj18@gmail.com)
     * @createdDate : 2022-12-16
     * @lastModifiedBy : 노민준(nomj18@gmail.com)
     * @lastModifiedDate : 2022-12-16
     */
    @ResponseBody
    @GetMapping("/info")
    public String info() {
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



}
