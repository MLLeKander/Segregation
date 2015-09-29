public class DoubleAgent extends AbstractAgent<DoubleAgent> {
   public boolean colorA, colorB;

   public DoubleAgent(boolean maximiserStrat, double similarityMin, double similarityMax, boolean colorA, boolean colorB) {
      super(similarityMin, similarityMax, maximiserStrat);
      this.colorA = colorA;
      this.colorB = colorB;
   }

   @Override
   public String toString() {
      return (colorA ? "{" : "<") + (colorB ? "}" : ">");
   }

   @Override
   public double similarityTo(DoubleAgent that) {
      int count = 0;
      if (that.colorA == this.colorA) {
         count++;
      }
      if (that.colorB == this.colorB) {
         count++;
      }
      return count/2.;
   }
}
