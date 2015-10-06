public interface Metric<AType extends Agent<AType>> {
   public void observe(Board<AType> board);
   public String repr();
   public String name();
}
