package ru.edjll.backend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.edjll.backend.entity.Country;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {

    Page<Country> findAllByIdGreaterThanEqualAndTitleStartingWithIgnoreCase(Long id, String title, Pageable pageable);
}
