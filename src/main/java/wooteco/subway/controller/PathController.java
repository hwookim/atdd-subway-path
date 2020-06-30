package wooteco.subway.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.domain.path.PathType;
import wooteco.subway.dto.PathResponse;
import wooteco.subway.service.LineService;

@RestController
@RequestMapping("/paths")
public class PathController {

    private LineService lineService;

    public PathController(LineService lineService) {
        this.lineService = lineService;
    }

    @GetMapping
    public ResponseEntity<PathResponse> findPath(
        @RequestParam("source") @NotNull String encodedSourceName,
        @RequestParam("target") @NotNull String encodedTargetName,
        @RequestParam("type") @NotNull String type) throws UnsupportedEncodingException {
        String sourceName = URLDecoder.decode(encodedSourceName, "UTF-8");
        String targetName = URLDecoder.decode(encodedTargetName, "UTF-8");

        PathResponse response = lineService
            .findShortestPath(sourceName, targetName, PathType.of(type));
        return ResponseEntity.ok()
            .body(response);
    }
}
