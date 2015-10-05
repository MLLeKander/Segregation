public interface Agent<AgentType extends Agent<AgentType>> {
   public boolean isSatisfied(Board<AgentType> b);
   public void act(Board<AgentType> b);
   public double similarityTo(AgentType o);
   public int numFeatures();
   public double compareFeature(AgentType o, int featureNdx);
   public void visitNeighbors(Board<AgentType> board, NeighborhoodVisitor<AgentTye> visitor);
}
