package wooteco.subway.controller;

import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.domain.Line;
import wooteco.subway.dto.EdgeCreateRequest;
import wooteco.subway.dto.LineDetailResponse;
import wooteco.subway.dto.LineRequest;
import wooteco.subway.dto.LineResponse;
import wooteco.subway.service.LineService;

@RestController
public class LineController {

    private LineService lineService;

    public LineController(LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping(value = "/lines")
    public ResponseEntity<LineResponse> createLine(@RequestBody LineRequest view) {
        Line persistLine = lineService.save(view.toLine());
        return ResponseEntity
            .created(URI.create("/lines/" + persistLine.getId()))
            .body(LineResponse.of(persistLine));
    }

    @GetMapping("/lines")
    public ResponseEntity<List<LineResponse>> showLines() {
        List<LineResponse> response = LineResponse.listOf(lineService.showLines());
        return ResponseEntity.ok()
            .body(response);
    }

    @GetMapping("/lines/detail")
    public ResponseEntity<List<LineDetailResponse>> showDetailLines() {
        List<LineDetailResponse> response = lineService.findDetailLines();
        return ResponseEntity.ok()
            .body(response);
    }

    @GetMapping("/lines/{id}")
    public ResponseEntity<LineDetailResponse> showDetailLine(@PathVariable Long id) {
        LineDetailResponse response = lineService.findDetailLineById(id);
        return ResponseEntity.ok()
            .body(response);
    }

    @PutMapping("/lines/{id}")
    public ResponseEntity<Void> updateLine(@PathVariable Long id, @RequestBody LineRequest view) {
        lineService.updateLine(id, view);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/lines/{id}")
    public ResponseEntity<Void> deleteLine(@PathVariable Long id) {
        lineService.deleteLineById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/lines/{lineId}/stations")
    public ResponseEntity<Void> addEdge(@PathVariable Long lineId,
        @RequestBody EdgeCreateRequest view) {
        lineService.addEdge(lineId, view);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/lines/{lineId}/stations/{stationId}")
    public ResponseEntity<Void> removeEdge(@PathVariable Long lineId,
        @PathVariable Long stationId) {
        lineService.removeEdge(lineId, stationId);
        return ResponseEntity.noContent().build();
    }
}
