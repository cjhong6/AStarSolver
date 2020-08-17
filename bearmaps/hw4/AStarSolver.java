package bearmaps.hw4;
import java.util.List;
import java.util.ArrayList;
import bearmaps.proj2ab.DoubleMapPQ;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;

public class AStarSolver<Vertex> implements ShortestPathsSolver<Vertex> {
    DoubleMapPQ<Vertex> fringe;
    double timeSpent;
    AStarGraph<Vertex> graph;
    HashMap<Vertex, Double> distTo;
    HashMap<Vertex, Vertex> edgeTo;
    Set<Vertex> visited;

    public AStarSolver(AStarGraph<Vertex> input, Vertex start, Vertex end, double timeout){
      fringe = new DoubleMapPQ<Vertex>();
      timeSpent = 0;
      graph = input;
      distTo = new HashMap<>();
      edgeTo = new HashMap<>();
      visited = new HashSet<>();

      fringe.add(start, h(start, end));
      Stopwatch sw = new Stopwatch();
      distTo.put(start, 0.0);
      edgeTo.put(start, null);
      visited.add(start);

      while(fringe.size() >= 1 && !(fringe.getSmallest().equals(end)) && timeSpent < timeout){
        Vertex smallestVertex = fringe.removeSmallest();
        relax(smallestVertex, end);
        timeSpent = sw.elapsedTime();
        System.out.println(smallestVertex);
      }
    }

    //Very crude heuristic that just return the min weight edge out of each vertex
    //Change to BFS in postmortem
    private double h(Vertex start, Vertex goal){
      return graph.estimatedDistanceToGoal(start, goal);
    }

    //relax all edges outgoing from currentVertex
    private void relax(Vertex currentVertex, Vertex goal){
      List<WeightedEdge<Vertex>> neighborEdges = graph.neighbors(currentVertex);

      for(WeightedEdge<Vertex> neighborEdge : neighborEdges){
        Vertex from = neighborEdge.from();
        Vertex to = neighborEdge.to();
        double weight = neighborEdge.weight();

        if(!visited.contains(to)){
          visited.add(to);
          distTo.put(to, distTo.get(from)+weight);
          edgeTo.put(to, from);
          fringe.add(to, h(to, goal)+distTo.get(to));

        }else if(visited.contains(to) && fringe.contains(to)){
          if(distTo.get(from)+weight < distTo.get(to)){
            distTo.replace(to, distTo.get(from)+weight);
            edgeTo.replace(to, from);
            fringe.changePriority(to, h(to, goal)+distTo.get(to));
          }
        }
      }
    }

    public SolverOutcome outcome(){
      return null;
    }
    public List<Vertex> solution(){
      return null;
    }
    public double solutionWeight(){
      return -1;
    }
    public int numStatesExplored(){
      return -1;
    }
    public double explorationTime(){
      return -1;
    }
}
