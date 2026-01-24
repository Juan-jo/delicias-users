package org.delicias.users.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record CreateUserInfoReqDTO(
        @NotNull
        Integer zoneId,

        @NotNull
        @DecimalMin(value = "-90.0")
        @DecimalMax(value = "90.0")
        BigDecimal registerLat,

        @NotNull
        @DecimalMin(value = "-180.0")
        @DecimalMax(value = "180.0")
        BigDecimal registerLng
) { }
