package org.delicias.mobile.dto;

import jakarta.validation.constraints.NotNull;

public record UpdateUserInfoReqDTO(

        @NotNull
        String name,


        String lastName,

        @NotNull
        String email
) { }
