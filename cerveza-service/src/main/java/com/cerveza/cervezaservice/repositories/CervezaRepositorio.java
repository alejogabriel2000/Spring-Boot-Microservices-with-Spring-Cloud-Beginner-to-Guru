package com.cerveza.cervezaservice.repositories;

import com.cerveza.cervezaservice.domain.Cerveza;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.UUID;

public interface CervezaRepositorio extends PagingAndSortingRepository<Cerveza, UUID> {
}
