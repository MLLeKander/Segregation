public abstract class AbstractAgent<AgentType extends AbstractAgent<AgentType>> implements Agent<AgentType> {
   protected MigrationStrategy<AgentType> strategy;
   private double similarityMin, similarityMax;

   public AbstractAgent(double similarityMin, double similarityMax, MigrationStrategy<AgentType> s) {
      this.similarityMin = similarityMin;
      this.similarityMax = similarityMax;
      this.strategy = s;
   }

   @SuppressWarnings("unchecked")
   public boolean isSatisfied(Board<AgentType> b) {
      return isSatisfiedAt(b, b.getPos((AgentType)this));
   }
   
   public boolean isSatisfiedAt(Board<AgentType> board, Point p) {
      return satisfactionScoreAt(board, p) >= 0;
   }

   public abstract double neighborSimilarityAt(Board<AgentType> board, Point p);

   public boolean isBounded(Board<AgentType> b, int r, int c) {
      Point bound = b.getBoardSize();
      return r >= 0 && r < bound.r && c >= 0 && c < bound.c;
   }

   public AgentType boundedGetAgent(Board<AgentType> b, int r, int c) {
      return isBounded(b,r,c) ? b.getAgent(r,c) : null;
   }

   public double satisfactionScoreAt(Board<AgentType> board, Point p) {
      double similarity = neighborSimilarityAt(board, p);
      double out;
      if (similarityMax == 1) {
         out = interp_(similarity,similarityMin);
      } else {
         out = interp_(similarity,similarityMin,similarityMax);
      }
      //System.err.printf("in:%f out:%f sim:%f\n",similarity,out,similarityMin);
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
