import java.util.*;

public class SimilarityMetric<AType extends AbstractAgent<AType>> extends AbstractMetric<AType, Double> {
   public SimilarityMetric() {
      super("Similarity");
   }

   @Override
   public void observe(Board<AType> board) {
      Point bound = board.getBoardSize();
      log.clear();

      for (int r = 0; r < bound.r; r++) {
         for (int c = 0; c < bound.c; c++) {
            AType a = board.getAgent(r, c);
            if (a != null) {
               log.add(a.neighborSimilarityAt(board,new Point(r,c)));
            }
         }
      }
   }

   public String format(Object o) {
      return String.format("%.3f", o);
   }
}
