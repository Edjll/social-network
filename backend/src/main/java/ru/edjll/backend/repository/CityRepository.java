package ru.edjll.backend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.edjll.backend.dto.city.CityDto;
import ru.edjll.backend.entity.City;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {

    @Query("select new ru.edjll.backend.dto.city.CityDto(c.id, c.title, c.country.id, c.country.title) from City c where c.country.id = :country_id")
    Collection<CityDto> findAllDtoByCountryId(@Param("country_id") long countryId);

    @Query("select new ru.edjll.backend.dto.city.CityDto(c.id, c.title, c.country.id, c.country.title) from City c")
    Collection<CityDto> findAllDto();

    @Query( "select new ru.edjll.backend.dto.city.CityDto(c.id, c.title, c.country.id, c.country.title) " +
            "from City c " +
            "where (:id = -1 or c.id = :id) " +
            "and (:title = '' or lower(c.title) = lower(:title)) " +
            "and (:country = '' or lower(c.country.title) = lower(:country))")
    Page<CityDto> search(@Param("id") Integer id, @Param("title") String title, @Param("country") String country, Pageable pageable);

    Boolean existsByTitleAndIdNot(String title, Long id);

    @Query("select new ru.edjll.backend.dto.city.CityDto(c.id, c.title, c.country.id, c.country.title) from City c where c.id = :id")
    Optional<CityDto> findDtoById(@Param("id") Long id);
}
