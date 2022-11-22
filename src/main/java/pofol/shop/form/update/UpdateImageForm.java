package pofol.shop.form.update;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UpdateImageForm {
    private MultipartFile uploadImg;
}
