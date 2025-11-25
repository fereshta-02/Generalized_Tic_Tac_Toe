package strategies;

import model.GameState;
import model.Position;

import java.util.List;
public interface MoveOrderingStrategy {
    List<Position> orderMoves(GameState state, List<Position> moves);
}