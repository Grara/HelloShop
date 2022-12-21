package pofol.shop.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Slf4j
@Component
public class TimeLogAop {

    Logger logger = LoggerFactory.getLogger(TimeLogAop.class);

    @Around("execution(* pofol.shop.controller..*(..))")
    public Object logController(ProceedingJoinPoint joinPoint) throws Throwable {
        boolean condition = !(joinPoint.toString().contains("File") || joinPoint.toString().contains("Crud"));

        long start = System.currentTimeMillis();
        if (condition) {
            logger.info("----------------------------------------------------------------");
            logger.info("START: " + joinPoint.toString());
        }

        try {
            return joinPoint.proceed();

        } catch (Exception e) {
            logger.info("!!!!!!!!!!!!!!!!!!!!!!!에러발생!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            logger.info("발생지점: " + joinPoint.toString());
            logger.info("에러종류: " + e.getClass().getSimpleName() + " / " + "에러메시지: " + e.getMessage() + " / " + "자세한 내용은 error.log참조");
            throw e;

        } finally {
            if (condition) {
                long finish = System.currentTimeMillis();
                long processingTime = finish - start;
                logger.info("END: " + joinPoint.toString() + " " + processingTime + "ms");
            }
        }
    }

    @Around("execution(* pofol.shop.api..*(..))")
    public Object logApiController(ProceedingJoinPoint joinPoint) throws Throwable {
        boolean condition = !(joinPoint.toString().contains("File") || joinPoint.toString().contains("Crud"));

        long start = System.currentTimeMillis();
        if (condition) {
            logger.info("----------------------------------------------------------------");
            logger.info("START: " + joinPoint.toString());
        }

        try {
            return joinPoint.proceed();

        } catch (Exception e) {
            logger.info("!!!!!!!!!!!!!!!!!!!!!!!에러발생!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            logger.info("발생지점: " + joinPoint.toString());
            logger.info("에러종류: " + e.getClass().getSimpleName() + " / " + "에러메시지: " + e.getMessage() + " / " + "자세한 내용은 error.log참조");
            throw e;

        } finally {
            if (condition) {
                long finish = System.currentTimeMillis();
                long processingTime = finish - start;
                logger.info("END: " + joinPoint.toString() + " " + processingTime + "ms");
            }
        }
    }

    @Around("execution(* pofol.shop.service..*(..))")
    public Object logService(ProceedingJoinPoint joinPoint) throws Throwable {
        boolean condition = !(joinPoint.toString().contains("File") || joinPoint.toString().contains("Crud"));

        long start = System.currentTimeMillis();
        if (condition) {
            logger.info("START: " + joinPoint.toString());
        }

        try {
            return joinPoint.proceed();
        } catch (Exception e) {
            logger.info("에러발생!!");
            logger.info("발생지점: " + joinPoint.toString());
            logger.info("에러종류: " + e.getClass().getSimpleName() + " / " + "에러메시지: " + e.getMessage() + " / " + "자세한 내용은 error.log참조");
            throw e;
        } finally {
            if (condition) {
                long finish = System.currentTimeMillis();
                long processingTime = finish - start;
                logger.info("END: " + joinPoint.toString() + " " + processingTime + "ms");
            }
        }
    }

    @Around("execution(* pofol.shop.repository..*(..))")
    public Object logRepository(ProceedingJoinPoint joinPoint) throws Throwable {
        boolean condition = !(joinPoint.toString().contains("File") || joinPoint.toString().contains("Crud"));

        long start = System.currentTimeMillis();
        if (condition) {
            logger.info("START: " + joinPoint.toString());
        }

        try {
            return joinPoint.proceed();
        } catch (Exception e) {
            logger.info("에러발생!!");
            logger.info("발생지점: " + joinPoint.toString());
            logger.info("에러종류: " + e.getClass().getSimpleName() + " / " + "에러메시지: " + e.getMessage() + " / " + "자세한 내용은 error.log참조");
            throw e;
        } finally {
            if (condition) {
                long finish = System.currentTimeMillis();
                long processingTime = finish - start;
                logger.info("END: " + joinPoint.toString() + " " + processingTime + "ms");
            }
        }
    }
}



