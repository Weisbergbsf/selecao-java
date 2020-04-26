package com.indra.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.indra.api.model.Bandeira;

@Repository
public interface BandeiraRepository extends JpaRepository<Bandeira, Long> {

}
