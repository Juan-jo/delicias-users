package org.delicias.address.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import org.delicias.SecurityContextService;
import org.delicias.address.domain.model.UserAddress;
import org.delicias.address.domain.repository.UserAddressRepository;
import org.delicias.address.dto.ConfirmAddressDTO;
import org.delicias.address.dto.CreateUserAddressReqDTO;
import org.delicias.address.dto.UserAddressItemDTO;
import org.delicias.common.dto.user.DefaultAddressDTO;
import org.delicias.common.dto.user.UserShoppingAddressDTO;
import org.delicias.users.domain.model.UserInfo;
import org.delicias.users.domain.repository.UserInfoRepository;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
public class UserAddressService {

    @Inject
    UserAddressRepository repository;

    @Inject
    UserInfoRepository userInfoRepository;

    @Inject
    SecurityContextService security;


    @Transactional
    public void create(CreateUserAddressReqDTO req) {

        GeometryFactory geometryFactory = new GeometryFactory();

        UUID userUUID = UUID.fromString(security.userId());

        UserAddress newAddress = UserAddress.builder()
                .userUUID(userUUID)
                .addressType(req.addressType())
                .indications(req.indications())
                .street(req.street())
                .address(req.address())
                .position(geometryFactory.createPoint(new Coordinate(req.longitude(), req.latitude())))
                .details(switch (req.addressType()) {
                    case HOME, DEPTO, OTHER -> req.details();
                    case OFFICE -> "";
                })
                .companyName(switch (req.addressType()) {
                    case HOME, DEPTO, OTHER -> "";
                    case OFFICE -> req.companyName();
                })
                .build();

        repository.persist(newAddress);
        evaluateSetDefaultUserAddress(newAddress);

    }


    private void evaluateSetDefaultUserAddress(UserAddress address) {

        UserInfo userInfo = userInfoRepository.findById(address.getUserUUID());

        if (userInfo == null) {
            throw new NotFoundException("User not found");
        }

        if (Optional.ofNullable(userInfo.getDefaultUserAddress()).isEmpty()) {
            userInfo.setDefaultUserAddress(address);
        }
    }

    @Transactional
    public void update(CreateUserAddressReqDTO req) {

        UserAddress userAddress = repository.findById(req.id());

        if (userAddress == null) {
            throw new NotFoundException("User address not found");
        }

        userAddress.update(req);
    }

    public Set<UserAddressItemDTO> listUserAddress() {

        UUID userUUID = UUID.fromString(security.userId());

        return repository.findByUserUUID(userUUID)
                .stream().map(it -> UserAddressItemDTO.builder()
                        .id(it.getId())
                        .name(switch (it.getAddressType()) {
                            case HOME, DEPTO, OTHER -> it.getDetails();
                            case OFFICE -> it.getCompanyName();
                        })
                        .address(it.getAddress())
                        .street(it.getStreet())
                        .build()
                ).collect(Collectors.toSet());
    }


    @Transactional
    public void delete(Integer id) {

        var deleted = repository.deleteById(id);

        if (!deleted) {
            throw new NotFoundException("User address not found");
        }
    }

    public ConfirmAddressDTO confirmAddress(
            double latitude,
            double longitude
    ) {

        // TODO Add google maps for search address

        return new ConfirmAddressDTO(
                "[Test] - Col. name",
                "Via Sin Nombre"
        );
    }

    public DefaultAddressDTO getDefaultAddress() {

        UUID userUUID = UUID.fromString(security.userId());

        UserInfo user = userInfoRepository.findById(userUUID);

        if (user == null) {
            throw new NotFoundException("User Not Found");
        }

        return Optional.ofNullable(user.getDefaultUserAddress())
                .map(it -> DefaultAddressDTO.builder()
                        .exists(true)
                        .latitude(it.getPosition().getY())
                        .longitude(it.getPosition().getX())
                        .data(DefaultAddressDTO.Data.builder()
                                .id(it.getId())
                                .name(switch (it.getAddressType()) {
                                    case HOME, DEPTO, OTHER -> it.getDetails();
                                    case OFFICE -> it.getCompanyName();
                                })
                                .address(it.getAddress())
                                .addressType(it.getAddressType().name())
                                .build())
                        .build())
                .orElse(DefaultAddressDTO.builder()
                        .exists(false)
                        .build());
    }

    public UserShoppingAddressDTO getShoppingAddressDTO(Integer addressId) {

        UserAddress address = repository.findById(addressId);

        if (address == null) {
            throw new NotFoundException("User Address Not Found");
        }

        return new UserShoppingAddressDTO(
                switch (address.getAddressType()) {
                    case HOME, DEPTO, OTHER -> address.getDetails();
                    case OFFICE -> address.getCompanyName();
                },
                address.getAddress(),
                address.getAddressType().name()
        );
    }
}
