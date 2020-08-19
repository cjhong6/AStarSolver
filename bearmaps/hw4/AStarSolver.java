package bearmaps.hw4;
import java.util.List;
import java.util.ArrayList;
import bearmaps.proj2ab.DoubleMapPQ;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.LinkedList;

public class AStarSolver<Vertex> implements ShortestPathsSolver<Vertex> {
    private DoubleMapPQ<Vertex> fringe;
    private double timeSpent;
    private AStarGraph<Vertex> graph;
    private HashMap<Vertex, Double> distTo;
    private HashMap<Vertex, Vertex> edgeTo;
    private Set<Vertex> visited;
    private SolverOutcome outcome;
    private int numStatesExplored;
    private Vertex goal;

    public AStarSolver(AStarGraph<Vertex> input, Vertex start, Vertex end, double timeout){
      this.goal = end;
      this.fringe = new DoubleMapPQ<Vertex>();
      this.timeSpent = 0;
      this.graph = input;
      this.distTo = new HashMap<>();
      this.edgeTo = new HashMap<>();
      this.visited = new HashSet<>();

      this.fringe.add(start, h(start, end));
      this.distTo.put(start, 0.0);
      this.edgeTo.put(start, null);
      this.visited.add(start);
      Stopwatch sw = new Stopwatch();
      while(fringe.size() >= 1 && !(fringe.getSmallest().equals(end)) && timeSpent < timeout){
        Vertex smallestVertex = fringe.removeSmallest();
        numStatesExplored++;
        relax(smallestVertex, end);
        timeSpent = sw.elapsedTime();
      }

      if(timeSpent > timeout){
        outcome = SolverOutcome.TIMEOUT;
      }else if(fringe.getSmallest().equals(end)){
        outcome = SolverOutcome.SOLVED;
      }else{
        outcome = SolverOutcome.UNSOLVABLE;
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
      return this.outcome;
    }

    public List<Vertex> solution(){
      LinkedList<Vertex> solution = new LinkedList<>();
      if(outcome==SolverOutcome.TIMEOUT || outcome==SolverOutcome.UNSOLVABLE){
        return new ArrayList<>();
      }else{
        solution.addFirst(this.goal);
        Vertex from = edgeTo.get(this.goal);
        while(from != null){
          solution.addFirst(from);
          from = edgeTo.get(from);
        }
        return new ArrayList<>(solution);
      }
    }

    public double solutionWeight(){
      if(outcome==SolverOutcome.TIMEOUT || outcome==SolverOutcome.UNSOLVABLE){
        return 0;
      }else{
        return distTo.get(this.goal);
      }
    }

    public int numStatesExplored(){
      return this.numStatesExplored;
    }
    public double explorationTime(){
      return timeSpent;
    }
}
