public class SchellingBoard extends Board {
   public SchellingBoard(int r, int c) {
      super(r,c);
   }

   public void printState() {
      for (Agent[] row : cells) {
         for (Agent a : row) {
            SchellingAgent sa = (SchellingAgent)a;
            char c = sa == null ? '.' : sa.color ? '#' : 'O';
            System.out.print(c);
         }
         System.out.println();
      }
   }
}
