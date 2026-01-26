package org.delicias.address.domain.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import org.delicias.address.domain.model.UserAddress;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class UserAddressRepository implements PanacheRepositoryBase<UserAddress, Integer>  {

    public List<UserAddress> findByUserUUID(UUID userUUID) {
        return list("userUUID", userUUID);
    }
}
