import java.util.*;

public class Main {
   @SuppressWarnings("unchecked")
   public static void main(String[] argArr) throws InterruptedException {
      Arguments args = new Arguments(argArr);
      Board<? extends AbstractAgent> board = BoardFactory.constructBoard(args);
      performRun(args, board);
   }

   public static <AType extends AbstractAgent<AType>> void performRun(Arguments args, Board<AType> board) throws InterruptedException {
      int maxIters = args.getInt("maxIters",100);
      boolean animate = args.getBool("animate", false);
      boolean color = args.getBool("color", true);
      long sleep = args.getLong("sleep", 0);
      boolean printAgents = args.getBool("printAgents", true);
      boolean printSimilarity = args.getBool("printSimilarity", true);
      boolean metrics = args.getBool("metrics", true);

      System.out.printf("Proceeding with maxIters=%d, animate=%b, sleep=%d, color=%b, printAgents=%b, printSimilarity=%b, metrics=%b...\n", maxIters, animate, sleep, color, printAgents, printSimilarity, metrics);
      //TODO: Rename to ANSI
      Colors.enabled = color;

      List<? extends Metric<AType>> metricList = Arrays.asList(new SimilarityMetric<AType>(), new ClusteringMetric<AType>());

      String emptyAgent = emptyAgent(board);
      System.out.println("Initial board state:");
      printState(board, printAgents, printSimilarity, emptyAgent);
      System.out.println();
      if (animate) {
         System.out.println("Epoch 0:");
         printState(board, printAgents, printSimilarity, emptyAgent);
      }
      if (metrics) {
         for (Metric<AType> m : metricList) {
            m.observe(board);
         }
      }
      int epochs;
      //TODO: Move this to ANSI
      String moveUp = "\u001B["+(board.getBoardSize().r+1)+"A";
      for (epochs = 0; epochs < maxIters && board.performEpoch(); epochs++) {
         if (animate) {
            System.out.print(moveUp);
            System.out.printf("Epoch %d:\n",epochs+1);
            printState(board, printAgents, printSimilarity, emptyAgent);
            if (sleep > 0) {
               Thread.sleep(sleep);
            }
         }
         if (metrics) {
            for (Metric<AType> m : metricList) {
               m.observe(board);
            }
         }
      }
      if (animate) {
         System.out.print(moveUp);
      }
      System.out.println("Final board state:");
      printState(board, printAgents, printSimilarity, emptyAgent);
      System.out.println();
      System.out.printf("Completed in %d epochs.\n", epochs);

      if (metrics) {
         System.out.println("\nMetrics");
         for (Metric<AType> m : metricList) {
            System.out.println(m.repr());
         }
      }
   }

   public static void colorForScore(double score) {
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
                  Colors.reset();
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
                  Colors.reset();
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
