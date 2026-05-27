package org.delicias.mobile.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import org.delicias.keycloack.UserKeycloakService;
import org.delicias.mobile.dto.CreateUserInfoReqDTO;
import org.delicias.mobile.dto.UpdateUserInfoReqDTO;
import org.delicias.rest.security.SecurityContextService;
import org.delicias.users.domain.model.UserInfo;
import org.delicias.users.domain.repository.UserInfoRepository;

import java.util.UUID;

@ApplicationScoped
public class UserRegisterService {


    @Inject
    UserInfoRepository repository;

    @Inject
    SecurityContextService security;

    @Inject
    UserKeycloakService keycloakService;

    @Transactional
    public void registerUser(CreateUserInfoReqDTO request) {

        String userUUID = security.userId();

        UserInfo userCurrent = repository.findById(UUID.fromString(userUUID));

        if (userCurrent == null) {
            UserInfo user = UserInfo.builder()
                    .id(UUID.fromString(userUUID))
                    .zoneId(request.zoneId())
                    .registerLat(request.registerLat())
                    .registerLng(request.registerLng())
                    .build();

            repository.persist(user);
        }
    }

    @Transactional
    public void updateInfo(UpdateUserInfoReqDTO req) {
        String userUUID = security.userId();

        UserInfo userCurrent = repository.findByIdOptional(UUID.fromString(userUUID))
                        .orElseThrow(() -> new NotFoundException("Not Found User"));

        keycloakService.updateData(req.name(), req.lastName(), req.email());

        userCurrent.setName(req.name());
        userCurrent.setLastName(req.lastName());
        userCurrent.setEmail(req.email());
    }

}
