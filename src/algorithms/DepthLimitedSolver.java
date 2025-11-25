package algorithms;

import model.GameState;
import model.Player;
import model.Position;
import engine.HeuristicEvaluator;
import strategies.MoveOrderingStrategy;
import strategies.DefaultMoveOrdering;
import java.util.List;

public class DepthLimitedSolver {
    private int maxDepth;
    private HeuristicEvaluator evaluator;
    private MoveOrderingStrategy moveOrdering;
    private int nodesEvaluated;

    public DepthLimitedSolver(int maxDepth) {
        this(maxDepth, new HeuristicEvaluator(), new DefaultMoveOrdering());
    }

    public DepthLimitedSolver(int maxDepth, HeuristicEvaluator evaluator, MoveOrderingStrategy moveOrdering) {
        this.maxDepth = maxDepth;
        this.evaluator = evaluator;
        this.moveOrdering = moveOrdering;
        this.nodesEvaluated = 0;
    }

    public Position findBestMove(GameState state) {
        nodesEvaluated = 0;
        List<Position> moves = moveOrdering.orderMoves(state, state.getLegalMoves());

        int bestValue = Integer.MIN_VALUE;
        Position bestMove = moves.get(0);
        Player maximizingPlayer = state.getCurrentPlayer();

        for (Position move : moves) {
            GameState newState = state.makeMove(move);
            int moveValue = alphaBeta(newState, maxDepth, Integer.MIN_VALUE,
                    Integer.MAX_VALUE, false, maximizingPlayer);

            if (moveValue > bestValue) {
                bestValue = moveValue;
                bestMove = move;
            }
        }

        return bestMove;
    }

    private int alphaBeta(GameState state, int depth, int alpha, int beta,
                          boolean isMaximizing, Player originalPlayer) {
        nodesEvaluated++;

        if (state.isTerminal() || depth == 0) {
            return evaluator.evaluate(state, originalPlayer);
        }

        List<Position> moves = moveOrdering.orderMoves(state, state.getLegalMoves());

        if (isMaximizing) {
            int maxEval = Integer.MIN_VALUE;
            for (Position move : moves) {
                GameState newState = state.makeMove(move);
                int eval = alphaBeta(newState, depth - 1, alpha, beta, false, originalPlayer);
                maxEval = Math.max(maxEval, eval);
                alpha = Math.max(alpha, eval);
                if (beta <= alpha) break;
            }
            return maxEval;
        } else {
            int minEval = Integer.MAX_VALUE;
            for (Position move : moves) {
                GameState newState = state.makeMove(move);
                int eval = alphaBeta(newState, depth - 1, alpha, beta, true, originalPlayer);
                minEval = Math.min(minEval, eval);
                beta = Math.min(beta, eval);
                if (beta <= alpha) break;
            }
            return minEval;
        }
    }

    public int getNodesEvaluated() {
        return nodesEvaluated;
    }
}
