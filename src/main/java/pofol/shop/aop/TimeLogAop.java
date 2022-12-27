package pofol.shop.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Controller, Service, Repository클래스들의 메소드 작업 수행시간을 로그로 남겨주는 AOP클래스입니다.
 *
 * @createdBy : 노민준(nomj18@gmail.com)
 * @createdDate : 2022-12-20
 * @lastModifiedBy : 노민준(nomj18@gmail.com)
 * @lastModifiedDate : 2022-12-21
 */
@Aspect
@Order(0)
@Slf4j
@Component
public class TimeLogAop {

    Logger logger = LoggerFactory.getLogger(TimeLogAop.class);

    /**
     * Controller, ApiController, Service, Repository의 모든 메소드 작업 수행시간을 로그로 남겨줍니다.
     * <br/>메소드 실행 도중 예외가 발생한다면 로그에 남겨줍니다.
     *
     * @param joinPoint AOP가 적용되는 메소드
     * @return : 실행한 메소드의 리턴값
     * @createdBy : 노민준(nomj18@gmail.com)
     * @createdDate : 2022-12-20
     * @lastModifiedBy : 노민준(nomj18@gmail.com)
     * @lastModifiedDate : 2022-12-21
     */
    @Around("within(pofol.shop.controller.*) || within(pofol.shop.api.*) || within(pofol.shop.service.business.*) || execution(* pofol.shop.repository..*(..))")
    public Object logProcessTime(ProceedingJoinPoint joinPoint) throws Throwable {

        //이름에 File이나 Crud가 들어간건 제외함
        boolean isNotFileOrCrud = !(joinPoint.toString().contains("File") || joinPoint.toString().contains("Crud"));

        long start = System.currentTimeMillis();

        if (isNotFileOrCrud) {

            if (joinPoint.toString().contains("Controller") || joinPoint.toString().contains("ApiController")) {
                //다른 컨트롤러 호출과 구분하기위해 -----표시
                logger.info("----------------------------------------------------------------");
            }

            logger.info("START: " + joinPoint.toString()); //현재 시작한 클래스, 메소드명 기록
        }

        try {
            return joinPoint.proceed();

        } catch (Exception e) {
            throw e;

        } finally { //종료된 클래스, 메소드명과 시작부터 종료까지 총 실행시간 기록
            if (isNotFileOrCrud) {
                long finish = System.currentTimeMillis();
                long processingTime = finish - start;
                logger.info("END: " + joinPoint.toString() + " " + processingTime + "ms");
            }
        }
    }
}

