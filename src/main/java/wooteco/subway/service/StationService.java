package wooteco.subway.service;

import java.util.List;
import org.springframework.stereotype.Service;
import wooteco.subway.domain.Station;
import wooteco.subway.repository.StationRepository;

@Service
public class StationService {

    private final StationRepository stationRepository;

    public StationService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }


    public Station save(Station station) {
        return stationRepository.save(station);
    }

    public List<Station> showStations() {
        return stationRepository.findAll();
    }

    public void deleteStationById(Long id) {
        stationRepository.deleteById(id);
    }
}
