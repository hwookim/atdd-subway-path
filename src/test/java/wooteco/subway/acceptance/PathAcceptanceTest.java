package wooteco.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.dto.LineResponse;
import wooteco.subway.dto.PathResponse;
import wooteco.subway.dto.StationResponse;

public class PathAcceptanceTest extends AcceptanceTest {

    @DisplayName("경로 검색")
    @Test
    void managePath() {
        // given
        LineResponse line = createLine(LINE_NAME_2);

        StationResponse station1 = createStation(STATION_NAME_KANGNAM);
        StationResponse station2 = createStation(STATION_NAME_SEOLLEUNG);
        StationResponse station3 = createStation(STATION_NAME_YEOKSAM);

        addEdge(line.getId(), null, station1.getId());
        addEdge(line.getId(), station1.getId(), station2.getId(), 10, 5);
        addEdge(line.getId(), station2.getId(), station3.getId(), 10, 5);

        // when
        PathResponse response = calculatePath(station1.getName(), station3.getName(), "DISTANCE");

        // then
        assertThat(response.getDistance()).isEqualTo(20);
        assertThat(response.getDuration()).isEqualTo(10);
    }
}
