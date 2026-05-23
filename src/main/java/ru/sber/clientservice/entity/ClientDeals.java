package ru.sber.clientservice.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;
import org.hibernate.annotations.Synchronize;

@Entity
@Immutable
@Subselect("SELECT deal_id, client_id FROM vw_client_deals")
@Synchronize("client_deals")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ClientDeals {

    @Id
    @Column(name = "deal_id", length = 50)
    private String dealId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;
}