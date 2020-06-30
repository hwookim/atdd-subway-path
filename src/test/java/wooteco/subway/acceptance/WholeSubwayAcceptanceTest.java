package wooteco.subway.acceptance;

import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.dto.LineDetailResponse;
import wooteco.subway.dto.LineResponse;
import wooteco.subway.dto.StationResponse;

public class WholeSubwayAcceptanceTest extends AcceptanceTest {

    @DisplayName("전체 지하철 노선과 그 노선의 지하철 역 조회")
    @Test
    void test() {
        // given
        LineResponse line1 = createLine(LINE_NAME_2);
        LineResponse line2 = createLine(LINE_NAME_3);

        StationResponse station1 = createStation(STATION_NAME_KANGNAM);
        StationResponse station2 = createStation(STATION_NAME_YEOKSAM);
        StationResponse station3 = createStation(STATION_NAME_SEOLLEUNG);

        addLineStation(line1.getId(), null, station1.getId());
        addLineStation(line1.getId(), station1.getId(), station2.getId());
        addLineStation(line2.getId(), null, station3.getId());

        // when
        List<LineDetailResponse> response = getDetailLines();

        // then
        Assertions.assertThat(response).hasSize(2);
        Assertions.assertThat(response.get(0).getId()).isEqualTo(line1.getId());
        Assertions.assertThat(response.get(1).getId()).isEqualTo(line2.getId());

        Assertions.assertThat(response.get(0).getStations()).hasSize(2);
        Assertions.assertThat(response.get(1).getStations()).hasSize(1);

        Assertions.assertThat(response.get(0).getStations()).contains(station1);
        Assertions.assertThat(response.get(0).getStations()).contains(station2);
        Assertions.assertThat(response.get(1).getStations()).contains(station3);
    }
}
