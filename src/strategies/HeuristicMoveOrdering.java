package strategies;

import engine.HeuristicEvaluator;
import model.GameState;
import model.Position;
import java.util.List;
import java.util.ArrayList;

public class HeuristicMoveOrdering implements MoveOrderingStrategy {
    private HeuristicEvaluator evaluator;

    public HeuristicMoveOrdering() {
        this.evaluator = new HeuristicEvaluator();
    }

    @Override
    public List<Position> orderMoves(GameState state, List<Position> moves) {
        List<ScoredMove> scoredMoves = new ArrayList<>();

        for (Position move : moves) {
            GameState newState = state.makeMove(move);
            int score = evaluator.evaluate(newState, state.getCurrentPlayer());
            scoredMoves.add(new ScoredMove(move, score));
        }

        scoredMoves.sort((a, b) -> Integer.compare(b.score, a.score));

        List<Position> result = new ArrayList<>();
        for (ScoredMove scoredMove : scoredMoves) {
            result.add(scoredMove.move);
        }

        return result;
    }

    private static class ScoredMove {
        Position move;
        int score;

        ScoredMove(Position move, int score) {
            this.move = move;
            this.score = score;
        }
    }
}
