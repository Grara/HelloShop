package pofol.shop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import pofol.shop.service.OrderService;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;


}
