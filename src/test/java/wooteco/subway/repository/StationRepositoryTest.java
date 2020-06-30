package wooteco.subway.repository;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.data.relational.core.conversion.DbActionExecutionException;
import wooteco.subway.domain.Station;

@DataJdbcTest
public class StationRepositoryTest {

    @Autowired
    private StationRepository stationRepository;

    @DisplayName("지하철 역 추가")
    @Test
    void saveStation() {
        String stationName = "강남역";
        stationRepository.save(new Station(stationName));

        assertThrows(DbActionExecutionException.class,
            () -> stationRepository.save(new Station(stationName)));
    }
}
