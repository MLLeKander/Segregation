public interface Agent<AgentType extends Agent<AgentType>> {
   public boolean isSatisfied(Board<AgentType> b);
   public void act(Board<AgentType> b);
}
