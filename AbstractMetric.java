import java.util.ArrayList;

public abstract class AbstractMetric<AType extends Agent<AType>> implements Metric<AType> {
   public String name;
   public ArrayList<ArrayList<?>> log = new ArrayList<>();

   public AbstractMetric(String name) {
      this.name = name;
   }

   @Override
   public String repr() {
      StringBuilder sb = new StringBuilder(name+":");
      String sep1 = "", sep2 = "";
      for (ArrayList<?> row : log) {
         sb.append(sep1);
         sep1 = ":";
         sep2 = "";
         for (Object i : row) {
            sb.append(sep2);
            sep2 = ",";
            sb.append(format(i));
         }
      }
      return sb.toString();
   }

   protected String format(Object dataObj) {
      return dataObj.toString();
   }
}
