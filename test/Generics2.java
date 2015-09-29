import java.util.*;

class Generics2 {
   public enum A { A1, A2 }
   public enum B { B1, B2 }

   public static List<? extends Object> listFactory(String[] args) {
      if (args.length == 0) {
         return new ArrayList<A>(Arrays.asList(A.A1, A.A2));
      } else {
         return new ArrayList<B>(Arrays.asList(B.B1, B.B2));
      }
   }

   public static void main(String[] args) {
      List<?> lst = listFactory(args);
      dblList(lst);
      System.out.println(lst);
   }

   public static <LType> void dblList(List<LType> lst) {
      int size = lst.size();
      for (int i = 0; i < size; i++) {
         lst.add(lst.get(i));
      }
   }
}
