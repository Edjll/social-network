package ru.edjll.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.edjll.backend.entity.City;

import java.util.Collection;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {

    Collection<City> findAllByCountryId(long countryId);
}
