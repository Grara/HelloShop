package pofol.shop.aop;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pofol.shop.dto.ApiResponseBody;
import pofol.shop.dto.security.UserAdapter;

/**
 * Controller, ApiController들의 메소드 중 인증정보가 필요한 메소드들에게 필요한 공통 작업을 처리하는 AOP클래스입니다.
 * @createdBy : 노민준(nomj18@gmail.com)
 * @createdDate : 2022-12-23
 * @lastModifiedBy : 노민준(nomj18@gmail.com)
 * @lastModifiedDate : 2022-12-23
 */

@Aspect
@Order(2)
@Slf4j
@Component
@RequiredArgsConstructor
public class AuthAop {

    private final PasswordEncoder encoder;

    /**
     * API요청 중 로그인 세션이 필요한 요청을 호출할 때 로그인을 안했으면 500번 응답을 반환합니다.
     * @createdBy : 노민준(nomj18@gmail.com)
     * @createdDate : 2022-12-23
     * @lastModifiedBy : 노민준(nomj18@gmail.com)
     * @lastModifiedDate : 2022-12-23
     * @param joinPoint : 실행할 메소드
     * @param principal : 메소드의 로그인 세션 정보
     * @return : 로그인을 안했으면 에러를 반환
     */
    @Around("execution(* pofol.shop.api..*(..)) && args(.., principal)")
    public Object authNullCheck(ProceedingJoinPoint joinPoint, UserAdapter principal) throws Throwable{
        if(principal == null){
            AuthenticationException e = new AuthenticationException("로그인이 필요합니다.") {
            };

            return ResponseEntity
                    .internalServerError()
                    .body(new ApiResponseBody<>(HttpStatus.FORBIDDEN, e.getMessage(), false));
        }
        return joinPoint.proceed();
    }
}
