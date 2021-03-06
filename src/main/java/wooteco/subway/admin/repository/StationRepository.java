package wooteco.subway.admin.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import wooteco.subway.admin.domain.Station;

public interface StationRepository extends CrudRepository<Station, Long> {

    @Override
    List<Station> findAllById(Iterable ids);

    @Override
    List<Station> findAll();

    @Query("select name from station where id in (:id)")
    List<String> findAllNameById(@Param("id") List<Long> id);

    @Query("select * from station where name = :stationName")
    Optional<Station> findByName(@Param("stationName") String stationName);

    @Query("select id from station where name = :name")
    Optional<Long> findIdByName(@Param("name") String name);
}