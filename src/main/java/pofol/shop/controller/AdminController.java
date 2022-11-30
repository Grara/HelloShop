package pofol.shop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import pofol.shop.domain.Member;
import pofol.shop.domain.Order;
import pofol.shop.domain.enums.Role;
import pofol.shop.dto.OrderSearchCondition;
import pofol.shop.repository.MemberRepository;
import pofol.shop.repository.OrderRepository;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class AdminController {

    private final OrderRepository orderRepository;

    @GetMapping("/admin") //어드민 메뉴 홈
    public String adminHome(){
        return "admin/home";
    }

}
