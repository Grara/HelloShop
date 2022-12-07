package pofol.shop.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pofol.shop.dto.ApiResponseBody;
import pofol.shop.repository.MemberRepository;
import pofol.shop.service.MemberService;


@RestController
@RequiredArgsConstructor
public class MemberApiController {
    private final MemberService memberService;
    private final MemberRepository memberRepository;

    @PostMapping("/members/duplicateCheck") //Member가입 시 회원명 중복체크 버튼
    public ResponseEntity<ApiResponseBody<Boolean>> duplicateCheck(@RequestBody String userName) {

        try {
            if (userName.equals("")) { //회원명을 입력했는지 체크
                return ResponseEntity
                        .ok()
                        .body(new ApiResponseBody<>(HttpStatus.BAD_REQUEST, "길이가 1이상이어야 합니다", false));
            }

            if (!userName.matches("[a-zA-Z0-9]*$")) { //회원명이 영어와 숫자만으로 이루어졌는지 검사
                return ResponseEntity
                        .ok()
                        .body(new ApiResponseBody<>(HttpStatus.BAD_REQUEST, "영문과 숫자만으로 이루어져야 합니다", false));
            }

            if (memberService.isDuplicate(userName)) { //중복인 경우
                return ResponseEntity
                        .ok()
                        .body(new ApiResponseBody<>(HttpStatus.ACCEPTED, "중복인 회원명이 존재합니다", false));
            } else { //사용가능한 회원명일 경우
                return ResponseEntity
                        .ok()
                        .body(new ApiResponseBody<>(HttpStatus.OK, "사용가능한 회원명입니다", true));
            }
        }catch (Exception e){ //에러 발생 시
            return ResponseEntity
                    .internalServerError()
                    .body(new ApiResponseBody<>(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null));
        }
    }

}
