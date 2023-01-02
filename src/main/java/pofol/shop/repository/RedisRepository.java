package pofol.shop.repository;

import org.springframework.data.repository.CrudRepository;
import pofol.shop.domain.Member;
import pofol.shop.domain.TestEntity;

/**
 * Redis를 사용하기 위한 임시 DAO입니다.
 *
 * @createdBy : 노민준(nomj18@gmail.com)
 * @createdDate : 2023-01-02
 * @lastModifiedBy : 노민준(nomj18@gmail.com)
 * @lastModifiedDate : 2023-01-02
 */
public interface RedisRepository extends CrudRepository<TestEntity, Long> {
}
