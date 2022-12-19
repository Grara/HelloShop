package pofol.shop.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pofol.shop.dto.UserAdapter;

import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@Slf4j
@RequiredArgsConstructor
public class HomeController {
    private final Environment env;

    @Value("${server.port}")
    private int port;

    @GetMapping("/")
    public String home(@AuthenticationPrincipal UserAdapter adapter) {

        return "home";
    }

    @ResponseBody
    @GetMapping("/profile")
    public String profile(){
        List<String> profiles = Arrays.asList(env.getActiveProfiles());
        List<String> realProfiles = Arrays.asList("real1", "real2");
        System.out.println(profiles);
        System.out.println(realProfiles);
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


}
