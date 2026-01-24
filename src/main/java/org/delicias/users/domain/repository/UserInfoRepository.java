package org.delicias.users.domain.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import org.delicias.users.domain.model.UserInfo;

import java.util.UUID;

@ApplicationScoped
public class UserInfoRepository implements PanacheRepositoryBase<UserInfo, UUID> {

}
