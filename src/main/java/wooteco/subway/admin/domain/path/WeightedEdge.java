package wooteco.subway.admin.domain.path;

import org.jgrapht.graph.DefaultWeightedEdge;

public class WeightedEdge extends DefaultWeightedEdge {

    private int subWeight = 0;

    public void setSubWeight(int subWeight) {
        this.subWeight = subWeight;
    }

    public int getSubWeight() {
        return subWeight;
    }
}
