package wooteco.subway.service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import wooteco.subway.domain.Edge;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Station;
import wooteco.subway.domain.path.PathType;
import wooteco.subway.domain.path.ShortestPath;
import wooteco.subway.domain.path.ShortestPathFactory;
import wooteco.subway.domain.vo.Edges;
import wooteco.subway.dto.EdgeCreateRequest;
import wooteco.subway.dto.LineDetailResponse;
import wooteco.subway.dto.LineRequest;
import wooteco.subway.dto.PathResponse;
import wooteco.subway.repository.LineRepository;
import wooteco.subway.repository.StationRepository;

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
        Line persistLine = lineRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("노선이 존재하지 않습니다."));
        persistLine.update(request.toLine());
        lineRepository.save(persistLine);
    }

    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

    public void addEdge(Long id, EdgeCreateRequest request) {
        Line line = lineRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("노선이 존재하지 않습니다."));
        Edge edge = new Edge(request.getPreStationId(), request.getStationId(),
            request.getDistance(), request.getDuration());
        line.addEdge(edge);
        lineRepository.save(line);
    }

    public void removeEdge(Long lineId, Long stationId) {
        Line line = lineRepository.findById(lineId)
            .orElseThrow(() -> new NoSuchElementException("노선이 존재하지 않습니다."));
        line.removeEdgeById(stationId);
        lineRepository.save(line);
    }

    public LineDetailResponse findDetailLineById(Long id) {
        Line line = lineRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("노선이 존재하지 않습니다."));
        List<Station> stations = stationRepository.findAllById(line.getStationIds());

        List<Station> orderedStations = new ArrayList<>();
        for (Long stationId : line.getStationIds()) {
            stations.stream()
                .filter(station -> station.getId().equals(stationId))
                .findAny()
                .ifPresent(orderedStations::add);
        }
        return LineDetailResponse.of(line, orderedStations);
    }

    public List<LineDetailResponse> findDetailLines() {
        List<LineDetailResponse> response = new ArrayList<>();
        for (Line line : lineRepository.findAll()) {
            List<Station> stations = stationRepository.findAllById(line.getStationIds());
            response.add(LineDetailResponse.of(line, stations));
        }
        return response;
    }

    public PathResponse findShortestPath(String sourceName, String targetName, PathType pathType) {
        if (sourceName.equals(targetName)) {
            throw new IllegalArgumentException("출발역과 도착역이 같습니다.");
        }

        ShortestPath shortestPath = ShortestPathFactory.createDijkstra(pathType, findAllEdges());
        Long sourceId = stationRepository.findIdByName(sourceName)
            .orElseThrow(() -> new NoSuchElementException("존재하지 않는 역입니다."));
        Long targetId = stationRepository.findIdByName(targetName)
            .orElseThrow(() -> new NoSuchElementException("존재하지 않는 역입니다."));

        List<Long> pathStationIds = shortestPath.getVertexList(sourceId, targetId);
        int weight = shortestPath.getWeight(sourceId, targetId);
        int subWeight = shortestPath.getSubWeight(sourceId, targetId);

        int distance = pathType.getDistance(weight, subWeight);
        int duration = pathType.getDuration(weight, subWeight);
        List<String> pathStationNames = stationRepository.findAllNameById(pathStationIds);
        return new PathResponse(distance, duration, pathStationNames);
    }

    private List<Edge> findAllEdges() {
        return lineRepository.findAll()
            .stream()
            .map(Line::getEdges)
            .map(Edges::getEdges)
            .flatMap(List::stream)
            .collect(Collectors.toList());
    }
}
