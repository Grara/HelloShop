package pofol.shop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pofol.shop.domain.FileEntity;
import pofol.shop.repository.FileRepository;

import java.io.File;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.UUID;

/**
 * FileEntity와 관련된 로직을 처리하는 Service클래스입니다.
 *
 * @createdBy : 노민준(nomj18@gmail.com)
 * @createdDate : 2022-11-19
 * @lastModifiedBy : 노민준(nomj18@gmail.com)
 * @lastModifiedDate : 2022-11-30
 */
@Service
@RequiredArgsConstructor
public class FileService {
    private final FileRepository fileRepository;

    @Value("${fileDir}")
    private String FILE_DIR; //기본 파일 경로

    public FileEntity findById(Long id) {
        return fileRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("id에 해당하는 FileEntity가 없습니다. 입력한 id : " + id));
    }

    /**
     * Form으로 제출된 파일을 기본 경로에 저장하고, 해당 파일의 FileEntity를 만들어 DB에 저장합니다.
     *
     * @param uploadFile 저장할 파일
     * @return 저장한 FileEntity의 id
     * @createdBy : 노민준(nomj18@gmail.com)
     * @createdDate : 2022-11-19
     * @lastModifiedBy : 노민준(nomj18@gmail.com)
     * @lastModifiedDate : 2022-11-30
     */
    public Long saveFile(MultipartFile uploadFile) throws IOException {
        if (uploadFile.getOriginalFilename().equals("")) {
            throw new IllegalArgumentException("이미지 첨부 안함 or 파일 이름이 없음");
        }
        FileEntity file = new FileEntity(); //새로운 FileEntity 생성
        file.setOriginName(uploadFile.getOriginalFilename()); //업로드한 파일의 원래 파일명
        String uuid = UUID.randomUUID().toString(); //랜덤UUID
        String extention = file.getOriginName().substring(file.getOriginName().lastIndexOf(".")); //확장자
        file.setSaveName(uuid + extention); //랜덤UUID + 확장자로 저장
        file.setSavePath(FILE_DIR + file.getSaveName()); //경로 + 저장할 파일이름

        uploadFile.transferTo(new File(file.getSavePath())); //실제 파일 저장
        fileRepository.save(file); //FileEntity 영속화
        return file.getId();
    }


    /**
     * FileEntity를 DB에 저장합니다.
     *
     * @param file 저장할 FileEntity
     * @return 저장한 FileEntity의 id
     * @createdBy : 노민준(nomj18@gmail.com)
     * @createdDate : 2022-11-21
     * @lastModifiedBy : 노민준(nomj18@gmail.com)
     * @lastModifiedDate : 2022-11-21
     */
    public Long initFile(FileEntity file) {
        FileEntity saveFile = fileRepository.save(file);
        return saveFile.getId();
    }
}
