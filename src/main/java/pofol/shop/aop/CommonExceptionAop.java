package pofol.shop.aop;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import pofol.shop.dto.ApiResponseBody;
import pofol.shop.service.LogService;

import java.util.NoSuchElementException;

/**
 * Controller, ApiController들의 공통적인 예외를 처리하는 AOP 클래스입니다.
 *
 * @createdBy : 노민준(nomj18@gmail.com)
 * @createdDate : 2022-12-24
 * @lastModifiedBy : 노민준(nomj18@gmail.com)
 * @lastModifiedDate : 2022-12-24
 */
@Aspect
@Order(1)
@Slf4j
@Component
@RequiredArgsConstructor
public class CommonExceptionAop {
    Logger logger = LoggerFactory.getLogger(TimeLogAop.class);
    private final LogService logService;

    /**
     * Controller와 API요청에 대한 공통적인 예외를 처리하는 메소드입니다.
     *
     * @createdBy : 노민준(nomj18@gmail.com)
     * @createdDate : 2022-12-24
     * @lastModifiedBy : 노민준(nomj18@gmail.com)
     * @lastModifiedDate : 2022-12-24
     */
    @Around("within(pofol.shop.controller.*) || within(pofol.shop.api.*)")
    public Object controllerExceptionCatch(ProceedingJoinPoint joinPoint) throws Throwable {
        boolean isNotAPI = (!joinPoint.toString().contains("Api")); //일반 Controller 요청일 경우

        try {
            return joinPoint.proceed();

        } catch (NoSuchElementException e) { //DB에서 가져온 Optional이 null일 경우
            logService.logError(joinPoint, e);
            if (isNotAPI) return "errors/noSuchElement";
            else {
                return ResponseEntity
                        .badRequest()
                        .body(new ApiResponseBody<>(HttpStatus.BAD_REQUEST, e.getMessage(), false));
            }

        } catch (AuthenticationException e) { //AuthAop에서 예외가 생겼을 경우
            logService.logError(joinPoint, e);
            if(isNotAPI) return "errors/authenticationError";
            else {
                return ResponseEntity
                        .internalServerError()
                        .body(new ApiResponseBody<>(HttpStatus.FORBIDDEN, e.getMessage(), false));
            }
        }
        catch (Exception e){ //그 외에 모든 예외
            logService.logError(joinPoint, e);
            if(isNotAPI) throw e; //에러페이지를 따로 만듬
            else {
                return ResponseEntity
                        .internalServerError()
                        .body(new ApiResponseBody<>(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), false));
            }
        }
    }
}
