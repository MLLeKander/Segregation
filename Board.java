import java.util.*;

public class Board {
   protected Agent[][] cells;
   protected HashMap<Agent, Point> agentLookup = new HashMap<Agent, Point>();
   private int agentCnt = 0;
   private Point boardSize;

   public Board(int r, int c) {
      cells = new Agent[r][c];
      boardSize = new Point(r,c);
   }

   public void addAgent(Agent a, int r, int c) {
      addAgent(a, new Point(r,c));
   }

   public void addAgent(Agent a, Point p) {
      if (cells[p.r][p.c] != null) {
         throw new IllegalArgumentException(String.format("Attempt to place agent at [%d,%d], which is not null.",p.r,p.c));
      } else if (agentLookup.containsKey(a)) {
         throw new IllegalArgumentException("Attempt to add agent already on board.");
      }

      cells[p.r][p.c] = a;
      agentLookup.put(a,p);
      agentCnt++;
   }

   public Point getPos(Agent a) {
      return agentLookup.get(a);
   }

   public Agent getAgent(int r, int c) {
      return cells[r][c];
   }

   public Agent getAgent(Point p) {
      return cells[p.r][p.c];
   }

   public int getNumAgents() {
      return agentCnt;
   }

   public Point getBoardSize() {
      return boardSize;
   }

   /**
    * Perform a single epoch, whereby unsatisfied agents move in the board.
    *    Returns true if at least one agent was unsatisfied during this epoch.
    */
   public boolean performEpoch() {
      ArrayList<Agent> unsatisfieds = new ArrayList<Agent>();
      for (Agent[] row : cells) {
         for (Agent a : row) {
            if (a != null && !a.isSatisfied(this)) {
               unsatisfieds.add(a);
            }
         }
      }

      for (Agent a : unsatisfieds) {
         a.act(this);
      }
      return unsatisfieds.size() != 0;
   }

   /**
    * Move the specified agent to the specified location.
    *  Throws an IllegalArgumentException if to already contains an agent.
    */
   public void move(Agent a, Point to) {
      if (cells[to.r][to.c] != null) {
         throw new IllegalArgumentException(String.format("Attempt to move to [%d,%d], which is not null.",to.r,to.c));
      }
      Point from = getPos(a);
      cells[from.r][from.c] = null;

      cells[to.r][to.c] = a;
      agentLookup.put(a, to);
   }
}
