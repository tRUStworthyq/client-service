package ru.sber.clientservice.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;
import org.hibernate.annotations.Synchronize;

import java.util.ArrayList;
import java.util.List;

@Entity
@Immutable
@Subselect("SELECT id, full_name, inn FROM vw_clients")
@Synchronize("clients")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Client {

    @Id
    @Column(name = "id", length = 50)
    private String id;

    @Column(name = "full_name", nullable = false, length = 255)
    private String fullName;

    @Column(name = "inn", nullable = false, unique = true, length = 12)
    private String inn;

    @OneToMany(mappedBy = "client")
    private List<ClientDeals> deals = new ArrayList<>();
}