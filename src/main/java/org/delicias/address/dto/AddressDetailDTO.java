package org.delicias.address.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import org.delicias.address.domain.enums.UserAddressType;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record AddressDetailDTO(
        Integer id,
        double latitude,
        double longitud,
        UserAddressType typeAddress,
        String details,
        String companyName,
        String street,
        String address,
        String indications
) { }
