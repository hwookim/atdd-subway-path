package wooteco.subway.domain.path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import wooteco.subway.domain.Edge;

public class PathTypeTest {

    @DisplayName("생성 성공")
    @ParameterizedTest
    @MethodSource("generateTypeArguments")
    void of(String input, PathType pathType) {
        assertThat(PathType.of(input)).isEqualTo(pathType);
    }

    static Stream<Arguments> generateTypeArguments() {
        return Stream.of(
            Arguments.of("DISTANCE", PathType.DISTANCE),
            Arguments.of("distance", PathType.DISTANCE),
            Arguments.of("Distance", PathType.DISTANCE),
            Arguments.of("DURATION", PathType.DURATION),
            Arguments.of("duration", PathType.DURATION),
            Arguments.of("Duration", PathType.DURATION));
    }

    @DisplayName("생성 실패")
    @ParameterizedTest
    @ValueSource(strings = {"", " ", "time"})
    void of_Fail(String input) {
        assertThatThrownBy(() -> PathType.of(input))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("최단 시간 / 거리 검색")
    @ParameterizedTest
    @MethodSource("generateTypeWeightArguments")
    void findWeight(PathType pathType, int expected) {
        Edge edge = new Edge(1L, 2L, 10, 5);
        int actual = pathType.findWeight(edge);
        assertThat(actual).isEqualTo(expected);
    }

    static Stream<Arguments> generateTypeWeightArguments() {
        return Stream.of(
            Arguments.of(PathType.DISTANCE, 10),
            Arguments.of(PathType.DURATION, 5));
    }

    @DisplayName("최단 시간 검색 시 거리 / 최단 거리 검색 시 소요 시간")
    @ParameterizedTest
    @MethodSource("generateTypeSubWeightArguments")
    void findSubWeight(PathType pathType, int expected) {
        Edge edge = new Edge(1L, 2L, 10, 5);
        int actual = pathType.findSubWeight(edge);
        assertThat(actual).isEqualTo(expected);
    }

    static Stream<Arguments> generateTypeSubWeightArguments() {
        return Stream.of(
            Arguments.of(PathType.DISTANCE, 5),
            Arguments.of(PathType.DURATION, 10));
    }
}
