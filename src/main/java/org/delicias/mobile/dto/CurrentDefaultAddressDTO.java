package org.delicias.mobile.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import org.delicias.address.domain.enums.UserAddressType;

import java.math.BigDecimal;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record CurrentDefaultAddressDTO(
        Boolean hasDefaultAddress,
        UserAddressType typeAddress,
        String addressDesc,
        BigDecimal latitude,
        BigDecimal longitude
) { }
