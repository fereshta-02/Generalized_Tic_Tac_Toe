package model;

import java.util.ArrayList;
import java.util.List;

public class GameState {
    private final Player[][] board;
    private final Player currentPlayer;
    private final int m;
    private final int k;
    private final Player winner;
    private final boolean isTerminal;

    public GameState(int m, int k) {
        this.m = m;
        this.k = k;
        this.board = new Player[m][m];
        this.currentPlayer = Player.X;
        this.winner = null;
        this.isTerminal = false;
    }

    private GameState(Player[][] board, Player currentPlayer, int m, int k, Player winner, boolean isTerminal) {
        this.m = m;
        this.k = k;
        this.board = copyBoard(board);
        this.currentPlayer = currentPlayer;
        this.winner = winner;
        this.isTerminal = isTerminal;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public Player getPlayerAt(int row, int col) {
        return board[row][col];
    }

    public int getSize() {
        return m;
    }

    public int getK() {
        return k;
    }

    public boolean isEmpty(int row, int col) {
        return board[row][col] == null;
    }

    public List<Position> getLegalMoves() {
        List<Position> moves = new ArrayList<>();
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < m; j++) {
                if (isEmpty(i, j)) {
                    moves.add(new Position(i, j));
                }
            }
        }
        return moves;
    }

    public GameState makeMove(Position move) {
        if (!isEmpty(move.row, move.col)) {
            throw new IllegalArgumentException("Position already occupied: " + move);
        }

        Player[][] newBoard = copyBoard(board);
        newBoard[move.row][move.col] = currentPlayer;

        Player nextPlayer = currentPlayer.getOpponent();
        Player newWinner = calculateWinner(newBoard, move);
        boolean newIsTerminal = (newWinner != null) || isBoardFull(newBoard);

        return new GameState(newBoard, nextPlayer, m, k, newWinner, newIsTerminal);
    }

    private Player calculateWinner(Player[][] board, Position lastMove) {
        if (lastMove == null) return null;

        Player player = board[lastMove.row][lastMove.col];
        if (player == null) return null;

        int[][] directions = {
                {0, 1},  // horizontal
                {1, 0},  // vertical
                {1, 1},  // diagonal
                {1, -1}  // anti-diagonal
        };

        for (int[] dir : directions) {
            int count = 1;

            count += countConsecutive(board, lastMove, dir[0], dir[1], player);
            count += countConsecutive(board, lastMove, -dir[0], -dir[1], player);

            if (count >= k) {
                return player;
            }
        }

        return null;
    }

    private int countConsecutive(Player[][] board, Position start, int deltaRow, int deltaCol, Player player) {
        int count = 0;
        int row = start.row + deltaRow;
        int col = start.col + deltaCol;

        while (row >= 0 && row < m && col >= 0 && col < m && board[row][col] == player) {
            count++;
            row += deltaRow;
            col += deltaCol;
        }

        return count;
    }

    private boolean isBoardFull(Player[][] board) {
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < m; j++) {
                if (board[i][j] == null) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isTerminal() {
        return isTerminal;
    }

    public Player getWinner() {
        return winner;
    }

    public int getUtility() {
        if (winner == null) return 0;
        return winner == Player.X ? 1 : -1;
    }

    private Player[][] copyBoard(Player[][] original) {
        Player[][] copy = new Player[m][m];
        for (int i = 0; i < m; i++) {
            System.arraycopy(original[i], 0, copy[i], 0, m);
        }
        return copy;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < m; j++) {
                String symbol = board[i][j] == null ? "." : board[i][j].getSymbol();
                sb.append(symbol).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}