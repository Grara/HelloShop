package pofol.shop.dto.business;


import lombok.Data;
import pofol.shop.domain.Member;
import pofol.shop.domain.enums.OrderStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 주문 검색 시 조건을 지정하는 클래스입니다.
 *
 * @createdBy : 노민준(nomj18@gmail.com)
 * @createdDate : 2022-11-21
 * @lastModifiedBy : 노민준(nomj18@gmail.com)
 * @lastModifiedDate : 2022-11-21
 */
@Data
public class OrderSearchCondition {
    private String userName; //주문자
    private OrderStatus orderStatus; //주문상태
    private LocalDateTime startDate; //검색 시작날짜
    private String startDateInput; //검색 시작날짜 html form입력값
    private LocalDateTime endDate; //검색 종료날짜
    private String endDateInput; //검색 종료날짜 html form입력값
}
