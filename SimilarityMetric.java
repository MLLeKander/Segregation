import java.util.*;

public class SimilarityMetric<AType extends AbstractAgent<AType>> extends AbstractMetric<AType> {
   public SimilarityMetric() {
      super("Similarity");
   }

   @Override
   public void observe(Board<AType> board) {
      Point bound = board.getBoardSize();

      ArrayList<Double> thisLog = new ArrayList<>();
      for (int r = 0; r < bound.r; r++) {
         for (int c = 0; c < bound.c; c++) {
            AType a = board.getAgent(r, c);
            if (a != null) {
               thisLog.add(a.neighborSimilarityAt(board,new Point(r,c)));
            }
         }
      }
      Collections.sort(thisLog);
      log.add(thisLog);
   }

   public String format(Object o) {
      return String.format("%.3f", o);
   }
}
