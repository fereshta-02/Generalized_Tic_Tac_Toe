package engine;

import model.GameState;
import model.Player;
import model.Position;
import java.util.List;

public class GameEngine {
    public static GameState initialState(int m, int k) {
        return new GameState(m, k);
    }

    public static Player currentPlayer(GameState state) {
        return state.getCurrentPlayer();
    }

    public static List<Position> getLegalMoves(GameState state) {
        return state.getLegalMoves();
    }

    public static GameState makeMove(GameState state, Position move) {
        return state.makeMove(move);
    }

    public static boolean isTerminal(GameState state) {
        return state.isTerminal();
    }

    public static Player getWinner(GameState state) {
        return state.getWinner();
    }

    public static int getUtility(GameState state) {
        return state.getUtility();
    }
}