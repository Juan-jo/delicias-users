package org.delicias.mobile.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import org.delicias.address.domain.model.UserAddress;
import org.delicias.mobile.dto.CurrentDefaultAddressDTO;
import org.delicias.rest.security.SecurityContextService;
import org.delicias.users.domain.model.UserInfo;
import org.delicias.users.domain.repository.UserInfoRepository;

import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class UserDefaultAddressService {

    @Inject
    UserInfoRepository userRepository;

    @Inject
    SecurityContextService security;

    public CurrentDefaultAddressDTO getDefaultAddress() {

        UserInfo userInfo = userRepository.findById(
                UUID.fromString(security.userId())
        );

        if (userInfo == null) {
            throw new NotFoundException("User Not Found");
        }

        CurrentDefaultAddressDTO.CurrentDefaultAddressDTOBuilder response =
                CurrentDefaultAddressDTO.builder();

        boolean hasDefaultAddress = Optional.ofNullable(userInfo.getDefaultUserAddress()).isPresent();

        response.hasDefaultAddress(hasDefaultAddress);

        if(hasDefaultAddress) {

            UserAddress address = userInfo.getDefaultUserAddress();

            response.typeAddress(address.getAddressType());
            response.addressDesc(switch (address.getAddressType()) {
                case HOME, DEPTO, OTHER -> address.getDetails();
                case OFFICE -> address.getCompanyName();
            });
        }
        else {
            response.latitude(userInfo.getRegisterLat());
            response.longitude(userInfo.getRegisterLng());
        }

        return response.build();
    }
}
