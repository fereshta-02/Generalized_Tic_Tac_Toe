package test;

import algorithms.AlphaBetaSolver;
import algorithms.DepthLimitedSolver;
import algorithms.MinimaxSolver;
import engine.GameEngine;
import model.GameState;
import model.Player;
import model.Position;
import strategies.HeuristicMoveOrdering;

public class TestGame {
    public static void main(String[] args) {
        testInitialState();
        testWinDetection();
        testMinimaxEquivalence();
        testAlphaBetaPruning();
        testLargerBoard();
        System.out.println("All tests passed!");
    }

    public static void testInitialState() {
        GameState state = GameEngine.initialState(3, 3);
        assertFalse(state.isTerminal(), "Initial state should not be terminal");
        assertEquals(Player.X, state.getCurrentPlayer(), "First player should be X");
        assertEquals(9, state.getLegalMoves().size(), "Should have 9 legal moves initially");
        System.out.println("testInitialState passed");
    }

    public static void testWinDetection() {
        GameState state = GameEngine.initialState(3, 3);

        state = state.makeMove(new Position(0, 0));
        state = state.makeMove(new Position(1, 0));
        state = state.makeMove(new Position(0, 1));
        state = state.makeMove(new Position(1, 1));
        state = state.makeMove(new Position(0, 2));

        assertTrue(state.isTerminal(), "Game should be terminal after win");
        assertEquals(Player.X, state.getWinner(), "X should be the winner");
        assertEquals(1, state.getUtility(), "Utility should be 1 for X win");
        System.out.println(" testWinDetection passed");
    }

    public static void testMinimaxEquivalence() {
        GameState state = GameEngine.initialState(3, 3);

        MinimaxSolver minimax = new MinimaxSolver();
        AlphaBetaSolver alphaBeta = new AlphaBetaSolver();

        Position mmMove = minimax.findBestMove(state);
        Position abMove = alphaBeta.findBestMove(state);

        assertEquals(mmMove, abMove, "Minimax and Alpha-Beta should return same moves");
        System.out.println(" testMinimaxEquivalence passed");
        System.out.println("   Minimax nodes: " + minimax.getNodesEvaluated());
        System.out.println("   AlphaBeta nodes: " + alphaBeta.getNodesEvaluated());
    }

    public static void testAlphaBetaPruning() {
        GameState state = GameEngine.initialState(3, 3);

        AlphaBetaSolver withOrdering = new AlphaBetaSolver(new HeuristicMoveOrdering());
        AlphaBetaSolver withoutOrdering = new AlphaBetaSolver();

        Position move1 = withOrdering.findBestMove(state);
        Position move2 = withoutOrdering.findBestMove(state);

        assertEquals(move1, move2, "Moves should be equal with and without ordering");
        assertTrue(withOrdering.getNodesEvaluated() <= withoutOrdering.getNodesEvaluated(),
                "Ordering should reduce node count");
        System.out.println(" testAlphaBetaPruning passed");
        System.out.println("   Without ordering nodes: " + withoutOrdering.getNodesEvaluated());
        System.out.println("   With ordering nodes: " + withOrdering.getNodesEvaluated());
        System.out.println("   Improvement: " + (withoutOrdering.getNodesEvaluated() - withOrdering.getNodesEvaluated()) + " nodes");
    }

    public static void testLargerBoard() {
        GameState state = GameEngine.initialState(4, 3);
        DepthLimitedSolver solver = new DepthLimitedSolver(3);

        Position move = solver.findBestMove(state);
        assertNotNull(move, "Should return a valid move");
        assertTrue(state.getLegalMoves().contains(move), "Move should be legal");
        System.out.println(" testLargerBoard passed - Move: " + move);
    }

    private static void assertTrue(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError( message);
        }
    }

    private static void assertFalse(boolean condition, String message) {
        if (condition) {
            throw new AssertionError( message);
        }
    }

    private static void assertEquals(Object expected, Object actual, String message) {
        if (!expected.equals(actual)) {
            throw new AssertionError(message + " - Expected: " + expected + ", Actual: " + actual);
        }
    }

    private static void assertNotNull(Object obj, String message) {
        if (obj == null) {
            throw new AssertionError( message);
        }
    }
}