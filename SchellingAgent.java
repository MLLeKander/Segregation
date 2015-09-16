import java.util.*;

public class SchellingAgent extends Agent {
   private double similarityMin, similarityMax;
   public boolean color;

   public SchellingAgent(double similarityMin, double similarityMax, boolean color) {
      this.similarityMin = similarityMin;
      this.similarityMax = similarityMax;
      this.color = color;
   }


   public boolean isSatisfied(Board b) {
      return isSatisfiedAt(b, b.getPos(this));
   }

   public boolean isSatisfiedAt(Board board, Point p) {
      Point bound = board.getBoardSize();
      int neighborCnt = 0, similarCnt = 0;
      int row = p.r, col = p.c, brow = bound.r, bcol = bound.c;

      for (int drow = -1; drow <= 1; drow++) {
         for (int dcol = -1; dcol <= 1; dcol++) {
            //if (drow != 0 || dcol != 0) {
               SchellingAgent n = boundedGetAgent(board,row+drow,col+dcol);
               if (n != null) {
                  neighborCnt++;
                  if (isSimilarTo(n)) {
                     similarCnt++;
                  }
               }
            //}
         }
      }
      return similarCnt >= neighborCnt*similarityMin && similarCnt <= neighborCnt*similarityMax;
   }
   
   private boolean isBounded(Board b, int r, int c) {
      Point bound = b.getBoardSize();
      return r >= 0 && r < bound.r && c >= 0 && c < bound.c;
   }

   private SchellingAgent boundedGetAgent(Board b, int r, int c) {
      return isBounded(b,r,c) ? (SchellingAgent)b.getAgent(r,c) : null;
   }

   public boolean isSimilarTo(SchellingAgent a) {
      return a.color == color;
   }

   public void act(Board b) {
      Point to = findClosestSatisfiedSpot(b);
      b.move(this, to);
   }

   public Point findClosestSatisfiedSpot(Board b) {
      Point bound = b.getBoardSize(), start = b.getPos(this);
      Queue<Point> queue = new LinkedList<Point>();
      boolean[][] visited = new boolean[bound.r][bound.c];
      queue.offer(start);
      Point tmp = null;
      while (!queue.isEmpty()) {
         Point curr = queue.remove();
         int row = curr.r, col = curr.c;
         if (!isBounded(b,row,col) || visited[row][col]) {
            continue;
         }
         if (b.getAgent(curr) == null) {
            if (isSatisfiedAt(b, curr)) {
               return curr;
            } else {
               tmp = curr;
            }
         }
         visited[row][col] = true;

         for (int drow = -1; drow <= 1; drow++) {
            for (int dcol = -1; dcol <= 1; dcol++) {
               if (drow != 0 || dcol != 0) {
                  queue.offer(new Point(row+drow,col+dcol));
               }
            }
         }
      }
      //System.err.println("No satisfactory location found. Agent type: "+(color ? "#" : "O")+". Moving from ["+start.r+","+start.c+"] to ["+tmp.r+","+tmp.c+"].");
      return tmp;
   }
}
