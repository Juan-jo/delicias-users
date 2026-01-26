package org.delicias.address.domain.model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import lombok.*;
import org.delicias.address.domain.enums.UserAddressType;

import java.util.List;
import java.util.UUID;

import org.delicias.address.dto.CreateUserAddressReqDTO;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;


@Entity
@Table(name = "user_address")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserAddress extends PanacheEntityBase {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_address_id_seq")
    @SequenceGenerator(
            name = "user_address_id_seq",
            allocationSize = 1
    )
    private Integer id;

    @Column(name = "user_uuid")
    private UUID userUUID;


    @Column(name = "position", columnDefinition = "GEOGRAPHY(Point, 4326)")
    //@Column(columnDefinition = "geometry(Point, 4326)")
    private Point position;

    @Column(name = "type_address")
    @Enumerated(EnumType.STRING)
    private UserAddressType addressType;

    private String details;

    @Column(name = "company_name")
    private String companyName;

    private String street;

    private String address;

    private String indications;

    public void update(CreateUserAddressReqDTO req) {

        this.addressType = req.addressType();
        this.street = req.street();
        this.address = req.address();
        this.indications = req.indications();

        GeometryFactory geometryFactory = new GeometryFactory();
        this.position = geometryFactory.createPoint(
                new Coordinate(req.longitude(), req.latitude()));

        details = switch (addressType) {
            case HOME, DEPTO, OTHER -> req.details();
            case OFFICE -> "";
        };

        companyName = switch (addressType) {
            case HOME, DEPTO, OTHER -> "";
            case OFFICE -> req.companyName();
        };
    }

    public static List<UserAddress> findByUser(UUID uuid) {
        return list("user_uuid", uuid);
    }
}
