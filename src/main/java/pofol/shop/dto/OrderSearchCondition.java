package pofol.shop.dto;


import lombok.Data;
import pofol.shop.domain.Member;
import pofol.shop.domain.enums.OrderStatus;

@Data
public class OrderSearchCondition {
    private Member member;
    private OrderStatus orderStatus;
}
