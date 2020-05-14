package wooteco.subway.admin.service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import wooteco.subway.admin.domain.Line;
import wooteco.subway.admin.domain.LineStation;
import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.domain.path.ShortestPath;
import wooteco.subway.admin.dto.LineDetailResponse;
import wooteco.subway.admin.dto.LineRequest;
import wooteco.subway.admin.dto.LineStationCreateRequest;
import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.repository.LineRepository;
import wooteco.subway.admin.repository.StationRepository;

@Service
public class LineService {
    private LineRepository lineRepository;
    private StationRepository stationRepository;

    public LineService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public Line save(Line line) {
        return lineRepository.save(line);
    }

    public List<Line> showLines() {
        return lineRepository.findAll();
    }

    public void updateLine(Long id, LineRequest request) {
        Line persistLine = lineRepository.findById(id).orElseThrow(RuntimeException::new);
        persistLine.update(request.toLine());
        lineRepository.save(persistLine);
    }

    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

    public void addLineStation(Long id, LineStationCreateRequest request) {
        Line line = lineRepository.findById(id).orElseThrow(RuntimeException::new);
        LineStation lineStation = new LineStation(request.getPreStationId(), request.getStationId(), request.getDistance(), request.getDuration());
        line.addLineStation(lineStation);

        lineRepository.save(line);
    }

    public void removeLineStation(Long lineId, Long stationId) {
        Line line = lineRepository.findById(lineId).orElseThrow(RuntimeException::new);
        line.removeLineStationById(stationId);
        lineRepository.save(line);
    }

    public LineDetailResponse findDetailLineById(Long id) {
        Line line = lineRepository.findById(id).orElseThrow(RuntimeException::new);
        List<Station> stations = stationRepository.findAllById(line.getLineStationsId());

        // Station id
        List<Station> orderedStations = new ArrayList<>();
        for (Long stationId : line.getLineStationsId()) {
            stations.stream()
                    .filter(station -> station.getId().equals(stationId))
                    .findAny()
                    .ifPresent(orderedStations::add);
        }
        return LineDetailResponse.of(line, orderedStations);
    }

    public List<LineDetailResponse> findDetailLines() {
        List<Line> lines = lineRepository.findAll();
        List<List<Station>> stations = lines.stream()
                .map(line -> stationRepository.findAllById(line.getLineStationsId()))
                .collect(Collectors.toList());

        List<LineDetailResponse> response = new ArrayList<>();
        for (int i = 0; i < lines.size(); i++) {
            response.add(LineDetailResponse.of(lines.get(i), stations.get(i)));
        }
        return response;
    }

    public PathResponse findShortestDistancePath(String sourceName, String targetName) {
        List<LineStation> lineStations = lineRepository.findAll()
            .stream()
            .map(Line::getStations)
            .flatMap(List::stream)
            .collect(Collectors.toList());
        ShortestPath shortestPath = ShortestPath.createDistancePath(lineStations);

        Long sourceId = stationRepository.findIdByName(sourceName)
            .orElseThrow(() -> new NoSuchElementException("존재하지 않는 역입니다."));
        Long targetId = stationRepository.findIdByName(targetName)
            .orElseThrow(() -> new NoSuchElementException("존재하지 않는 역입니다."));
        List<Long> pathStationIds = shortestPath.getVertexList(sourceId, targetId);

        int distance = shortestPath.getWeight(sourceId, targetId);
        int duration = shortestPath.getSubWeight(sourceId, targetId);
        List<String> pathStationNames = stationRepository.findAllNameById(pathStationIds);

        return new PathResponse(distance, duration, pathStationNames);
    }
}
