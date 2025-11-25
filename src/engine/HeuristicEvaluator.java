package engine;

import model.GameState;
import model.Player;

public class HeuristicEvaluator {
    public int evaluate(GameState state, Player player) {
        if (state.isTerminal()) {
            return state.getUtility() * (player == Player.X ? 1 : -1);
        }

        int score = 0;
        score += evaluateLines(state, player);
        score += evaluateCenterControl(state, player);
        score += evaluateMobility(state, player);

        return score;
    }

    private int evaluateLines(GameState state, Player player) {
        int score = 0;
        int m = state.getSize();
        int k = state.getK();

        // Check rows
        for (int i = 0; i < m; i++) {
            for (int j = 0; j <= m - k; j++) {
                score += evaluateLine(state, i, j, 0, 1, k, player);
            }
        }

        // Check columns
        for (int j = 0; j < m; j++) {
            for (int i = 0; i <= m - k; i++) {
                score += evaluateLine(state, i, j, 1, 0, k, player);
            }
        }

        // Check diagonals
        for (int i = 0; i <= m - k; i++) {
            for (int j = 0; j <= m - k; j++) {
                score += evaluateLine(state, i, j, 1, 1, k, player);
            }
        }

        // Check anti-diagonals
        for (int i = 0; i <= m - k; i++) {
            for (int j = k - 1; j < m; j++) {
                score += evaluateLine(state, i, j, 1, -1, k, player);
            }
        }

        return score;
    }

    private int evaluateLine(GameState state, int startRow, int startCol,
                             int deltaRow, int deltaCol, int length, Player player) {
        int playerCount = 0;
        int opponentCount = 0;
        Player opponent = player.getOpponent();

        for (int i = 0; i < length; i++) {
            int row = startRow + i * deltaRow;
            int col = startCol + i * deltaCol;
            Player cell = state.getPlayerAt(row, col);

            if (cell == player) {
                playerCount++;
            } else if (cell == opponent) {
                opponentCount++;
            }
        }

        if (playerCount > 0 && opponentCount > 0) {
            return 0;
        }

        if (playerCount > 0) {
            return (int) Math.pow(10, playerCount);
        } else if (opponentCount > 0) {
            return -(int) Math.pow(10, opponentCount);
        }

        return 0;
    }

    private int evaluateCenterControl(GameState state, Player player) {
        int m = state.getSize();
        int center = m / 2;
        int score = 0;

        if (state.getPlayerAt(center, center) == player) {
            score += 3;
        }

        for (int i = center - 1; i <= center + 1; i++) {
            for (int j = center - 1; j <= center + 1; j++) {
                if (i >= 0 && i < m && j >= 0 && j < m && state.getPlayerAt(i, j) == player) {
                    score += 1;
                }
            }
        }

        return score;
    }

    private int evaluateMobility(GameState state, Player player) {
        return state.getLegalMoves().size();
    }
}