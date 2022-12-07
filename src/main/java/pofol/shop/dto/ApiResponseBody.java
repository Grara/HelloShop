package pofol.shop.dto;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@NoArgsConstructor
public class ApiResponseBody<T> {
    private HttpStatus status;
    private String message;
    private T data;

    public ApiResponseBody(HttpStatus status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

}
