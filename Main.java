import java.util.*;
import java.io.*;

public class Main {
   public static <AType extends AbstractAgent<AType>> void initOut(PrintStream out, List<? extends Metric<AType>> metrics) {
      out.print("numFeatures\tseed\tsimilarity\temptiness\trows\tcols\tstrat\tthreshes\tnumEpochs");
      for (Metric<AType> m : metrics) {
         out.print("\tbefore"+m.name());
      }
      for (Metric<AType> m : metrics) {
         out.print("\tafter"+m.name());
      }
      out.println();
   }

   @SuppressWarnings("unchecked")
   public static void main(String[] argArr) throws InterruptedException, FileNotFoundException {
      Arguments args = new Arguments(argArr);
      List<Metric<?>> metrics = Arrays.<Metric<?>>asList(new SimilarityMetric(), new ClusteringMetric(true), new ClusteringMetric(false));
      List metrics_ = metrics;

      if (args.getBool("batch", false)) {
         PrintStream out = new PrintStream(new File(args.get("outFile")));
         initOut(out, metrics_);
         args.put("headless", "true");
         args.put("metrics", "true");
         for (int i = args.getInt("batchMin",0); i < args.getInt("batchMax"); i++) {
            args.put("seed", i+"");
            Board<? extends AbstractAgent> board = BoardFactory.constructBoard(args, out);
            performRun(args, board, metrics_, out);
         }
      } else {
         Board<? extends AbstractAgent> board = BoardFactory.constructBoard(args, null);
         performRun(args, board, metrics_, System.out);
      }
   }

   public static <AType extends AbstractAgent<AType>> void performRun(Arguments args, Board<AType> board, List<? extends Metric<AType>> metricList, PrintStream out) throws InterruptedException {
      int maxIters = args.getInt("maxIters",100);
      boolean color = args.getBool("color", true) & args.getBool("ansi",true);
      long sleep = args.getLong("sleep", 0);
      boolean printAgents = args.getBool("printAgents", true);
      boolean printSimilarity = args.getBool("printSimilarity", true);
      boolean metrics = args.getBool("metrics", false);
      boolean headless = args.getBool("headless", false);
      boolean animate = args.getBool("animate", false);

      System.out.printf("Proceeding with maxIters=%d, animate=%b, sleep=%d, color=%b, printAgents=%b, printSimilarity=%b, metrics=%b, headless=%b...\n", maxIters, animate, sleep, color, printAgents, printSimilarity, metrics, headless);
      ANSI.enabled = color;

      StringBuffer metricsBuffer = new StringBuffer();
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
         for (Metric<AType> m : metricList) {
            m.observe(board);
            metricsBuffer.append('\t').append(m.repr());
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
         out.printf("\t%d", epochs);
         for (Metric<AType> m : metricList) {
            m.observe(board);
            metricsBuffer.append('\t').append(m.repr());
         }
         out.println(metricsBuffer);
      }
   }

   public static void colorForScore(double score) {
      if (score < 0.5) {
         ANSI.lightRed();
      } else if (score < 0) {
         ANSI.red();
      } else if (score < 0.5) {
         ANSI.green();
      } else {
         ANSI.lightGreen();
      }
   }

   public static <AType extends AbstractAgent<AType>> void colorForAgentAt(AType a, Board<AType> b, Point p) {
      colorForScore(a.satisfactionScoreAt(b,p));
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
                  Point p = new Point(i,j);
                  double rawScore = a.neighborSimilarityAt(b,p);
                  double score = 100*rawScore;
                  colorForAgentAt(a, b, p);
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
