package org.delicias.users.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import org.delicias.address.domain.model.UserAddress;
import org.delicias.common.dto.user.UserZoneDTO;
import org.delicias.keycloack.UserKeycloakService;
import org.delicias.minio.MinioStorageService;
import org.delicias.rest.security.SecurityContextService;
import org.delicias.users.domain.model.UserInfo;
import org.delicias.users.domain.repository.UserInfoRepository;
import org.delicias.mobile.dto.CreateUserInfoReqDTO;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.locationtech.jts.geom.Point;

import java.util.*;

@ApplicationScoped
public class UserInfoService {

    @Inject
    UserInfoRepository repository;

    @Inject
    SecurityContextService security;

    @Inject
    MinioStorageService minioStorageService;

    @ConfigProperty(name = "delicias.defaultPicture")
    String defaultPicture;

    @Inject
    UserKeycloakService keycloakService;

    @Transactional
    public void create(CreateUserInfoReqDTO request) {

        String userUUID = security.userId();
        //String email = security.email();


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

    @Transactional
    public void pathInfo(Map<String, Object> payload) {

        String userUUID = security.userId();

        UserInfo entity = repository.findByIdOptional(UUID.fromString(userUUID))
                .orElseThrow(() -> new NotFoundException("Address Not Found"));

        ObjectMapper mapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        UserInfo patched = mapper.convertValue(payload, UserInfo.class);

        keycloakService.updateData(
                patched.getName(),
                patched.getLastName(),
                patched.getEmail()
        );

        if (patched.getName() != null) {
            entity.setName(patched.getName());
        }
        if(patched.getLastName() != null) {
            entity.setLastName(patched.getLastName());
        }
        if(patched.getEmail() != null) {
            entity.setEmail(patched.getEmail());
        }



    }

    public Map<String, Object> meWithFields(String fields) {

        String userUUID = security.userId();

        Set<String> fieldSet = fields != null
                ? new HashSet<>(Arrays.asList(fields.split(",")))
                : null;

        UserInfo user = repository.findById(UUID.fromString(userUUID));

        if(user == null) {
            throw new NotFoundException("Address Not Found");
        }

        return filterFields(user, fieldSet);
    }

    private Map<String, Object> filterFields(UserInfo user, Set<String> fields) {
        Map<String, Object> map = new HashMap<>();

        if (fields != null) {

            if (fields.contains("name"))
                map.put("name", user.getName());

            if (fields.contains("lastName"))
                map.put("lastName", user.getLastName());

            if(fields.contains("pictureUrl")) {
                map.put("pictureUrl", minioStorageService.thumbnailUrl(Optional.ofNullable(user.getPictureUrl()).orElse(defaultPicture)));
            }

            if (fields.contains("email"))
                map.put("email", user.getEmail());
        }

        return map;
    }

}
