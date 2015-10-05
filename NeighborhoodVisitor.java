public interface NeighborhoodVisitor<AType extends Agent<AType>> {
   public void visit(Board<AType> board, AType base, AType  neighbor);
}
