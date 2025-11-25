package strategies;

import model.GameState;
import model.Position;
import java.util.List;
import java.util.ArrayList;

public class CenterFirstMoveOrdering implements MoveOrderingStrategy {
    @Override
    public List<Position> orderMoves(GameState state, List<Position> moves) {
        int m = state.getSize();
        int center = m / 2;

        List<Position> sorted = new ArrayList<>(moves);
        sorted.sort((a, b) -> {
            int distA = Math.abs(a.row - center) + Math.abs(a.col - center);
            int distB = Math.abs(b.row - center) + Math.abs(b.col - center);
            return Integer.compare(distA, distB);
        });

        return sorted;
    }
}
