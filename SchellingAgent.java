public class SchellingAgent extends AbstractAgent<SchellingAgent> {
   public boolean color;

   public SchellingAgent(boolean maximiserStrat, double similarityMin, double similarityMax, boolean color) {
      super(similarityMin, similarityMax, maximiserStrat);
      this.color = color;
   }

   @Override
   public String toString() {
      return color ? "#" : "O";
   }

   @Override
   public double similarityTo(SchellingAgent that) {
      return (this.color==that.color) ? 1 : 0;
   }

   @Override
   public int numFeatures() { return 1; }

   @Override
   public double compareFeature(SchellingAgent that, int featureNdx) {
      return (this.color==that.color) ? 1 : 0;
   }
}
