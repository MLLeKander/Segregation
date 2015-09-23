public class Colors {

   public static void reset() {
      System.out.print("\u001B[0m");
   }

   public static void red() {
      System.out.print("\u001B[0;31m");
   }

   public static void lightRed() {
      System.out.print("\u001B[0;1;31m");
   }

   public static void lightGreen() {
      System.out.print("\u001B[0;1;32m");
   }

   public static void green() {
      System.out.print("\u001B[0;32m");
   }
}
