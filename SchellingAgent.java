public class SchellingAgent extends AbstractAgent<SchellingAgent> {
   public boolean color;
   //private final static MigrationStrategy<SchellingAgent> schellingStrategy = new MostSatisfied<SchellingAgent>();
   private final static MigrationStrategy<SchellingAgent> schellingStrategy = new CompositeStrategy<SchellingAgent>(
         new ClosestSatisfied<SchellingAgent>(),
         new MostSatisfied<SchellingAgent>()
      );

   public SchellingAgent(double similarityMin, double similarityMax, boolean color) {
      super(similarityMin, similarityMax, schellingStrategy);
      this.color = color;
   }

   public double neighborSimilarityAt(Board<SchellingAgent> board, Point p) {
      Point bound = board.getBoardSize();
      int neighborCnt = 0, similarCnt = 0;
      int row = p.r, col = p.c, brow = bound.r, bcol = bound.c;

      for (int drow = -1; drow <= 1; drow++) {
         for (int dcol = -1; dcol <= 1; dcol++) {
            if (drow != 0 || dcol != 0) {
               SchellingAgent that = boundedGetAgent(board,row+drow,col+dcol);
               if (that != null) {
                  neighborCnt++;
                  if (that.color == this.color) {
                     similarCnt++;
                  }
               }
            }
         }
      }
      return similarCnt/(double)neighborCnt;
   }
}
