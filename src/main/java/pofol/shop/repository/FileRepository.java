package pofol.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pofol.shop.domain.FileEntity;

import java.util.Optional;

/**
 * 스프링데이터JPA를 사용한 FileEntity 관련 DAO입니다.
 *
 * @createdBy : 노민준(nomj18@gmail.com)
 * @createdDate : 2022-11-19
 * @lastModifiedBy : 노민준(nomj18@gmail.com)
 * @lastModifiedDate : 2022-12-19
 */
public interface FileRepository extends JpaRepository<FileEntity, Long> {

    /**
     * 저장경로로 FileEntity를 찾아냅니다.
     *
     * @param savePath 찾을 FileEntity의 저장경로
     * @return 찾아낸 FileEntity를 담은 Optional객체
     * @createdBy : 노민준(nomj18@gmail.com)
     * @createdDate : 2022-12-19
     * @lastModifiedBy : 노민준(nomj18@gmail.com)
     * @lastModifiedDate : 2022-12-19
     */
    Optional<FileEntity> findBySavePath(String savePath);
}
