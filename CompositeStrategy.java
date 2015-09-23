public class CompositeStrategy<AgentType extends AbstractAgent<AgentType>> implements MigrationStrategy<AgentType> {
   private MigrationStrategy<AgentType>[] strats;

   @SafeVarargs
   @SuppressWarnings("varargs")
   public CompositeStrategy(MigrationStrategy<AgentType>... strats) {
      this.strats = strats;
   }

   public Point findPoint(Board<AgentType> board, AgentType agent) {
      for (MigrationStrategy<AgentType> strat : strats) {
         Point p = strat.findPoint(board, agent);
         if (p != null) {
            return p;
         }
      }
      return null;
   }
}
