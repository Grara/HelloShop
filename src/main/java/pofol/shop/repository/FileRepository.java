package pofol.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pofol.shop.domain.FileEntity;

public interface FileRepository extends JpaRepository<FileEntity, Long> {
}
