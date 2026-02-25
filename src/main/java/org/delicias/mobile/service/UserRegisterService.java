package org.delicias.mobile.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.delicias.SecurityContextService;
import org.delicias.mobile.dto.CreateUserInfoReqDTO;
import org.delicias.users.domain.model.UserInfo;
import org.delicias.users.domain.repository.UserInfoRepository;

import java.util.UUID;

@ApplicationScoped
public class UserRegisterService {


    @Inject
    UserInfoRepository repository;

    @Inject
    SecurityContextService security;

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

}
