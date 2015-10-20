import java.util.*;

public class SingleFeatureClusteringMetric<AType extends Agent<AType>> extends AbstractMetric<AType, Integer> {
   boolean[][] visited = new boolean[1][1];
   boolean useMooreNeighborhood;

   public SingleFeatureClusteringMetric() {
      this(true);
   }

   public SingleFeatureClusteringMetric(boolean useMooreNeighborhood) {
      super("SingleFeature"+(useMooreNeighborhood ? "Moore" : "Neumann")+"Clustering");
      this.useMooreNeighborhood = useMooreNeighborhood;
   }

   @Override
   public void observe(Board<AType> board) {
      Point bound = board.getBoardSize();
      initVisited(bound);
      log.clear();
      int clusterCount = 0;

      for (int r = 0; r < bound.r; r++) {
         for (int c = 0; c < bound.c; c++) {
            AType a = board.getAgent(r, c);
            if (a != null && !visited[r][c]) {
               floodFill(board, bound, new Point(r,c), a);
               clusterCount++;
            }
         }
      }
      log.add(clusterCount);
   }

   public void initVisited(Point bound) {
      if (visited.length < bound.r || visited[0].length < bound.c) {
         visited = new boolean[bound.r][bound.c];
      }
      for (boolean[] row : visited) {
         Arrays.fill(row, false);
      }
   }

   public int floodFill(Board<AType> board, Point bound, Point start, AType a) {
      ArrayDeque<Point> stack = new ArrayDeque<Point>();
      stack.add(start);

      int count = 0;
      while (!stack.isEmpty()) {
         Point curr = stack.pop();
         int row = curr.r, col = curr.c;
         if (!board.isBounded(row,col) || visited[row][col]) {
            continue;
         }

         AType tmpAgent = board.getAgent(curr);
         if (tmpAgent == null || Math.abs(tmpAgent.compareFeature(a, 0) - 1) > 1e-10) {
            continue;
         }

         count++;
         visited[row][col] = true;

         if (useMooreNeighborhood) {
            for (int drow = -1; drow <= 1; drow++) {
               for (int dcol = -1; dcol <= 1; dcol++) {
                  if (drow != 0 || dcol != 0) {
                     stack.add(new Point(row+drow,col+dcol));
                  }
               }
            }
         } else {
            for (int d = -1; d <= 1; d+=2) {
               stack.add(new Point(row+d,col));
               stack.add(new Point(row,col+d));
            }
         }
      }
      return count;
   }
}
