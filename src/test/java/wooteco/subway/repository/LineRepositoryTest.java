package wooteco.subway.repository;

import java.time.LocalTime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import wooteco.subway.domain.Edge;
import wooteco.subway.domain.Line;

@DataJdbcTest
public class LineRepositoryTest {

    @Autowired
    private LineRepository lineRepository;

    @DisplayName("노선에 지하철 역 추가")
    @Test
    void addEdge() {
        // given
        Line line = new Line("2호선", LocalTime.of(05, 30), LocalTime.of(22, 30), 5);
        Line persistLine = lineRepository.save(line);
        persistLine.addEdge(new Edge(null, 1L, 10, 10));
        persistLine.addEdge(new Edge(1L, 2L, 10, 10));

        // when
        Line resultLine = lineRepository.save(persistLine);

        // then
        Assertions.assertThat(resultLine.getEdges().getEdges()).hasSize(2);
    }
}
