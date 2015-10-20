import java.util.Random;
import java.util.Arrays;
import java.io.PrintStream;

public class BoardFactory {
   public static Board<? extends AbstractAgent> constructBoard(Arguments args, PrintStream out) {
      String agentType = args.get("agentType", "double").toLowerCase();
      if (agentType.equals("schelling")) {
         return constructSchellingBoard(args, out);
      } else if (agentType.equals("double")) {
         return constructDoubleBoard(args, out);
      } else if (agentType.equals("multi")) {
         return constructMultiBoard(args, out);
      } else {
         throw new IllegalArgumentException("Unknown agent type: "+agentType);
      }
   }

   public static void printInitParams(int numFeatures, long seed, double similarity, double empty, int rows, int cols, boolean maximizer, double[] threshes, PrintStream out) {
      if (out == null) {
         return;
      }
      String stratName = maximizer ? "maximizer" : "satisficer";
      out.printf("%d\t%d\t%.3f\t%.3f\t%d\t%d\t%s",numFeatures,seed,similarity,empty,rows,cols,stratName);
      //out.printf("%d\t%d\t%.3f\t%.3f\t%d\t%d\t%s\t",numFeatures,seed,similarity,empty,rows,cols,stratName);
      //String sep = "";
      //for (double d : threshes) {
      //   out.printf("%s%.3f",sep,d);
      //   sep = ",";
      //}
   }

   public static Board<SchellingAgent> constructSchellingBoard(Arguments args, PrintStream out) {
      long seed = args.getLong("seed",1);
      int rows = args.getInt("rows", 13);
      int cols = args.getInt("cols", 16);
      double similarity = args.getDbl("similarity",0.4);
      double similarityMax = args.getDbl("similarityMax",1);
      double empty = args.getDbl("empty", 0.2);
      double thresh = args.getDbl("thresh", 0.5);
      boolean maximizer = args.getBool("maximizer", false);
      printInitParams(1, seed, similarity, empty, rows, cols, maximizer, new double[]{thresh}, out);
      return constructSchellingBoard(seed, rows, cols, similarity, similarityMax, empty, thresh, maximizer);
   }

   public static Board<SchellingAgent> constructSchellingBoard(long seed, int rows, int cols, double similarity, double similarityMax, double empty, double thresh, boolean maximizer) {
      System.out.printf("Constructing a SchellingAgent board with seed=%d, rows=%d, cols=%d, similarity=%.3f, similarityMax=%.3f, empty=%.3f, thresh=%.3f, maximizer=%b...\n", seed, rows, cols, similarity, similarityMax, empty, thresh, maximizer);
      Board<SchellingAgent> board = new Board<>(rows, cols);
      Random rand = new Random(seed);
      for (int r = 0; r < rows; r++) {
         for (int c = 0; c < cols; c++) {
            if (rand.nextDouble() > empty) {
               boolean color = rand.nextDouble() > thresh;
               SchellingAgent agent = new SchellingAgent(maximizer, similarity, similarityMax, color);

               board.addAgent(agent, new Point(r,c));
            }
         }
      }
      return board;
   }

   public static Board<DoubleAgent> constructDoubleBoard(Arguments args, PrintStream out) {
      long seed = args.getLong("seed",1);
      int rows = args.getInt("rows", 13);
      int cols = args.getInt("cols", 16);
      double similarity = args.getDbl("similarity",0.4);
      double similarityMax = args.getDbl("similarityMax",1);
      double empty = args.getDbl("empty", 0.2);
      double aThresh = args.getDbl("aThresh", 0.5);
      double bThresh = args.getDbl("bThresh", 0.5);
      boolean maximizer = args.getBool("maximizer", false);
      printInitParams(2, seed, similarity, empty, rows, cols, maximizer, new double[]{aThresh, bThresh}, out);
      return constructDoubleBoard(seed, rows, cols, similarity, similarityMax, empty, aThresh, bThresh, maximizer);
   }

   public static Board<DoubleAgent> constructDoubleBoard(long seed, int rows, int cols, double similarity, double similarityMax, double empty, double aThresh, double bThresh, boolean maximizer) {
      System.out.printf("Constructing a DoubleAgent board with seed=%d, rows=%d, cols=%d, similarity=%.3f, similarityMax=%.3f, empty=%.3f, aThresh=%.3f, bThresh=%.3f, maximizer=%b...\n", seed, rows, cols, similarity, similarityMax, empty, aThresh, bThresh, maximizer);
      Random rand = new Random(seed);
      Board<DoubleAgent> board = new Board<>(rows, cols);
      for (int r = 0; r < rows; r++) {
         for (int c = 0; c < cols; c++) {
            if (rand.nextDouble() > empty) {
               boolean a = rand.nextDouble() > aThresh;
               boolean b = rand.nextDouble() > bThresh;
               DoubleAgent agent = new DoubleAgent(maximizer, similarity, similarityMax, a, b);

               board.addAgent(agent, new Point(r,c));
            }
         }
      }
      return board;
   }

   public static double[] getThreshes(Arguments args) {
      double[] out;
      if (args.hasArg("threshes")) {
         String threshesStr = args.get("threshes");
         String[] threshesStrs = threshesStr.split(":");
         out = new double[threshesStrs.length];
         for (int i = 0; i < threshesStrs.length; i++) {
           out[i] = Double.parseDouble(threshesStrs[i]);
         }
      } else {
         out = new double[args.getInt("numfeatures",5)];
         Arrays.fill(out, args.getDbl("thresh", 0.5));
      }
      return out;
   }

   private static String toString(double[] threshes) {
      StringBuilder sb = new StringBuilder();
      String sep="";
      for (double thresh : threshes) {
         sb.append(sep);
         sep = ":";
         sb.append(String.format("%.3f",thresh));
      }
      return sb.toString();
   }

   public static Board<MultiAgent> constructMultiBoard(Arguments args, PrintStream out) {
      long seed = args.getLong("seed",1);
      int rows = args.getInt("rows", 13);
      int cols = args.getInt("cols", 16);
      double similarity = args.getDbl("similarity",0.4);
      double similarityMax = args.getDbl("similarityMax",1);
      double empty = args.getDbl("empty", 0.2);
      double[] threshes = getThreshes(args);
      boolean maximizer = args.getBool("maximizer", false);
      printInitParams(threshes.length, seed, similarity, empty, rows, cols, maximizer, threshes, out);
      return constructMultiBoard(seed, rows, cols, similarity, similarityMax, empty, threshes, maximizer);
   }

   public static Board<MultiAgent> constructMultiBoard(long seed, int rows, int cols, double similarity, double similarityMax, double empty, double[] threshes, boolean maximizer) {
      System.out.printf("Constructing a MultiAgent board with seed=%d, rows=%d, cols=%d, similarity=%.3f, similarityMax=%.3f, empty=%.3f, threshes=%s, maximizer=%b...\n", seed, rows, cols, similarity, similarityMax, empty, toString(threshes), maximizer);
      Random rand = new Random(seed);
      Board<MultiAgent> board = new Board<>(rows, cols);
      for (int r = 0; r < rows; r++) {
         for (int c = 0; c < cols; c++) {
            if (rand.nextDouble() > empty) {
               boolean[] features = new boolean[threshes.length];
               for (int i = 0; i < threshes.length; i++) {
                  features[i] = rand.nextDouble() > threshes[i];
               }
               MultiAgent agent = new MultiAgent(maximizer, similarity, similarityMax, features);

               board.addAgent(agent, new Point(r,c));
            }
         }
      }
      return board;
   }
}
