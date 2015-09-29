public class MultiAgent extends AbstractAgent<MultiAgent> {
   public boolean[] features;
   public int numFeatures;
   private final static MigrationStrategy<MultiAgent> maximizerStrategy = new MostSatisfied<MultiAgent>();
   private final static MigrationStrategy<MultiAgent> satisficerStrategy = new CompositeStrategy<MultiAgent>(
         new ClosestSatisfied<MultiAgent>(),
         new MostSatisfied<MultiAgent>()
      );

   public MultiAgent(boolean maximiserStrat, double similarityMin, double similarityMax, boolean[] features) {
      super(similarityMin, similarityMax, maximiserStrat ? maximizerStrategy : satisficerStrategy);
      this.features = features;
      this.numFeatures = features.length;
   }

   public double neighborSimilarityAt(Board<MultiAgent> board, Point p) {
      Point bound = board.getBoardSize();
      int neighborCnt = 0, similarCnt = 0;
      int row = p.r, col = p.c, brow = bound.r, bcol = bound.c;

      for (int drow = -1; drow <= 1; drow++) {
         for (int dcol = -1; dcol <= 1; dcol++) {
            if (drow != 0 || dcol != 0) {
               MultiAgent that = boundedGetAgent(board,row+drow,col+dcol);
               if (that != null) {
                  if (this.numFeatures != that.numFeatures) {
                     throw new IllegalStateException("???");
                  }
                  neighborCnt+=this.numFeatures;
                  for (int ndx = 0; ndx < this.numFeatures; ndx++) {
                     if (features[ndx] == that.features[ndx]) {
                        similarCnt++;
                     }
                  }
               }
            }
         }
      }
      if (neighborCnt == 0) return 0;
      return similarCnt/(double)neighborCnt;
   }

   public String toString() {
      StringBuilder sb = new StringBuilder();
      sb.append('{');
      for (boolean feature : features) {
         sb.append(feature ? 'O' : '*');
      }
      return sb.append('}').toString();
   }
}
