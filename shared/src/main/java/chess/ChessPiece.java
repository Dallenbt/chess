package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor pieceColor;
    private final PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {

        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {

        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        var moves = new HashSet<ChessMove>();

        if (this.type == PieceType.BISHOP) {
            moves = bishop(board, myPosition);
        }
        if (this.type == PieceType.KNIGHT) {
            moves = knight(board, myPosition);
        }
        if (this.type == PieceType.ROOK) {
            moves = rook(board, myPosition);
        }
        if (this.type == PieceType.QUEEN) {
            moves = queen(board, myPosition);
        }
        if (this.type == PieceType.KING) {
            moves = king(board, myPosition);
        }
        if (this.type == PieceType.PAWN) {
            moves = pawn(board, myPosition);
        }

        return moves;
    }
    public HashSet<ChessMove> bishop(ChessBoard board, ChessPosition myPosition) {
        var moves = new HashSet<ChessMove>();

        int[][] directions = {
                {1, 1},
                {1, -1},
                {-1, 1},
                {-1, -1}
        };

        for (int[] dir : directions) {
            int row = myPosition.getRow();
            int col = myPosition.getColumn();

            while (true) {
                row += dir[0];
                col += dir[1];

                if (isInBounds(row, col)){
                    break;
                }

                ChessPosition newPos = new ChessPosition(row, col);
                ChessPiece occupyingPiece = board.getPiece(newPos);

                if (occupyingPiece == null) {
                    moves.add(new ChessMove(myPosition, newPos, null));
                } else {
                    if (occupyingPiece.getTeamColor() != this.getTeamColor()) {
                        moves.add(new ChessMove(myPosition, newPos, null));
                    }
                    break;
                }
            }
        }
        return moves;
    }

    public HashSet<ChessMove> knight(ChessBoard board, ChessPosition myPosition) {
        var moves = new HashSet<ChessMove>();

        int[][] directions = {
                {2, 1},
                {2, -1},
                {-2, 1},
                {-2, -1},
                {-1, 2},
                {-1, -2},
                {1, 2},
                {1, -2}
        };

        for (int[] dir : directions) {
            int row = myPosition.getRow();
            int col = myPosition.getColumn();

                row += dir[0];
                col += dir[1];

                if (isInBounds(row, col)) {
                    continue;
                }

                ChessPosition newPos = new ChessPosition(row, col);
                ChessPiece occupyingPiece = board.getPiece(newPos);

                if (occupyingPiece == null) {
                    moves.add(new ChessMove(myPosition, newPos, null));
                } else {
                    if (occupyingPiece.getTeamColor() != this.getTeamColor()) {
                        moves.add(new ChessMove(myPosition, newPos, null));
                    }
                }

        }
        return moves;
    }

    public HashSet<ChessMove> rook(ChessBoard board, ChessPosition myPosition) {
        var moves = new HashSet<ChessMove>();

        int[][] directions = {
                {1, 0},
                {0, -1},
                {-1, 0},
                {0, 1}
        };

        for (int[] dir : directions) {
            int row = myPosition.getRow();
            int col = myPosition.getColumn();

            while (true) {
                row += dir[0];
                col += dir[1];

                if (isInBounds(row, col)) {
                    break;
                }

                ChessPosition newPos = new ChessPosition(row, col);
                ChessPiece occupyingPiece = board.getPiece(newPos);

                if (occupyingPiece == null) {
                    moves.add(new ChessMove(myPosition, newPos, null));
                } else {
                    if (occupyingPiece.getTeamColor() != this.getTeamColor()) {
                        moves.add(new ChessMove(myPosition, newPos, null));
                    }
                    break;
                }
            }
        }
        return moves;
    }
    public HashSet<ChessMove> queen(ChessBoard board, ChessPosition myPosition){
        var moves = new HashSet<ChessMove>();
        var allMoves = new HashSet<ChessMove>();

        moves = bishop(board, myPosition);
        allMoves = rook(board, myPosition);

        moves.addAll(allMoves);
        return moves;
    }

    public HashSet<ChessMove> king(ChessBoard board, ChessPosition myPosition) {
        var moves = new HashSet<ChessMove>();

        int[][] directions = {
                {1, 0},
                {0, -1},
                {-1, 0},
                {0, 1},
                {1, 1},
                {1, -1},
                {-1, 1},
                {-1, -1}
        };

        for (int[] dir : directions) {
            int row = myPosition.getRow();
            int col = myPosition.getColumn();

            row += dir[0];
            col += dir[1];

            if (isInBounds(row, col)) {
                break;
            }

            ChessPosition newPos = new ChessPosition(row, col);
            ChessPiece occupyingPiece = board.getPiece(newPos);

            if (occupyingPiece == null) {
                moves.add(new ChessMove(myPosition, newPos, null));
            } else {
                if (occupyingPiece.getTeamColor() != this.getTeamColor()) {
                    moves.add(new ChessMove(myPosition, newPos, null));
                }
            }
        }
        return moves;
    }

    public HashSet<ChessMove> pawn(ChessBoard board, ChessPosition myPosition) {
        var moves = new HashSet<ChessMove>();
        int row = myPosition.getRow();
        int col = myPosition.getColumn();

        int dir = (this.pieceColor == ChessGame.TeamColor.WHITE) ? 1 : -1;

        int newRow = row + dir;
        if (!isInBounds(newRow, col)) {
            ChessPosition forwardPos = new ChessPosition(newRow, col);
            if (board.getPiece(forwardPos) == null) {
                addPawnMove(moves, myPosition, forwardPos);

                if ((this.pieceColor == ChessGame.TeamColor.WHITE && row == 2) ||
                        (this.pieceColor == ChessGame.TeamColor.BLACK && row == 7)) {
                    int twoRow = row + 2 * dir;
                    ChessPosition twoForward = new ChessPosition(twoRow, col);
                    if (board.getPiece(twoForward) == null) {
                        addPawnMove(moves, myPosition, twoForward);
                    }
                }
            }
        }

        int[][] captureDirs = {{dir, -1}, {dir, 1}};
        for (int[] cDir : captureDirs) {
            int cRow = row + cDir[0];
            int cCol = col + cDir[1];
            if (!isInBounds(cRow, cCol)) {
                ChessPosition capturePos = new ChessPosition(cRow, cCol);
                ChessPiece target = board.getPiece(capturePos);
                if (target != null && target.getTeamColor() != this.getTeamColor()) {
                    addPawnMove(moves, myPosition, capturePos);
                }
            }
        }

        return moves;
    }

    // Helper for promotion handling
    private void addPawnMove(HashSet<ChessMove> moves, ChessPosition from, ChessPosition to) {
        int promotionRow = (this.pieceColor == ChessGame.TeamColor.WHITE) ? 8 : 1;
        if (to.getRow() == promotionRow) {
            moves.add(new ChessMove(from, to, PieceType.QUEEN));
            moves.add(new ChessMove(from, to, PieceType.ROOK));
            moves.add(new ChessMove(from, to, PieceType.BISHOP));
            moves.add(new ChessMove(from, to, PieceType.KNIGHT));
        } else {
            moves.add(new ChessMove(from, to, null));
        }
    }


    private boolean isInBounds(int row, int col) {
        return row < 1 || row > 8 || col < 1 || col > 8;
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }
}
