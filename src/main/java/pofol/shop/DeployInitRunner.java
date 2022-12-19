package pofol.shop;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

import org.springframework.stereotype.Component;
import static pofol.shop.config.DefaultValue.*;
import pofol.shop.controller.HomeController;
import pofol.shop.repository.FileRepository;

@Component
@RequiredArgsConstructor
public class DeployInitRunner implements ApplicationRunner {
    private final HomeController homeController;
    private final FileRepository fileRepository;
    @Value("${fileDir}")
    private String FILE_DIR;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if(homeController.profile().contains("real")) {
            DEFAULT_PROFILE_IMAGE_ID = fileRepository.findBySavePath(FILE_DIR + "DEFAULT_PROFILE_IMAGE.jpeg").get().getId();
            DEFAULT_ITEM_THUMBNAIL_ID = fileRepository.findBySavePath(FILE_DIR + "DEFAULT_ITEM_THUMBNAIL.png").get().getId();
            BTN_LOGIN_NAVER_ID = fileRepository.findBySavePath(FILE_DIR + "btn_login_naver.png").get().getId();
            BTN_LOGIN_GOOGLE_ID = fileRepository.findBySavePath(FILE_DIR + "btn_login_google.png").get().getId();
        }
    }
}
