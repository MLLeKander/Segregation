import java.util.*;

public class Main {
   @SuppressWarnings("unchecked")
   public static void main(String[] argArr) throws InterruptedException {
      Arguments args = new Arguments(argArr);
      if (args.getBool("batch", false)) {
         args.put("headless", "true");
         for (int i = args.getInt("batchMin",0); i < args.getInt("batchMax"); i++) {
            args.put("seed", i+"");
            Board<? extends AbstractAgent> board = BoardFactory.constructBoard(args);
            performRun(args, board);
         }
      } else {
         Board<? extends AbstractAgent> board = BoardFactory.constructBoard(args);
         performRun(args, board);
      }
   }

   public static <AType extends AbstractAgent<AType>> void performRun(Arguments args, Board<AType> board) throws InterruptedException {
      int maxIters = args.getInt("maxIters",100);
      boolean color = args.getBool("color", true) & args.getBool("ansi",true);
      long sleep = args.getLong("sleep", 0);
      boolean printAgents = args.getBool("printAgents", true);
      boolean printSimilarity = args.getBool("printSimilarity", true);
      boolean metrics = args.getBool("metrics", true);
      boolean headless = args.getBool("headless", false);
      boolean animate = args.getBool("animate", false);

      System.out.printf("Proceeding with maxIters=%d, animate=%b, sleep=%d, color=%b, printAgents=%b, printSimilarity=%b, metrics=%b, headless=%b...\n", maxIters, animate, sleep, color, printAgents, printSimilarity, metrics, headless);
      ANSI.enabled = color;

      List<? extends Metric<AType>> beforeMetrics = Arrays.asList(new SimilarityMetric<AType>(), new ClusteringMetric<AType>());
      List<? extends Metric<AType>> afterMetrics = Arrays.asList(new SimilarityMetric<AType>(), new ClusteringMetric<AType>());

      String emptyAgent = emptyAgent(board);
      if (!headless) {
         System.out.println("Initial board state:");
         printState(board, printAgents, printSimilarity, emptyAgent);
         System.out.println();
         if (animate) {
            System.out.println("Epoch 0:");
            printState(board, printAgents, printSimilarity, emptyAgent);
            System.out.println();
         }
      }
      if (metrics) {
         for (Metric<AType> m : beforeMetrics) {
            m.observe(board);
         }
      }
      int epochs;
      int lines = board.getBoardSize().r+2;
      for (epochs = 0; epochs < maxIters && board.performEpoch(); epochs++) {
         if (!headless && animate) {
            ANSI.moveUp(lines);
            System.out.printf("Epoch %d:\n",epochs+1);
            printState(board, printAgents, printSimilarity, emptyAgent);
            System.out.println();
            if (sleep > 0) {
               Thread.sleep(sleep);
            }
         }
      }
      if (!headless) {
         if (animate) {
            ANSI.moveUp(lines);
         }
         System.out.println("Final board state:");
         printState(board, printAgents, printSimilarity, emptyAgent);
         System.out.println();
      }
      System.out.printf("Completed in %d epochs.\n", epochs);

      if (metrics) {
         for (Metric<AType> m : afterMetrics) {
            m.observe(board);
         }
         System.out.println("\nMetrics");
         for (int i = 0; i < beforeMetrics.size(); i++) {
            System.out.println("before"+beforeMetrics.get(i).repr());
            System.out.println("after"+afterMetrics.get(i).repr());
         }
      }
   }

   public static void colorForScore(double score) {
      if (score < 0.25) {
         ANSI.lightRed();
      } else if (score < 0.5) {
         ANSI.red();
      } else if (score < 0.85) {
         ANSI.green();
      } else {
         ANSI.lightGreen();
      }
   }

   public static <AType extends AbstractAgent<AType>> void colorForAgentAt(AType a, Board<AType> b, Point p) {
      colorForScore(a.neighborSimilarityAt(b,p));
   }

   public static <AType extends AbstractAgent<AType>> void printState(Board<AType> b, boolean printAgents, boolean printSimilarity, String emptyAgent) {
      Point boardSize = b.getBoardSize();
      for (int i = 0; i < boardSize.r; i++) {
         if (printAgents) {
            for (int j = 0; j < boardSize.c; j++) {
               AType a = b.getAgent(i,j);
               if (a != null) {
                  colorForAgentAt(a, b, new Point(i,j));
                  System.out.print(a.toString());
                  ANSI.reset();
               } else {
                  System.out.print(emptyAgent);
               }
            }
         }

         if (printAgents && printSimilarity) {
            System.out.print("   ");
         }

         if (printSimilarity) {
            for (int j = 0; j < boardSize.c; j++) {
               AType a = b.getAgent(i,j);
               if (a == null) {
                  System.out.print("    ");
               } else {
                  double rawScore = a.neighborSimilarityAt(b,new Point(i,j));
                  double score = 100*rawScore;
                  colorForScore(rawScore);
                  System.out.printf(" %3.0f",score);
                  ANSI.reset();
               }
            }
         }
         System.out.println();
      }
   }

   public static String emptyAgent(Board<?> b) {
      Point bounds = b.getBoardSize();
      for (int r = 0; r < bounds.r; r++) {
         for (int c = 0; c < bounds.c; c++) {
            Object a = b.getAgent(r,c);
            if (a != null) {
               int len = a.toString().length();
               return new String(new char[len]).replaceAll("\0", " ");
            }
         }
      }
      return " ";
   }
}
