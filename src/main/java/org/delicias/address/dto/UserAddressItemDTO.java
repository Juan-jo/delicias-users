package org.delicias.address.dto;

import lombok.Builder;
import org.delicias.address.domain.enums.UserAddressType;

@Builder
public record UserAddressItemDTO(
        Integer id,
        UserAddressType addressType,
        String name,
        String address,
        String street,
        boolean isDefault
) { }
