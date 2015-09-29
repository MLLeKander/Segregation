public abstract class AbstractAgent<AgentType extends AbstractAgent<AgentType>> implements Agent<AgentType> {
   protected MigrationStrategy<AgentType> strategy;
   private double similarityMin, similarityMax;

   @SuppressWarnings("unchecked")
   private final static MigrationStrategy maximizerStrategy = new MostSatisfied();
   @SuppressWarnings("unchecked")
   private final static MigrationStrategy satisficerStrategy = new CompositeStrategy(
         new ClosestSatisfied(),
         new MostSatisfied()
      );

   public AbstractAgent(double similarityMin, double similarityMax, MigrationStrategy<AgentType> s) {
      this.similarityMin = similarityMin;
      this.similarityMax = similarityMax;
      this.strategy = s;
   }

   @SuppressWarnings("unchecked")
   public AbstractAgent(double similarityMin, double similarityMax, boolean useMaximizer) {
      this(similarityMin, similarityMax, useMaximizer ? maximizerStrategy : satisficerStrategy);
   }

   @SuppressWarnings("unchecked")
   public boolean isSatisfied(Board<AgentType> b) {
      return isSatisfiedAt(b, b.getPos((AgentType)this));
   }
   
   public boolean isSatisfiedAt(Board<AgentType> board, Point p) {
      return satisfactionScoreAt(board, p) >= 0;
   }

   public double neighborSimilarityAt(Board<AgentType> board, Point p) {
      Point bound = board.getBoardSize();
      int neighborCnt = 0;
      double similarScore = 0;
      int row = p.r, col = p.c, brow = bound.r, bcol = bound.c;

      for (int drow = -1; drow <= 1; drow++) {
         for (int dcol = -1; dcol <= 1; dcol++) {
            if (drow != 0 || dcol != 0) {
               AgentType a = boundedGetAgent(board,row+drow,col+dcol);
               if (a != null) {
                  neighborCnt++;
                  similarScore += similarityTo(a);
               }
            }
         }
      }
      if (neighborCnt == 0) return 0;
      return similarScore/neighborCnt;
   }

   public AgentType boundedGetAgent(Board<AgentType> b, int r, int c) {
      return b.isBounded(r,c) ? b.getAgent(r,c) : null;
   }

   public double satisfactionScoreAt(Board<AgentType> board, Point p) {
      double similarity = neighborSimilarityAt(board, p);
      double out;
      if (similarityMax == 1) {
         out = interp_(similarity,similarityMin);
      } else {
         out = interp_(similarity,similarityMin,similarityMax);
      }
      return out;
   }

   private double interp_(double p, double min) {
      if (p < min) {
         return p/min-1;
      } else {
         return (p-min)/(1-min);
      }
   }

   private double interp_(double p, double min, double max) {
      double tailNorm = Math.max(min,1-max);
      if (p < min) {
         return p/tailNorm - 1;
      } else if (p < max) {
         double midpoint = (min+max)/2;
         return 1 - Math.abs(p-midpoint) / (midpoint-min);
      } else {
         return (max-p)/tailNorm;
      }
   }

   @SuppressWarnings("unchecked")
   public void act(Board<AgentType> b) {
      AgentType a = (AgentType)this;
      Point from = b.removeAgent(a);
      Point to = strategy.findPoint(b, a, from);
      b.addAgent(a, to != null ? to : from);
   }
}
