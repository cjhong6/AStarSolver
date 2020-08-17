package bearmaps.hw4;
import java.util.List;
import java.util.ArrayList;
import bearmaps.proj2ab.DoubleMapPQ;

public class AStarSolver<Vertex> implements ShortestPathsSolver<Vertex> {
    DoubleMapPQ<Vertex> fringe;
    double timeSpent;

    public AStarSolver(AStarGraph<Vertex> input, Vertex start, Vertex end, double timeout){
      fringe = new DoubleMapPQ<Vertex>();
      timeSpent = 0;

      fringe.add(start, h(start, end));
      Stopwatch sw = new Stopwatch();

      while(fringe.size() >= 1 || !(fringe.getSmallest().equals(end)) || timeSpent < timeout){
        Vertex smallestVertex = fringe.getSmallest();
        relax(smallestVertex);
        timeSpent = sw.elapsedTime();
      }
    }

    private double h(Vertex start, Vertex goal){
      return -1;
    }

    private void relax(Vertex start){
      return;
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
