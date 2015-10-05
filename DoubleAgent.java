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
      if (this.colorA == that.colorA) {
         count++;
      }
      if (this.colorB == that.colorB) {
         count++;
      }
      return count/2.;
   }

   @Override
   public int numFeatures() { return 2; }

   @Override
   public double compareFeature(DoubleAgent that, int featureNdx) {
      boolean tmp = featureNdx == 0 ? this.colorA == that.colorA : this.colorB == that.colorB;
      return tmp ? 1 : 0;
   }
}
