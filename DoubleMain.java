import java.util.Random;

public class DoubleMain {
   public static void main(String[] argArr) throws Exception {
      Arguments args = new Arguments(argArr);
      long seed = args.getLong("seed",1);
      int maxIters = args.getInt("maxIters",100);
      int rows = args.getInt("rows", 13);
      int cols = args.getInt("cols", 16);
      double similarity = args.getDbl("similarity",0.4);
      double similarityMax = args.getDbl("similarityMax",1);
      double empty = args.getDbl("empty", 0.2);
      double aThresh = args.getDbl("aThresh", 0.5);
      double bThresh = args.getDbl("bThresh", 0.5);
      boolean animate = args.getBool("animate", false);
      boolean color = args.getBool("color", true);
      long sleep = args.getLong("sleep", 0);
      boolean maximizer = args.getBool("maximizer", false);
      boolean printSimilarity = args.getBool("printSimilarity", true);

      System.err.printf("Proceeding with seed=%d, maxIters=%d, rows=%d, cols=%d, similarity=%.3f, similarityMax=%.3f, empty=%.3f, aThresh=%.3f, bThresh=%.3f, animate=%b, color=%b, sleep=%d, maximizer=%b, printSimilarity=%b\n", seed, maxIters, rows, cols, similarity, similarityMax, empty, aThresh, bThresh, animate, color, sleep, maximizer, printSimilarity);

      Colors.enabled = color;

      DoubleBoard board = new DoubleBoard(rows, cols, printSimilarity);
      Random rand = new Random(seed);
      for (int r = 0; r < rows; r++) {
         for (int c = 0; c < cols; c++) {
            double randDbl = rand.nextDouble();
            if (randDbl > empty) {
               boolean a = rand.nextDouble() > aThresh;
               boolean b = rand.nextDouble() > bThresh;
               DoubleAgent agent = new DoubleAgent(maximizer, similarity, similarityMax, a, b);

               board.addAgent(agent, new Point(r,c));
            }
         }
      }
      board.printState();
      System.out.println(board.getNumEmpty());
      if (animate) {
         System.out.println();
         board.printState();
      }
      int epochs;
      String moveUp = "\u001B["+rows+"A";
      for (epochs = 0; epochs < maxIters && board.performEpoch(); epochs++) {
         if (animate) {
            System.out.print(moveUp);
            board.printState();
            Thread.sleep(sleep);
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
