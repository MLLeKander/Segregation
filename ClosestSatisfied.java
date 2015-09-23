import java.util.*;

public class ClosestSatisfied<AgentType extends AbstractAgent<AgentType>> implements MigrationStrategy<AgentType> {
   public Point findPoint(Board<AgentType> board, AgentType agent) {
      Point bound = board.getBoardSize(), start = board.getPos(agent);
      Queue<Point> queue = new LinkedList<Point>();
      boolean[][] visited = new boolean[bound.r][bound.c];
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
