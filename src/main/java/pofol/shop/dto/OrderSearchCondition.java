package pofol.shop.dto;


import lombok.Data;
import pofol.shop.domain.Member;
import pofol.shop.domain.enums.OrderStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class OrderSearchCondition {
    private String userName;
    private OrderStatus orderStatus;
    private LocalDateTime startDate;
    private String startDateInput;
    private LocalDateTime endDate;
    private String endDateInput;
}
