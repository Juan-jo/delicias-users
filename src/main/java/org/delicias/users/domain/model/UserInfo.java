package org.delicias.users.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_info")
public class UserInfo {

    @Id
    public UUID id;

    @Column(name = "zone_id", nullable = false)
    public Integer zoneId;

    @Column(name = "register_lat", nullable = false, precision = 9, scale = 6)
    public BigDecimal registerLat;

    @Column(name = "register_lng", nullable = false, precision = 9, scale = 6)
    public BigDecimal registerLng;
}
