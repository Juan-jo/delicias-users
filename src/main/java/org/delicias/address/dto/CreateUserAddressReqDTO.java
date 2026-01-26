package org.delicias.address.dto;

import jakarta.validation.constraints.NotNull;
import org.delicias.address.domain.enums.UserAddressType;
import org.delicias.common.validation.OnCreate;
import org.delicias.common.validation.OnUpdate;

public record CreateUserAddressReqDTO(
        @NotNull(message = "The parameter is mandatory", groups = { OnUpdate.class })
        Integer id,

        @NotNull(message = "The parameter is mandatory", groups = { OnCreate.class, OnUpdate.class })
        UserAddressType addressType,

        @NotNull(message = "The parameter is mandatory", groups = { OnCreate.class, OnUpdate.class})
        String street,

        @NotNull(message = "The parameter is mandatory", groups = { OnCreate.class, OnUpdate.class})
        String address,

        @NotNull(message = "The parameter is mandatory", groups = { OnCreate.class, OnUpdate.class})
        Double latitude,

        @NotNull(message = "The parameter is mandatory", groups = { OnCreate.class, OnUpdate.class})
        Double longitude,

        @NotNull(message = "The parameter is mandatory", groups = { OnCreate.class, OnUpdate.class})
        String indications,

        String details,

        String companyName
) { }
