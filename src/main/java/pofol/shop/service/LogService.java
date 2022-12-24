package pofol.shop.service;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pofol.shop.aop.TimeLogAop;

/**
 * 로그와 관련된 복잡한 로직을 처리해주는 Service 클래스입니다.
 *
 * @createdBy : 노민준(nomj18@gmail.com)
 * @createdDate : 2022-12-20
 * @lastModifiedBy : 노민준(nomj18@gmail.com)
 * @lastModifiedDate : 2022-12-24
 */
@Service
public class LogService {

    private final Logger rootLogger = LoggerFactory.getLogger(this.getClass().getSimpleName());
    private final Logger logger = LoggerFactory.getLogger(TimeLogAop.class);

    /**
     * 예외 발생 시 에러 로그를 남겨줍니다.
     *
     * @param joinPoint 실행중인 JoinPoint
     * @param e         발생한 예외
     * @createdBy : 노민준(nomj18@gmail.com)
     * @createdDate : 2022-12-24
     * @lastModifiedBy : 노민준(nomj18@gmail.com)
     * @lastModifiedDate : 2022-12-24
     */
    public void logError(JoinPoint joinPoint, Exception e) {
        StackTraceElement[] stackTraces = e.getStackTrace();
        String result = stackTraces[0].toString();
        for (int i = 1; i < stackTraces.length; i++) {
            result = result + "\n    at " + stackTraces[i].toString();
        }
        rootLogger.error(result);

        logger.info("!!!!!!!!!!!!!!!!!!!!!!!에러발생!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        logger.info("발생지점: " + joinPoint.toString());
        logger.info("에러종류: " + e + " / " + "에러메시지: " + e.getMessage() + " / " + "자세한 내용은 error.log참조");
    }
}
