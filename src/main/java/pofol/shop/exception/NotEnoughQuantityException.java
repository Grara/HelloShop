package pofol.shop.exception;

/**
 * 상품의 재고가 부족할 때 발생하는 예외입니다.
 *
 * @createdBy : 노민준(nomj18@gmail.com)
 * @createdDate : 2022-11-21
 * @lastModifiedBy : 노민준(nomj18@gmail.com)
 * @lastModifiedDate : 2022-11-21
 */
public class NotEnoughQuantityException extends RuntimeException {

    public NotEnoughQuantityException(String msg) { super(msg);}
}
