public class MultiAgent extends AbstractAgent<MultiAgent> {
   public boolean[] features;
   public int numFeatures;

   public MultiAgent(boolean maximiserStrat, double similarityMin, double similarityMax, boolean[] features) {
      super(similarityMin, similarityMax, maximiserStrat);
      this.features = features;
      this.numFeatures = features.length;
   }

   @Override
   public double similarityTo(MultiAgent that) {
      int similarCnt = 0;
      if (this.numFeatures != that.numFeatures) {
         throw new IllegalStateException("???");
      }

      for (int ndx = 0; ndx < this.numFeatures; ndx++) {
         if (features[ndx] == that.features[ndx]) {
            similarCnt++;
         }
      }
      return similarCnt/(double)numFeatures;
   }

   @Override
   public String toString() {
      StringBuilder sb = new StringBuilder();
      sb.append('{');
      for (boolean feature : features) {
         sb.append(feature ? '#' : 'O');
      }
      return sb.append('}').toString();
   }
}
