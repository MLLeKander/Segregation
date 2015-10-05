public interface Agent<AgentType extends Agent<AgentType>> {
   public boolean isSatisfied(Board<AgentType> b);
   public void act(Board<AgentType> b);
   public double similarityTo(AgentType o);
   public int numFeatures();
   public double compareFeature(AgentType o, int featureNdx);
   public void visitNeighborsAt(Board<AgentType> board, Point p, NeighborhoodVisitor<AgentType> visitor);
}
