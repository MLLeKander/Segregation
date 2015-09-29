public interface MigrationStrategy<AgentType extends AbstractAgent<AgentType>> {
   public Point findPoint(Board<AgentType> board, AgentType agent, Point start);
}
