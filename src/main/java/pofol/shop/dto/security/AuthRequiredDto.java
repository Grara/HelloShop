package pofol.shop.dto.security;

import lombok.Data;

@Data
public abstract class AuthRequiredDto {
    private Long MemberId;
}
