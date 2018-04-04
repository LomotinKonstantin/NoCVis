import java.util.ArrayList;
import java.util.Arrays;

import static java.lang.Math.min;

public class DemoAlgorithm implements Algorithm {
/**
 *  Алгоритм поиска, основанный на обходе дерева в ширину.
 *  Посещенные вершины отбрасываются.
 *  **/
    protected int nodes_num;
    protected int start, stop;
    protected int[] components;
    protected int[] path;
    protected boolean finished;
    ArrayList<Integer> visited;

    DemoAlgorithm(int nodes_num, int[] components, int start, int stop) {
        this.nodes_num = nodes_num;
        this.components = components;
        this.start = min(nodes_num - 1, start);
        this.stop = min(nodes_num - 1, stop);
        finished = false;

        visited = new ArrayList<>();
        visited.add(start);
    }

    @Override
    public int[] path() {
        return path.clone();
    }

    @Override
    public int[] nextStep() {
        if (!finished) {
            int next = visited.get(visited.size() - 1) + components[0];
            visited.add(next);
            if (next == stop) {
                finished = true;
            }
            Integer[] temp = visited.toArray(new Integer[0]);
            path = Arrays.stream(temp).mapToInt(Integer::intValue).toArray();
        }
        return path;
    }

    @Override
    public boolean isFinished() {
        return finished;
    }
}
