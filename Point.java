public class Point {
   int r, c;
   public Point(int r_, int c_) {r=r_; c=c_;}
   public boolean equals(Object o) {
      if (o.getClass() != this.getClass()) {
         return false;
      }
      Point po = (Point)o;
      return po.r == r && po.c == c;
   }

   public String toString() { return String.format("[%d,%d]",r,c);}
}
