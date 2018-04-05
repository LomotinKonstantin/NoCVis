import java.util.ArrayList;
import java.util.Arrays;

import static java.lang.Math.min;

/**
 *  Алгоритм поиска, основанный на обходе дерева в ширину.
 *  Посещенные вершины отбрасываются.
 *  **/
public class DemoAlgorithm implements Algorithm {

    protected int nodes_num;                        // число узлов
    protected int start, stop;                      // номера стартового и конечного узлов
    protected int[] components;                     // образующие
    protected int[] path;                           // найденный путь
    protected boolean finished;
    private ArrayList<ArrayList<Integer>> tree;     // список всех рассчитанных путей
    private int curr_branch;                        // текущий маршрут для визуализации

    /**
     * @param nodes_num число узлов в сети
     * @param components образующие циркулянта
     * @param start номер стартовой вершины
     * @param stop номер конечной вершины
     */
    DemoAlgorithm(int nodes_num, int[] components, int start, int stop) {
        this.nodes_num = nodes_num;
        this.components = components;
        this.start = min(nodes_num - 1, start);
        this.stop = min(nodes_num - 1, stop);
        finished = false;

        tree = new ArrayList<>();

        buildTree();

        curr_branch = 0;
    }


    /**
     * Обертка для преобразования вектора Integer в массив int.
     * Дженерики не могут иметь примитивы в качестве шаблонов, а
     * автоматического приведения Integer[] к int[] в Java нет.
     * @param arrl вектор с объектами типа Integer
     * @return массив int
     */
    /* Удобное преобразование вектора Integer в массив int */
    public static int[] arrlToArr(ArrayList<Integer> arrl) {
        Integer[] temp = arrl.toArray(new Integer[0]);
        return Arrays.stream(temp).mapToInt(Integer::intValue).toArray();
    }

    @Override
    public int[] path() {
        return path.clone();
    }

    @Override
    public int startNum() {
        return start;
    }

    @Override
    public int finishNum() {
        return stop;
    }

    @Override
    public int[] nextStep() {
        if (!finished) {
            if (curr_branch < tree.size() - 1)
                return arrlToArr(tree.get(curr_branch++));
            else {
                finished = true;
                return path;
            }
        } else
            return path;
    }

    @Override
    public boolean isFinished() {
        return finished;
    }

    /**
     * Поиск пути в ширину. Все промежуточные шаги сохраняюся в tree.
     */
    private void buildTree() {
        ArrayList<Integer> visited = new ArrayList<>();
        visited.add(start);
        {
            ArrayList<Integer> branch = new ArrayList<>();
            branch.add(start);
            tree.add(branch);
        }
        int i = 0;
        while (i < tree.size()){
            ArrayList<Integer> branch = tree.get(i);
            ++i;
            int last = branch.get(branch.size() - 1);
            for (int c : components) {
                int next_a = last + c;
                next_a -= (next_a >= nodes_num) ? nodes_num : 0;
                if (!visited.contains(next_a)) {
                    ArrayList<Integer> new_branch = new ArrayList<>(branch);
                    new_branch.add(next_a);
                    tree.add(new_branch);
                    if (next_a == stop) {
                        path = arrlToArr(new_branch);
                        return;
                    }
                }
                int next_b = last - c;
                next_b += (next_b < 0) ? nodes_num : 0;
                if (!visited.contains(next_b)) {
                    ArrayList<Integer> new_branch = new ArrayList<>(branch);
                    new_branch.add(next_b);
                    tree.add(new_branch);
                    if (next_b == stop) {
                        path = arrlToArr(new_branch);
                        return;
                    }
                }
            }
        }
    }
}
