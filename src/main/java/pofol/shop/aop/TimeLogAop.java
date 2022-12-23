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
 * @createdBy : 노민준(nomj18@gmail.com)
 * @createdDate : 2022-12-20
 * @lastModifiedBy : 노민준(nomj18@gmail.com)
 * @lastModifiedDate : 2022-12-21
 */
@Aspect
@Order(1)
@Slf4j
@Component
public class TimeLogAop {

    Logger logger = LoggerFactory.getLogger(TimeLogAop.class);
    Logger rootLogger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    /**
     * Controller, ApiController, Service, Repository의 모든 메소드 작업 수행시간을 로그로 남겨줍니다.
     * <br/>메소드 실행 도중 예외가 발생한다면 로그에 남겨줍니다.
     * @createdBy : 노민준(nomj18@gmail.com)
     * @createdDate : 2022-12-20
     * @lastModifiedBy : 노민준(nomj18@gmail.com)
     * @lastModifiedDate : 2022-12-21
     * @param joinPoint : AOP가 적용되는 메소드
     * @return : 실행한 메소드의 리턴값
     */
    @Around("within(pofol.shop.controller.*) || within(pofol.shop.api.*) || within(pofol.shop.service.*) || execution(* pofol.shop.repository..*(..))")
    public Object logProcessTime(ProceedingJoinPoint joinPoint) throws Throwable{

        //이름에 File이나 Crud가 들어간건 제외함
        boolean condition = !(joinPoint.toString().contains("File") || joinPoint.toString().contains("Crud"));

        long start = System.currentTimeMillis();

        if (condition) {

            if (joinPoint.toString().contains("Controller") || joinPoint.toString().contains("ApiController")) {
                //다른 컨트롤러 호출과 구분하기위해 -----표시
                logger.info("----------------------------------------------------------------");
            }

            logger.info("START: " + joinPoint.toString()); //현재 시작한 클래스, 메소드명 기록
        }

        try {
            return joinPoint.proceed();

        } catch (Exception e) {
            //메소드 실행도중 메소드에서 catch하지 못한 예외가 발생했을 경우 로그를 남겨줌
            logger.info("!!!!!!!!!!!!!!!!!!!!!!!에러발생!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            logger.info("발생지점: " + joinPoint.toString());
            logger.info("에러종류: " + e.getClass().getSimpleName() + " / " + "에러메시지: " + e.getMessage() + " / " + "자세한 내용은 error.log참조");
            throw e;

        } finally { //종료된 클래스, 메소드명과 시작부터 종료까지 총 실행시간 기록
            if (condition) {
                long finish = System.currentTimeMillis();
                long processingTime = finish - start;
                logger.info("END: " + joinPoint.toString() + " " + processingTime + "ms");
            }
        }
    }
}

