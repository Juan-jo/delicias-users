package org.delicias.address.dto;

import lombok.Builder;

@Builder
public record UserAddressItemDTO(
        Integer id,
        String name,
        String address,
        String street
) { }
