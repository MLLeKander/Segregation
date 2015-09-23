
public class SchellingBoard extends Board<SchellingAgent> {
   public SchellingBoard(int r, int c) {
      super(r,c);
   }

   public void colorForScore(double score) {
      if (score < -0.5) {
         Colors.lightRed();
      } else if (score < 0) {
         Colors.red();
      } else if (score < 0.5) {
         Colors.green();
      } else {
         Colors.lightGreen();
      }
   }

   public void colorForAgentAt(SchellingAgent a, Point p) {
      colorForScore(a.satisfactionScoreAt(this,p));
   }

   public void printState() {
      Point boardSize = getBoardSize();
      for (int i = 0; i < boardSize.r; i++) {
         for (int j = 0; j < boardSize.r; j++) {
            SchellingAgent a = getAgent(i,j);
            char c = a == null ? '.' : a.color ? '#' : 'O';
            if (a != null) {
               colorForAgentAt(a, new Point(i,j));
            }
            System.out.print(c);
            Colors.reset();
         }
         System.out.print("   ");
         for (int j = 0; j < boardSize.r; j++) {
            SchellingAgent a = getAgent(i,j);
            if (a == null) {
               System.out.print("    ");
            } else {
               char c = a.color ? '#' : 'O';
               double rawScore = a.satisfactionScoreAt(this,new Point(i,j));
               double score = 10*rawScore;
               colorForScore(rawScore);
               System.out.printf(" %+-3.0f",score);
            }
         }
         Colors.reset();
         System.out.println();
      }
   }
}
