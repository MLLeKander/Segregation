public class ANSI {
   public static boolean enabled = true;

   public static void reset() {
      if (enabled) {
         System.out.print("\u001B[0m");
      }
   }

   public static void red() {
      if (enabled) {
         System.out.print("\u001B[0;31m");
      }
   }

   public static void lightRed() {
      if (enabled) {
         System.out.print("\u001B[0;1;31m");
      }
   }

   public static void lightGreen() {
      if (enabled) {
         System.out.print("\u001B[0;1;32m");
      }
   }

   public static void green() {
      if (enabled) {
         System.out.print("\u001B[0;32m");
      }
   }

   public static void moveUp(int lines) {
      if (enabled) {
         System.out.print("\u001B["+lines+"A");
      }
   }
}
