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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pofol.shop.dto.ApiResponseBody;
import pofol.shop.dto.security.UserAdapter;
import pofol.shop.form.UserNameRequiredForm;
import pofol.shop.service.LogService;

/**
 * Controller, ApiController들의 메소드 중 인증정보가 필요한 메소드들에게 필요한 공통 작업을 처리하는 AOP클래스입니다.
 *
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
    private final LogService logService;
    Logger logger = LoggerFactory.getLogger(TimeLogAop.class);

    /**
     * Controller와 API요청 중 로그인 세션이 필요한 요청을 호출할 때 세션이 null인지 체크합니다.<br/>
     * null이라면 AuthenticationException를 던집니다.
     *
     * @param joinPoint 실행할 메소드
     * @param principal 메소드의 인자로 들어온 로그인 세션 정보
     * @return : 로그인을 안했으면 에러를 반환
     * @createdBy : 노민준(nomj18@gmail.com)
     * @createdDate : 2022-12-23
     * @lastModifiedBy : 노민준(nomj18@gmail.com)
     * @lastModifiedDate : 2022-12-23
     */
    @Order(1)
    @Around("( execution(* pofol.shop.api..*(..)) || execution(* pofol.shop.controller..*(..)) ) && args(.., principal)")
    public Object apiAuthNullCheck(ProceedingJoinPoint joinPoint, UserAdapter principal) throws Throwable {
        if (principal == null) {
            throw new AuthenticationException("로그인이 필요합니다.") {
            };
        }
        return joinPoint.proceed();
    }

    /**
     * Controller와 API요청 중 폼의 대상 회원과 요청을 보낸 로그인 세션이 일치하는지 확인합니다. <br/>
     * 일치하지 않으면 AuthenticationException을 던집니다.
     *
     * @param joinPoint 실행할 메소드
     * @param form      대상이 되는 유저이름을 필드로 지닌 폼 객체
     * @param principal 메소드의 인자로 들어온 로그인 세션 정보
     * @createdBy : 노민준(nomj18@gmail.com)
     * @createdDate : 2022-12-23
     * @lastModifiedBy : 노민준(nomj18@gmail.com)
     * @lastModifiedDate : 2022-12-23
     */
    @Order(2)
    @Around("( execution(* pofol.shop.api..*(..)) || execution(* pofol.shop.controller..*(..)) )&& args(form,.., principal)")
    public Object apiSessionCheck(ProceedingJoinPoint joinPoint, UserNameRequiredForm form, UserAdapter principal) throws Throwable {
        if (!form.getUserName().equals(principal.getUsername())) {
            throw new AuthenticationException("요청을 보낸 회원과 대상이 되는 회원이 다릅니다.") {
            };
        }
        return joinPoint.proceed();
    }
}
