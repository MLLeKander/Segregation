import java.util.*;

public class ClosestSatisfied<AgentType extends AbstractAgent<AgentType>> implements MigrationStrategy<AgentType> {
   boolean[][] visited = new boolean[1][1];

   public void initVisited(Point bound) {
      if (visited.length < bound.r || visited[0].length < bound.c) {
         visited = new boolean[bound.r][bound.c];
      }
      for (boolean[] row : visited) {
         Arrays.fill(row, false);
      }
   }

   @Override
   public Point findPoint(Board<AgentType> board, AgentType agent, Point start) {
      Point bound = board.getBoardSize();
      initVisited(bound);
      Queue<Point> queue = new LinkedList<Point>();
      queue.offer(start);
      Point tmp = null;
      while (!queue.isEmpty()) {
         Point curr = queue.remove();
         int row = curr.r, col = curr.c;
         if (!agent.isBounded(board,row,col) || visited[row][col]) {
            continue;
         }
         if (board.getAgent(curr) == null && agent.isSatisfiedAt(board, curr)) {
            return curr;
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
      return null;
   }
}
