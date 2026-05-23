package ru.sber.clientservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.sber.clientservice.entity.Client;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, String> {
    @Query("select c from Client c join c.deals d where d.dealId = :dealId")
    Optional<Client> findByDealId(String dealId);
}
