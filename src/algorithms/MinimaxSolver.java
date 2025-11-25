package algorithms;

import model.GameState;
import model.Position;
import java.util.List;

public class MinimaxSolver {
    private int nodesEvaluated;

    public MinimaxSolver() {
        this.nodesEvaluated = 0;
    }

    public Position findBestMove(GameState state) {
        nodesEvaluated = 0;
        List<Position> moves = state.getLegalMoves();

        int bestValue = Integer.MIN_VALUE;
        Position bestMove = null;

        for (Position move : moves) {
            GameState newState = state.makeMove(move);
            int moveValue = minimax(newState, false);

            if (moveValue > bestValue) {
                bestValue = moveValue;
                bestMove = move;
            }
        }

        return bestMove;
    }

    private int minimax(GameState state, boolean isMaximizing) {
        nodesEvaluated++;

        if (state.isTerminal()) {
            return state.getUtility();
        }

        if (isMaximizing) {
            int maxEval = Integer.MIN_VALUE;
            for (Position move : state.getLegalMoves()) {
                GameState newState = state.makeMove(move);
                int eval = minimax(newState, false);
                maxEval = Math.max(maxEval, eval);
            }
            return maxEval;
        } else {
            int minEval = Integer.MAX_VALUE;
            for (Position move : state.getLegalMoves()) {
                GameState newState = state.makeMove(move);
                int eval = minimax(newState, true);
                minEval = Math.min(minEval, eval);
            }
            return minEval;
        }
    }

    public int getNodesEvaluated() {
        return nodesEvaluated;
    }
}
