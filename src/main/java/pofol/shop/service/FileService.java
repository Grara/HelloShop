package pofol.shop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pofol.shop.domain.FileEntity;
import pofol.shop.repository.FileRepository;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileService {
    private final FileRepository fileRepository;

    @Value("${fileDir}")
    private String FILE_DIR; //기본 파일 경로

    /**
     * Form으로 제출된 파일을 기본 경로에 저장하고, 해당 파일의 FileEntity를 만들어 DB에 저장합니다.
     * @param uploadFile 저장할 파일
     * @return
     */
    public Long saveFile(MultipartFile uploadFile)throws IOException {
        if(uploadFile.getOriginalFilename().equals("")){
            throw new IllegalArgumentException("이미지 첨부 안함 or 파일 이름이 없음");
        }
        FileEntity file = new FileEntity();
        file.setOriginName(uploadFile.getOriginalFilename());
        String uuid = UUID.randomUUID().toString();
        String extention = file.getOriginName().substring(file.getOriginName().lastIndexOf("."));
        file.setSaveName(uuid + extention);
        file.setSavePath(FILE_DIR + file.getSaveName());



        uploadFile.transferTo(new File(file.getSavePath()));
        fileRepository.save(file);
        return file.getId();
    }


    /**
     * FileEntity를 DB에 저장합니다.
     * @param file 저장할 FileEntity
     * @return 저장한 FileEntity의 id
     */
    public Long initFile(FileEntity file){
        FileEntity saveFile = fileRepository.save(file);
        return saveFile.getId();
    }
}
