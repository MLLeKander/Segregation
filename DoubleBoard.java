
public class DoubleBoard extends Board<DoubleAgent> {
   public DoubleBoard(int r, int c) {
      super(r,c);
   }

   public void colorForScore(double score) {
      if (score < 0.25) {
         Colors.lightRed();
      } else if (score < 0.5) {
         Colors.red();
      } else if (score < 0.85) {
         Colors.green();
      } else {
         Colors.lightGreen();
      }
   }

   public void colorForAgentAt(DoubleAgent a, Point p) {
      colorForScore(a.neighborSimilarityAt(this,p));
   }

   public void printState() {
      Point boardSize = getBoardSize();
      for (int i = 0; i < boardSize.r; i++) {
         for (int j = 0; j < boardSize.c; j++) {
            DoubleAgent a = getAgent(i,j);
            if (a != null) {
               colorForAgentAt(a, new Point(i,j));
            }
            String s = "  ";
            if (a != null) {
               s = a.toString();
            }
            System.out.print(s);
            Colors.reset();
         }
         //System.out.print("   ");
         //for (int j = 0; j < boardSize.c; j++) {
         //   DoubleAgent a = getAgent(i,j);
         //   if (a == null) {
         //      System.out.print("    ");
         //   } else {
         //      double rawScore = a.neighborSimilarityAt(this,new Point(i,j));
         //      double score = 100*rawScore;
         //      colorForScore(rawScore);
         //      System.out.printf(" %3.0f",score);
         //   }
         //}
         Colors.reset();
         System.out.println();
      }
   }
}
