public class DoubleAgent extends AbstractAgent<DoubleAgent> {
   public boolean colorA, colorB;
   private final static MigrationStrategy<DoubleAgent> maximizerStrategy = new MostSatisfied<DoubleAgent>();
   private final static MigrationStrategy<DoubleAgent> satisficerStrategy = new CompositeStrategy<DoubleAgent>(
         new ClosestSatisfied<DoubleAgent>(),
         new MostSatisfied<DoubleAgent>()
      );

   public DoubleAgent(boolean maximiserStrat, double similarityMin, double similarityMax, boolean colorA, boolean colorB) {
      super(similarityMin, similarityMax, maximiserStrat ? maximizerStrategy : satisficerStrategy);
      this.colorA = colorA;
      this.colorB = colorB;
   }

   public double neighborSimilarityAt(Board<DoubleAgent> board, Point p) {
      Point bound = board.getBoardSize();
      int neighborCnt = 0, similarCnt = 0;
      int row = p.r, col = p.c, brow = bound.r, bcol = bound.c;

      for (int drow = -1; drow <= 1; drow++) {
         for (int dcol = -1; dcol <= 1; dcol++) {
            if (drow != 0 || dcol != 0) {
               DoubleAgent that = boundedGetAgent(board,row+drow,col+dcol);
               if (that != null) {
                  neighborCnt+=2;
                  if (that.colorA == this.colorA) {
                     similarCnt++;
                  }
                  if (that.colorB == this.colorB) {
                     similarCnt++;
                  }
               }
            }
         }
      }
      if (neighborCnt == 0) return 0;
      return similarCnt/(double)neighborCnt;
   }

   public String toString() {
      return (colorA ? "{" : "<") + (colorB ? "}" : ">");
   }
}
