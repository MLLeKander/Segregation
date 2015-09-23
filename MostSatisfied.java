public class MostSatisfied<AgentType extends AbstractAgent<AgentType>> implements MigrationStrategy<AgentType> {
   public Point findPoint(Board<AgentType> board, AgentType agent) {
      Point bound = board.getBoardSize();
      boolean[][] visited = new boolean[bound.r][bound.c];
      Point maxPoint = null;
      double maxSat = Double.MIN_VALUE;
      for (int r = 0; r < bound.r; r++) {
         for (int c = 0; c < bound.c; c++) {
            if (board.getAgent(r,c) == null) {
               Point tmpPoint = new Point(r,c);
               double tmpSat = agent.satisfactionScoreAt(board, tmpPoint);
               if (tmpSat > maxSat) {
                  maxSat = tmpSat;
                  maxPoint = tmpPoint;
               }
            }
         }
      }
      return maxPoint;
   }
}
