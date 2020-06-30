package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalTime;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class LineTest {

    private Line line;

    @BeforeEach
    void setUp() {
        line = new Line(1L, "2호선", LocalTime.of(05, 30), LocalTime.of(22, 30), 5);
        line.addEdge(new Edge(null, 1L, 10, 10));
        line.addEdge(new Edge(1L, 2L, 10, 10));
        line.addEdge(new Edge(2L, 3L, 10, 10));
    }

    @DisplayName("노선에 첫번째 역 추가")
    @Test
    void addEdge() {
        Long addStationId = 4L;
        line.addEdge(new Edge(null, addStationId, 10, 10));

        Assertions.assertThat(line.getEdges().getEdges()).hasSize(4);
        Edge edge = line.getEdges().getEdges().stream()
            .filter(it -> addStationId.equals(it.getPreStationId()))
            .findFirst()
            .orElseThrow(RuntimeException::new);
        assertThat(edge.getStationId()).isEqualTo(1L);
    }

    @DisplayName("노선의 지하철 역 검색")
    @Test
    void getEdges() {
        List<Long> stationIds = line.getStationIds();

        assertThat(stationIds.size()).isEqualTo(3);
        assertThat(stationIds.get(0)).isEqualTo(1L);
        assertThat(stationIds.get(1)).isEqualTo(2L);
        assertThat(stationIds.get(2)).isEqualTo(3L);
    }

    @DisplayName("노선에서 역 삭제")
    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L})
    void removeEdge(Long stationId) {
        line.removeEdgeById(stationId);

        Assertions.assertThat(line.getEdges().getEdges()).hasSize(2);
    }
}
