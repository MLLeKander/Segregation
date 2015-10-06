import java.util.*;

public abstract class AbstractMetric<AType extends Agent<AType>, LType extends Comparable<LType>> implements Metric<AType> {
   public String name;
   public ArrayList<LType> log = new ArrayList<>();

   public AbstractMetric(String name) {
      this.name = name;
   }

   @Override
   public String repr() {
      Collections.sort(log);
      StringBuilder sb = new StringBuilder();
      String sep = "";
      for (LType i : log) {
         sb.append(sep);
         sep = ",";
         sb.append(format(i));
      }
      return sb.toString();
   }

   protected String format(Object dataObj) {
      return dataObj.toString();
   }

   @Override
   public String name() { return name; }
}
