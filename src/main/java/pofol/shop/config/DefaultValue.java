package pofol.shop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pofol.shop.domain.FileEntity;

/**
 * 애플리케이션 전체에서 사용하는 상수들을 저장하는 클래스입니다.
 *
 * @createdBy : 노민준(nomj18@gmail.com)
 * @createdDate : 2022-11-21
 * @lastModifiedBy : 노민준(nomj18@gmail.com)
 * @lastModifiedDate : 2022-12-19
 */
public class DefaultValue {

    public static Long DEFAULT_PROFILE_IMAGE_ID;
    public static Long DEFAULT_ITEM_THUMBNAIL_ID;
    public static Long BTN_LOGIN_NAVER_ID;
    public static Long BTN_LOGIN_GOOGLE_ID;
}
