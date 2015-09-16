import java.util.Random;

public class SchellingMain {
   public static void main(String[] argArr) {
      Arguments args = new Arguments(argArr);
      long seed = args.getLong("seed",1);
      int maxIters = args.getInt("maxIters",100);
      int rows = args.getInt("rows", 13);
      int cols = args.getInt("cols", 16);
      double similarity = args.getDbl("similarity",0.4);
      double similarityMax = args.getDbl("similarityMax",1);
      double wThresh = args.getDbl("wThresh", 0.4);
      double bThresh = args.getDbl("bThresh", 0.4);
      boolean animate = args.getBool("animate", false);

      System.err.printf("Proceeding with seed=%d, maxIters=%d, rows=%d, cols=%d, similarity=%.3f, similarityMax=%.3f, wThresh=%.3f, bThresh=%.3f, animate=%b\n", seed, maxIters, rows, cols, similarity, similarityMax, wThresh, bThresh, animate);

      SchellingBoard board = new SchellingBoard(rows, cols);
      Random rand = new Random(seed);
      for (int r = 0; r < rows; r++) {
         for (int c = 0; c < cols; c++) {
            double randDbl = rand.nextDouble();
            Agent a = null;
            if (randDbl < wThresh) {
               a = new SchellingAgent(similarity, similarityMax, false);
            } else if (randDbl > 1-bThresh) {
               a = new SchellingAgent(similarity, similarityMax, true);
            }

            if (a != null) {
               board.addAgent(a, new Point(r,c));
            }
         }
      }
      board.printState();
      int epochs;
      for (epochs = 0; epochs < maxIters && board.performEpoch(); epochs++) {
         if (animate) {
            System.out.println();
            board.printState();
         }
      }
      if (!animate) {
         System.out.println();
         board.printState();
      }
      System.out.println();
      System.out.printf("Completed in %d epochs.\n", epochs);
   }
}
