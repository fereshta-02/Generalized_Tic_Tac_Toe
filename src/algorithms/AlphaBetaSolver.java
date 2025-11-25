package algorithms;

import model.GameState;
import model.Position;
import strategies.MoveOrderingStrategy;
import strategies.DefaultMoveOrdering;
import java.util.List;

public class AlphaBetaSolver {
    private int nodesEvaluated;
    private MoveOrderingStrategy moveOrdering;

    public AlphaBetaSolver() {
        this(new DefaultMoveOrdering());
    }

    public AlphaBetaSolver(MoveOrderingStrategy moveOrdering) {
        this.nodesEvaluated = 0;
        this.moveOrdering = moveOrdering;
    }

    public Position findBestMove(GameState state) {
        nodesEvaluated = 0;
        List<Position> moves = moveOrdering.orderMoves(state, state.getLegalMoves());

        int bestValue = Integer.MIN_VALUE;
        Position bestMove = moves.get(0);

        for (Position move : moves) {
            GameState newState = state.makeMove(move);
            int moveValue = alphaBeta(newState, Integer.MIN_VALUE, Integer.MAX_VALUE, false);

            if (moveValue > bestValue) {
                bestValue = moveValue;
                bestMove = move;
            }
        }

        return bestMove;
    }

    private int alphaBeta(GameState state, int alpha, int beta, boolean isMaximizing) {
        nodesEvaluated++;

        if (state.isTerminal()) {
            return state.getUtility();
        }

        List<Position> moves = moveOrdering.orderMoves(state, state.getLegalMoves());

        if (isMaximizing) {
            int maxEval = Integer.MIN_VALUE;
            for (Position move : moves) {
                GameState newState = state.makeMove(move);
                int eval = alphaBeta(newState, alpha, beta, false);
                maxEval = Math.max(maxEval, eval);
                alpha = Math.max(alpha, eval);
                if (beta <= alpha) {
                    break;
                }
            }
            return maxEval;
        } else {
            int minEval = Integer.MAX_VALUE;
            for (Position move : moves) {
                GameState newState = state.makeMove(move);
                int eval = alphaBeta(newState, alpha, beta, true);
                minEval = Math.min(minEval, eval);
                beta = Math.min(beta, eval);
                if (beta <= alpha) {
                    break;
                }
            }
            return minEval;
        }
    }

    public int getNodesEvaluated() {
        return nodesEvaluated;
    }
}