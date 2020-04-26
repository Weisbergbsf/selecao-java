package com.indra.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.indra.api.model.Revendedora;

@Repository
public interface RevendedoraRepository extends JpaRepository<Revendedora, Long> {

}
