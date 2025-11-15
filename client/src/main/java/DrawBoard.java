public class DrawBoard {

    private static final String RESET = "\u001B[0m";
    private static final String LIGHT_BG = "\u001B[47m";   // light square
    private static final String DARK_BG = "\u001B[100m";   // dark square
    private static final String WHITE_PIECE_COLOR = "\u001B[31m"; // red
    private static final String BLACK_PIECE_COLOR = "\u001B[34m"; // blue

    private static final int SQUARE_WIDTH = 5;

    public static void printBoard(boolean perspectiveWhite) {
        String[][] board = getDefaultBoard();

        int[] rows = perspectiveWhite ? new int[]{8,7,6,5,4,3,2,1} : new int[]{1,2,3,4,5,6,7,8};
        char[] cols = perspectiveWhite ? new char[]{'a','b','c','d','e','f','g','h'} : new char[]{'h','g','f','e','d','c','b','a'};


        System.out.print("   ");
        for (char c : cols){ System.out.print(centerText(String.valueOf(c), SQUARE_WIDTH));}
        System.out.println();


        for (int rIndex = 0; rIndex < 8; rIndex++) {
            int r = rows[rIndex];
            System.out.print(r + " "); // row number

            for (int cIndex = 0; cIndex < 8; cIndex++) {
                int c = perspectiveWhite ? cIndex : 7 - cIndex;
                String cell = board[r-1][c];

                boolean lightSquare = ((r-1) + c) % 2 == 0;
                String bg = lightSquare ? LIGHT_BG : DARK_BG;

                String symbol = cell.equals(" ") ? " " : cell; // just letters now
                String color = getPieceColor(cell);

                String squareContent = centerText(symbol, SQUARE_WIDTH);

                // Print square with piece color + background
                System.out.print(bg + color + squareContent + RESET);
            }

            System.out.println(" " + r);
        }

        // Bottom column labels
        System.out.print("   ");
        for (char c : cols) {System.out.print(centerText(String.valueOf(c), SQUARE_WIDTH));}
        System.out.println();
    }

    // Determine piece color
    private static String getPieceColor(String piece) {
        if (piece.equals(" ") ){ return "";} // empty square has no color
        if (piece.equals(piece.toUpperCase())) {return WHITE_PIECE_COLOR;} // white piece
        return BLACK_PIECE_COLOR; // black piece
    }

    private static String centerText(String text, int width) {
        int padding = width - text.length();
        int padLeft = padding / 2;
        int padRight = padding - padLeft;
        return " ".repeat(padLeft) + text + " ".repeat(padRight);
    }

    // Default starting board with letters
    private static String[][] getDefaultBoard() {
        String[][] board = new String[8][8];

        board[0] = new String[]{"r","n","b","q","k","b","n","r"};
        board[1] = new String[]{"p","p","p","p","p","p","p","p"};

        for (int r = 2; r <= 5; r++) {
            for (int c = 0; c < 8; c++) {
                board[r][c] = " ";
            }
        }

        board[6] = new String[]{"P","P","P","P","P","P","P","P"};
        board[7] = new String[]{"R","N","B","Q","K","B","N","R"};

        return board;
    }

    public static void main(String[] args) {
        System.out.println("White perspective:");
        printBoard(true);

        System.out.println("\nBlack perspective:");
        printBoard(false);
    }
}
