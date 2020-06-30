package wooteco.subway.api;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import wooteco.subway.config.ETagHeaderFilter;
import wooteco.subway.controller.LineController;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.LineDetailResponse;
import wooteco.subway.service.LineService;

@WebMvcTest(controllers = {LineController.class})
@Import(ETagHeaderFilter.class)
public class LineControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LineService lineService;

    @DisplayName("전체 지하철 노선도 조회")
    @Test
    void ETag() throws Exception {
        List<LineDetailResponse> response = Arrays
            .asList(createMockResponse(), createMockResponse());
        given(lineService.findDetailLines()).willReturn(response);

        String uri = "/lines/detail";

        MvcResult mvcResult = mockMvc.perform(get(uri))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(header().exists("ETag"))
            .andReturn();

        String eTag = mvcResult.getResponse().getHeader("ETag");

        mockMvc.perform(get(uri).header("If-None-Match", eTag))
            .andDo(print())
            .andExpect(status().isNotModified())
            .andExpect(header().exists("ETag"))
            .andReturn();
    }

    private LineDetailResponse createMockResponse() {
        List<Station> stations = Arrays.asList(new Station(), new Station(), new Station());
        return LineDetailResponse.of(new Line(), stations);
    }
}