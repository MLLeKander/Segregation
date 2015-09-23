import java.lang.reflect.Array;

class Sup<T> {
   T[] arr;

   @SuppressWarnings("unchecked")
   public Sup(int r) {
      arr = (T[])new Object[r];
      //arr = (T[])Array.newInstance(Object.class, r);
   }

   public void printSup() {
      System.out.println("Printing from sup.");
      for (Object i : arr) {
         System.out.print(i+" ");
      }
      System.out.println();
   }
}

class Sub extends Sup<String> {
   public Sub(int r) {
      super(r);
   }
   
   public void printSub() {
      System.out.println("Printing from sub.");
      for (Object i : arr) {
         System.out.print(i+" ");
      }
      System.out.println();
   }

   public static void main(String[] args) {
      Sub s = new Sub(4);
      s.printSup();
      s.printSub();
   }
}
