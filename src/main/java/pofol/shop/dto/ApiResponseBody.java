package pofol.shop.dto;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * API응답 시 사용하는 클래스입니다. <br/>
 * ReponseEntity의 Body에 담아서 사용합니다.
 *
 * @createdBy : 노민준(nomj18@gmail.com)
 * @createdDate : 2022-12-07
 * @lastModifiedBy : 노민준(nomj18@gmail.com)
 * @lastModifiedDate : 2022-12-07
 */
@Data
@NoArgsConstructor
public class ApiResponseBody<T> {
    private HttpStatus status; //상태코드
    private String message; //메세지
    private T data; //데이터

    public ApiResponseBody(HttpStatus status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

}
