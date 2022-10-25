package pofol.shop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CartController {

    @GetMapping("/cart/new")
    public String addItemToCart(@RequestParam("itemId")Long itemId,
                       @RequestParam("count")Integer count,
                       @RequestParam("userName") String userName){

        return "hello";
    }
}
