import java.util.*;
import java.lang.reflect.Array;

public class Board<AgentType extends Agent<AgentType>> {
   private AgentType[][] cells;
   protected HashMap<AgentType, Point> agentLookup = new HashMap<AgentType, Point>();
   private int agentCnt = 0;
   private Point boardSize;

   @SuppressWarnings("unchecked")
   public Board(int r, int c) {
      cells = (AgentType[][])Array.newInstance(Agent.class,r,c);
      boardSize = new Point(r,c);
   }

   public void addAgent(AgentType a, int r, int c) {
      addAgent(a, new Point(r,c));
   }

   public void addAgent(AgentType a, Point p) {
      if (cells[p.r][p.c] != null) {
         throw new IllegalArgumentException(String.format("Attempt to place agent at [%d,%d], which is not null.",p.r,p.c));
      } else if (agentLookup.containsKey(a)) {
         throw new IllegalArgumentException("Attempt to add agent already on board.");
      }

      cells[p.r][p.c] = a;
      agentLookup.put(a,p);
      agentCnt++;
   }

   public Point getPos(AgentType a) {
      return agentLookup.get(a);
   }

   public AgentType getAgent(int r, int c) {
      return cells[r][c];
   }

   public AgentType getAgent(Point p) {
      return cells[p.r][p.c];
   }

   public Point removeAgent(AgentType a) {
      Point p = agentLookup.remove(a);
      if (cells[p.r][p.c] != a) {
         throw new IllegalStateException("???");
      }
      cells[p.r][p.c] = null;
      agentCnt--;
      return p;
   }

   public AgentType removeAgent(Point p) {
      AgentType a = cells[p.r][p.c];
      cells[p.r][p.c] = null;
      if (!p.equals(agentLookup.remove(a))) {
         throw new IllegalStateException("???");
      }
      agentCnt--;
      return a;
   }

   public int getNumAgents() {
      return agentCnt;
   }

   public int getNumEmpty() {
      return boardSize.r*boardSize.c - agentCnt;
   }

   public Point getBoardSize() {
      return boardSize;
   }

   /**
    * Perform a single epoch, whereby unsatisfied agents move in the board.
    *    Returns true if at least one agent was unsatisfied during this epoch.
    */
   @SuppressWarnings("unchecked")
   public boolean performEpoch() {
      ArrayList<AgentType> unsatisfieds = new ArrayList<AgentType>();
      for (AgentType[] row : cells) {
         for (AgentType a : row) {
            if (a != null && !a.isSatisfied(this)) {
               unsatisfieds.add(a);
            }
         }
      }

      for (AgentType a : unsatisfieds) {
         a.act(this);
      }
      return unsatisfieds.size() != 0;
   }

   /**
    * Move an agent to the specified location.
    *  Throws an IllegalArgumentException if to already contains an agent.
    */
   public void move(AgentType a, Point to) {
      Point from = getPos(a);
      if (from.equals(to)) return;

      if (cells[to.r][to.c] != null) {
         throw new IllegalArgumentException(String.format("Attempt to move to [%d,%d], which is not null.",to.r,to.c));
      }
      cells[from.r][from.c] = null;

      cells[to.r][to.c] = a;
      agentLookup.put(a, to);
   }

   public boolean isBounded(int r, int c) {
      return r >= 0 && r < boardSize.r && c >= 0 && c < boardSize.c;
   }
}
