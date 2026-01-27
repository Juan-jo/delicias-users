package org.delicias.users.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import org.delicias.SecurityContextService;
import org.delicias.common.dto.UserZoneDTO;
import org.delicias.users.domain.model.UserInfo;
import org.delicias.users.domain.repository.UserInfoRepository;
import org.delicias.users.dto.CreateUserInfoReqDTO;

import java.util.UUID;

@ApplicationScoped
public class UserInfoService {

    @Inject
    UserInfoRepository repository;

    @Inject
    SecurityContextService security;


    @Transactional
    public void create(CreateUserInfoReqDTO request) {

        String userUUID = security.userId();
        String email = security.email();


        UserInfo user = UserInfo.builder()
                .id(UUID.fromString(userUUID))
                .zoneId(request.zoneId())
                .registerLat(request.registerLat())
                .registerLng(request.registerLng())
                .build();
        repository.persist(user);
    }

    @Transactional
    public void delete(UUID id) {
        boolean deleted = repository.deleteById(id);

        if (!deleted) {
            throw new NotFoundException("User not found");
        }
    }

    public UserZoneDTO getUserZone(UUID userUUID) {

        UserInfo userInfo = repository.findById(userUUID);

        if (userInfo == null) {
            throw new NotFoundException("User not found");
        }


        return new UserZoneDTO(userInfo.id, userInfo.zoneId);

    }

}
