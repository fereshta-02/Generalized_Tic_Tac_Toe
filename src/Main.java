
import model.GameState;
import model.Player;
import model.Position;
import engine.GameEngine;
import algorithms.AlphaBetaSolver;
import algorithms.MinimaxSolver;
import strategies.HeuristicMoveOrdering;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        if (args.length > 0 && args[0].equals("test")) {
            runTests();
            return;
        }

        playGame();
    }

    public static void playGame() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Generalized Tic-Tac-Toe AI ===");
        System.out.print("Enter board size (m): ");
        int m = scanner.nextInt();
        System.out.print("Enter win condition (k): ");
        int k = scanner.nextInt();

        GameState state = GameEngine.initialState(m, k);
        AlphaBetaSolver solver;

        if (m == 3 && k == 3) {
            solver = new AlphaBetaSolver();
            System.out.println("Using optimal Alpha-Beta for 3x3 board");
        } else {
            solver = new AlphaBetaSolver(new HeuristicMoveOrdering());
            System.out.println("Using heuristic Alpha-Beta for larger board");
        }

        while (!state.isTerminal()) {
            System.out.println("\nCurrent board:");
            System.out.println(state);
            System.out.println("Current player: " + state.getCurrentPlayer().getSymbol());

            if (state.getCurrentPlayer() == Player.X) {
                System.out.println("AI is thinking...");
                Position aiMove = solver.findBestMove(state);
                System.out.println("AI plays: " + aiMove);
                state = state.makeMove(aiMove);
                System.out.println("Nodes evaluated: " + solver.getNodesEvaluated());
            } else {
                System.out.print("Enter your move (row col): ");
                int row = scanner.nextInt();
                int col = scanner.nextInt();
                Position humanMove = new Position(row, col);

                if (!state.getLegalMoves().contains(humanMove)) {
                    System.out.println("Invalid move! Try again.");
                    continue;
                }

                state = state.makeMove(humanMove);
            }
        }

        System.out.println("\n=== GAME OVER ===");
        System.out.println("Final board:");
        System.out.println(state);
        Player winner = state.getWinner();
        if (winner != null) {
            System.out.println("Winner: " + winner.getSymbol() + "!");
        } else {
            System.out.println("It's a draw!");
        }

        scanner.close();
    }

    public static void runTests() {
        System.out.println("=== RUNNING TESTS ===");

        testGameState();
        testWinConditions();
        testAlgorithms();
        testMoveOrdering();
        testLargerBoards();

        System.out.println("\n=== ALL TESTS COMPLETED ===");
    }

    public static void testGameState() {
        System.out.println("\n--- Testing GameState ---");

        GameState state = new GameState(3, 3);
        if (state.isTerminal()) {
            System.out.println("Initial state should not be terminal");
            return;
        }
        if (state.getCurrentPlayer() != Player.X) {
            System.out.println("First player should be X");
            return;
        }
        if (state.getLegalMoves().size() != 9) {
            System.out.println("Should have 9 legal moves initially");
            return;
        }
        if (state.getWinner() != null) {
            System.out.println(" No winner initially");
            return;
        }
        System.out.println("GameState initialization tests passed");

        Position move = new Position(0, 0);
        GameState newState = state.makeMove(move);
        if (newState.getCurrentPlayer() != Player.O) {
            System.out.println("Next player should be O");
            return;
        }
        if (newState.getPlayerAt(0, 0) != Player.X) {
            System.out.println("Position (0,0) should have X");
            return;
        }
        if (!state.isEmpty(0, 0)) {
            System.out.println("Original state should be unchanged");
            return;
        }
        System.out.println("Move making tests passed");

        if (newState.getLegalMoves().size() != 8) {
            System.out.println("Should have 8 legal moves after first move");
            return;
        }
        System.out.println("Legal moves tests passed");
    }

    public static void testWinConditions() {
        System.out.println("\n--- Testing Win Conditions ---");

        GameState state = new GameState(3, 3);
        state = state.makeMove(new Position(0, 0));
        state = state.makeMove(new Position(1, 0));
        state = state.makeMove(new Position(0, 1));
        state = state.makeMove(new Position(1, 1));
        state = state.makeMove(new Position(0, 2));

        if (!state.isTerminal()) {
            System.out.println("Horizontal win should end game");
            return;
        }
        if (state.getWinner() != Player.X) {
            System.out.println("X should win horizontally");
            return;
        }
        if (state.getUtility() != 1) {
            System.out.println(" Utility should be 1 for X win");
            return;
        }
        System.out.println("Horizontal win test passed");

        state = new GameState(3, 3);
        state = state.makeMove(new Position(0, 0));
        state = state.makeMove(new Position(0, 1));
        state = state.makeMove(new Position(1, 0));
        state = state.makeMove(new Position(1, 1));
        state = state.makeMove(new Position(2, 0));

        if (!state.isTerminal()) {
            System.out.println(" Vertical win should end game");
            return;
        }
        if (state.getWinner() != Player.X) {
            System.out.println("X should win vertically");
            return;
        }
        System.out.println("Vertical win test passed");

        state = new GameState(3, 3);
        state = state.makeMove(new Position(0, 0));
        state = state.makeMove(new Position(0, 1));
        state = state.makeMove(new Position(1, 1));
        state = state.makeMove(new Position(0, 2));
        state = state.makeMove(new Position(2, 2));

        if (!state.isTerminal()) {
            System.out.println("Diagonal win should end game");
            return;
        }
        if (state.getWinner() != Player.X) {
            System.out.println("X should win diagonally");
            return;
        }
        System.out.println("Diagonal win test passed");
    }

    public static void testAlgorithms() {
        System.out.println("\n--- Testing Algorithms ---");

        GameState state = new GameState(3, 3);

        MinimaxSolver minimax = new MinimaxSolver();
        Position mmMove = minimax.findBestMove(state);
        if (mmMove == null) {
            System.out.println("Minimax should return a valid move");
            return;
        }
        if (!state.getLegalMoves().contains(mmMove)) {
            System.out.println("Minimax move should be legal");
            return;
        }
        System.out.println("Minimax test passed - Move: " + mmMove + ", Nodes: " + minimax.getNodesEvaluated());

        AlphaBetaSolver alphaBeta = new AlphaBetaSolver();
        Position abMove = alphaBeta.findBestMove(state);
        if (abMove == null) {
            System.out.println("Alpha-Beta should return a valid move");
            return;
        }
        if (!state.getLegalMoves().contains(abMove)) {
            System.out.println(" Alpha-Beta move should be legal");
            return;
        }
        System.out.println("Alpha-Beta test passed - Move: " + abMove + ", Nodes: " + alphaBeta.getNodesEvaluated());

        if (!mmMove.equals(abMove)) {
            System.out.println("Minimax and Alpha-Beta should return same move");
            return;
        }
        System.out.println("Algorithm equivalence test passed");

        if (alphaBeta.getNodesEvaluated() >= minimax.getNodesEvaluated()) {
            System.out.println(" Alpha-Beta should evaluate fewer nodes than Minimax");
            return;
        }
        int nodesSaved = minimax.getNodesEvaluated() - alphaBeta.getNodesEvaluated();
        System.out.println("Pruning efficiency: " + nodesSaved + " nodes saved");
    }

    public static void testMoveOrdering() {
        System.out.println("\n--- Testing Move Ordering ---");

        GameState state = new GameState(3, 3);

        AlphaBetaSolver withoutOrdering = new AlphaBetaSolver();
        AlphaBetaSolver withOrdering = new AlphaBetaSolver(new HeuristicMoveOrdering());

        Position move1 = withoutOrdering.findBestMove(state);
        Position move2 = withOrdering.findBestMove(state);

        if (!move1.equals(move2)) {
            System.out.println("Moves should be the same with and without ordering");
            return;
        }
        if (withOrdering.getNodesEvaluated() > withoutOrdering.getNodesEvaluated()) {
            System.out.println("Move ordering should not increase node count");
            return;
        }

        int improvement = withoutOrdering.getNodesEvaluated() - withOrdering.getNodesEvaluated();
        System.out.println("Move ordering test passed - Improvement: " + improvement + " nodes");
    }

    public static void testLargerBoards() {
        System.out.println("\n--- Testing Larger Boards ---");

        GameState state4x4 = new GameState(4, 3);
        AlphaBetaSolver solver4x4 = new AlphaBetaSolver(new HeuristicMoveOrdering());

        Position move4x4 = solver4x4.findBestMove(state4x4);
        if (move4x4 == null) {
            System.out.println(" Should find valid move for 4x4 board");
            return;
        }
        if (!state4x4.getLegalMoves().contains(move4x4)) {
            System.out.println(" Move should be legal for 4x4 board");
            return;
        }
        System.out.println(" 4x4 board test passed - Move: " + move4x4);

        GameState state5x5 = new GameState(5, 4);
        AlphaBetaSolver solver5x5 = new AlphaBetaSolver(new HeuristicMoveOrdering());

        Position move5x5 = solver5x5.findBestMove(state5x5);
        if (move5x5 == null) {
            System.out.println(" Should find valid move for 5x5 board");
            return;
        }
        if (!state5x5.getLegalMoves().contains(move5x5)) {
            System.out.println("Move should be legal for 5x5 board");
            return;
        }
        System.out.println(" 5x5 board test passed - Move: " + move5x5);
    }
}