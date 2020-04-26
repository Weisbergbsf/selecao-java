package com.indra.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.indra.api.model.Municipio;

@Repository
public interface MunicipioRepository extends JpaRepository<Municipio, Long> {

}
