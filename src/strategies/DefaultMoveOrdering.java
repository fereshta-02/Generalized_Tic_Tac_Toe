package strategies;

import model.GameState;
import model.Position;
import java.util.List;
import java.util.ArrayList;

public class DefaultMoveOrdering implements MoveOrderingStrategy {
    @Override
    public List<Position> orderMoves(GameState state, List<Position> moves) {
        List<Position> sorted = new ArrayList<>(moves);
        sorted.sort((a, b) -> {
            if (a.row != b.row) return Integer.compare(a.row, b.row);
            return Integer.compare(a.col, b.col);
        });
        return sorted;
    }
}
