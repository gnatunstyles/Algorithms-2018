package lesson5;

import kotlin.NotImplementedError;
import lesson5.impl.GraphBuilder;

import java.util.*;

@SuppressWarnings("unused")
public class JavaGraphTasks {
    /**
     * Эйлеров цикл.
     * Средняя
     * <p>
     * Дан граф (получатель). Найти по нему любой Эйлеров цикл.
     * Если в графе нет Эйлеровых циклов, вернуть пустой список.
     * Соседние дуги в списке-результате должны быть инцидентны друг другу,
     * а первая дуга в списке инцидентна последней.
     * Длина списка, если он не пуст, должна быть равна количеству дуг в графе.
     * Веса дуг никак не учитываются.
     * <p>
     * Пример:
     * <p>
     * G -- H
     * |    |
     * A -- B -- C -- D
     * |    |    |    |
     * E    F -- I    |
     * |              |
     * J ------------ K
     * <p>
     * Вариант ответа: A, E, J, K, D, C, H, G, B, C, I, F, B, A
     * <p>
     * Справка: Эйлеров цикл -- это цикл, проходящий через все рёбра
     * связного графа ровно по одному разу
     */
    public static List<Graph.Edge> findEulerLoop(Graph graph) {
        throw new NotImplementedError();
    }

    /**
     * Минимальное остовное дерево.
     * Средняя
     * <p>
     * Дан граф (получатель). Найти по нему минимальное остовное дерево.
     * Если есть несколько минимальных остовных деревьев с одинаковым числом дуг,
     * вернуть любое из них. Веса дуг не учитывать.
     * <p>
     * Пример:
     * <p>
     * G -- H
     * |    |
     * A -- B -- C -- D
     * |    |    |    |
     * E    F -- I    |
     * |              |
     * J ------------ K
     * <p>
     * Ответ:
     * <p>
     * G    H
     * |    |
     * A -- B -- C -- D
     * |    |    |
     * E    F    I
     * |
     * J ------------ K
     * <p>
     * <p>
     * <p>
     * Трудоемкость = O(N), где N - количество вершин графа
     * Ресурсоемкость = O(N)
     */
    public static Graph minimumSpanningTree(Graph graph) {
        LinkedList<Graph.Vertex> list = new LinkedList<>();
        List<Graph.Vertex> verticesList = new ArrayList<>(graph.getVertices());
        GraphBuilder gb = new GraphBuilder();
        list.add((Graph.Vertex) verticesList.toArray()[0]);
        while (!list.isEmpty()) {
            graph.getConnections(list.remove()).forEach((key, value) -> {
                if (gb.getVertex(key) == null) {
                    gb.addVertex(key.getName());
                    list.addLast(key);
                    gb.addConnection(value.getBegin(), value.getEnd(), 1);
                }
            });
        }
        return gb.build();
    }

    /**
     * Максимальное независимое множество вершин в графе без циклов.
     * Сложная
     * <p>
     * Дан граф без циклов (получатель), например
     * <p>
     * G -- H -- J
     * |
     * A -- B -- D
     * |         |
     * C -- F    I
     * |
     * E
     * <p>
     * Найти в нём самое большое независимое множество вершин и вернуть его.
     * Никакая пара вершин в независимом множестве не должна быть связана ребром.
     * <p>
     * Если самых больших множеств несколько, приоритет имеет то из них,
     * в котором вершины расположены раньше во множестве this.vertices (начиная с первых).
     * <p>
     * В данном случае ответ (A, E, F, D, G, J)
     * <p>
     * Эта задача может быть зачтена за пятый и шестой урок одновременно
     * Трудоемкость = О(N)
     * // Ресурсоемкость = О(N)
     */
    public static Set<Graph.Vertex> largestIndependentVertexSet(Graph graph) {
        Set<Graph.Vertex> child = new HashSet<>();
        Set<Graph.Vertex> grandChild = new HashSet<>();
        Set<Graph.Edge> connections = graph.getEdges();
        for (Graph.Edge edge : connections) {
            Graph.Vertex begin = edge.getBegin();
            Graph.Vertex end = edge.getEnd();
            if (child.isEmpty() ||
                    !grandChild.contains(begin)) {
                child.add(begin);
                grandChild.add(end);
            }
            if (grandChild.contains(begin) &&
                    !child.contains(end)) {
                child.add(end);
            }
            if (child.contains(begin) &&
                    !grandChild.contains(end)) {
                grandChild.add(end);
            }
        }
        return grandChild.size() > child.size() ? grandChild : child;
    }

    /**
     * Наидлиннейший простой путь.
     * Сложная
     * <p>
     * Дан граф (получатель). Найти в нём простой путь, включающий максимальное количество рёбер.
     * Простым считается путь, вершины в котором не повторяются.
     * Если таких путей несколько, вернуть любой из них.
     * <p>
     * Пример:
     * <p>
     * G -- H
     * |    |
     * A -- B -- C -- D
     * |    |    |    |
     * E    F -- I    |
     * |              |
     * J ------------ K
     * <p>
     * Ответ: A, E, J, K, D, C, H, G, B, F, I
     * Трудоемкость = O(N!), где N - количество вершин графа
     * Ресурсоемкость = O(N!)
     */
    public static Path longestSimplePath(Graph graph) {
        if (graph == null) return null;
        List<Graph.Vertex> verticesList = new ArrayList<>(graph.getVertices());
        LinkedList<Path> list = new LinkedList<>();
        Path longestPath = null;
        int length = 0;
        for (Graph.Vertex vertex : verticesList) {
            list.add(new Path(vertex));
        }
        if (list.isEmpty()) return null;
        while (!list.isEmpty()) {
            Path path = list.remove();
            if (path.getLength() > length) {
                longestPath = path;
                length = path.getLength();
                if (path.getVertices().size() == verticesList.size()) break;
            }
            List<Graph.Vertex> vertices = path.getVertices();
            for (Graph.Vertex neighbour : graph.getNeighbors(vertices.get(vertices.size() - 1))) {
                if (!path.contains(neighbour)) {
                    list.add((new Path(path, graph, neighbour)));
                }
            }
        }
        return longestPath;
    }

}
