//build using minmax algorithm by Luiz Paulo

package jogadores;

public class JogadorVingador extends Jogador {
    private int idealBoardSize = 3;
    private int emptyValue = -1;

    public JogadorVingador(String nome) {
        super(nome);
    }

    @Override
    public int[] jogar(int[][] board) {
        int[] move = move(board);
        return move;
    }

    private int[] move(int[][] board) {
        int bestMove = -1;
        int boardSize = board.length;
        int[] move = new int[2];

        for (int line = 0; line < boardSize; line++) { // search for the best move
            for (int colunm = 0; colunm < boardSize; colunm++) {

                if (board[line][colunm] == -1) { // check if position is empty
                    // play on tha position to check the result
                    board[line][colunm] = this.getSimbolo();

                    /*
                     * send the boad, depth, false(false means that it's not my trun to play,
                     * since I already did a move on previous line)
                     */
                    int tryMove = minMax(board, 0, false);
                    //System.out.println(tryMove);
                    // undo last move to preserv the board
                    board[line][colunm] = emptyValue;

                    // test if the last move is the best
                    if (tryMove > bestMove) {
                        move[0] = line;
                        move[1] = colunm;
                        bestMove = tryMove;
                    }
                }

            }

        }
        return move;
    }

    private int minMax(int[][] board, int depth, boolean turn) {
        int score = validateNextMove(board);

        // TO DO check more sizes of the boad to limit the depth of the search
        // Terminal case to the recursion

        // start
        // limits the recursion by number of runs
        if (board.length <= idealBoardSize) {
            // limit the recursion to 8 times
            if (depth >= 8) {
                return score;
            }
        } else {
            // limit the recursion to idealBoardSize times
            if (depth >= 3) {
                return score;
            }
        }

        // terminal cases with win or lose condition(end of the game)
        if (score == -1 || score == 1) {
            return score;
        }
        // end

        // check if there still moves avalible before try another move
        if (!moveAvalible(board)) {
            return 0;
        }

        // check turn
        //
        // my turn
        if (turn) {
            int best = -1;
            int boardSize = board.length;

            for (int line = 0; line < boardSize; line++) {
                for (int colunm = 0; colunm < boardSize; colunm++) {
                    // check if position is empty
                    if (board[line][colunm] == -1) {
                        // play on tha position to check the result
                        board[line][colunm] = this.getSimbolo();

                        // try to find the best value to MAX player (me) and change the turn
                        best = Math.max(best, minMax(board, depth + 1, !turn));

                        // undo last move to preserv the board
                        board[line][colunm] = emptyValue;
                    }
                }
            }
            return best;
        }

        // opposite Turn
        else {
            int best = 1;
            int boardSize = board.length;

            for (int line = 0; line < boardSize; line++) {
                for (int colunm = 0; colunm < boardSize; colunm++) {

                    if (board[line][colunm] == -1) { // check if position is empty
                        // play on tha position to check the result
                        board[line][colunm] = oppositeSymbol();

                        // try to find the worst value to MIN player and change the turn
                        best = Math.min(best, minMax(board, depth + 1, !turn));

                        // undo last move to preserv the board
                        board[line][colunm] = emptyValue;
                    }
                }
            }
            return best;
        }
    }

    private boolean moveAvalible(int[][] board) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (board[i][j] == -1) {
                    return true;
                }
            }
        }

        return false;
    }

    private int validateNextMove(int[][] board) {
        
        int line = 0;
        int colunm = 0;
        int boardSize = board.length; // used to compare with win condition
        int winCondition = 0;
        int loseCondition = 0;
        int oppositeSymbol = oppositeSymbol();

        // check terminal cases
        // start
        //
        // check lose paths(line)
        for (line = 0; line < boardSize; line++) {
            for (colunm = 0; colunm < boardSize; colunm++) {
                if (board[line][colunm] == oppositeSymbol) {
                    loseCondition++;
                }

            }
            if (loseCondition == boardSize) {
                return -1; // Detect as lose path for the next opposite move (line)
            }
            loseCondition = 0;
        }

        // check lose paths (colunm)
        for (colunm = 0; colunm < boardSize; colunm++) {
            for (line = 0; line < boardSize; line++) {
                if (board[line][colunm] == oppositeSymbol) {
                    loseCondition++;
                }

            }
            if (loseCondition == boardSize) {
                return -1; // Detect as lose path for the next opposite move (colunm)
            }
            loseCondition = 0;
        }

        //TO DO: review later
        // check lose paths (left diagonal)
        for (colunm = 0; colunm < (boardSize - (board.length - 1)); colunm++) {
			for (line = 0; line < (board.length - (board.length - 1)); line++) {
				while (board[line][colunm] == oppositeSymbol) {
					line++;
					colunm++;
					loseCondition++;
					if (loseCondition == board.length) {
						return -1;
					}
				}
				loseCondition = 0;
			}
		}
        //TO DO: review later
        // check lose paths (right diagonal)
        for (colunm = boardSize-1; colunm > boardSize - 2; colunm-- ) {
			for (line = 0; line < (board.length - (board.length - 1)); line++) {
				while (board[line][colunm] == oppositeSymbol) {
					line++;
					colunm--;
					loseCondition++;
					if (loseCondition == board.length) {
						return -1;
					}
				}
				loseCondition = 0;
			}
		}
        //
        // check win paths (line)
        for (line = 0; line < boardSize; line++) {
            for (colunm = 0; colunm < boardSize; colunm++) {
                if (board[line][colunm] == this.getSimbolo()) {
                    winCondition++;
                }

            }
            if (winCondition == boardSize) {
                return 1; // Detect as win path for the next move (line)
            }
            winCondition = 0;
        }

        // check win paths (colunm)
        for (colunm = 0; colunm < boardSize; colunm++) {
            for (line = 0; line < boardSize; line++) {
                if (board[line][colunm] == this.getSimbolo()) {
                    winCondition++;
                }

            }
            if (winCondition == boardSize) {
                return 1; // Detect as win path for the next move (colunm)
            }
            winCondition = 0;
        }

        // check win paths (left diagonal)
        for (line = 0; line < boardSize; line++) { // TO DO: replace this line to 'line = 0'
            for (colunm = 0; colunm < boardSize; colunm++) {
                if (board[line][colunm] == this.getSimbolo()) {
                    winCondition++;
                }
                if (line < boardSize) {
                    // go to next line if not last
                    line++;
                }

            }
            if (winCondition == boardSize) {
                return 1; // Detect as win path for the next move (left diagonal)
            }
            winCondition = 0;
        }

        // check win paths (right diagonal)
        for (line = 0; line < boardSize; line++) {
            for (colunm = 2; colunm > boardSize; colunm--) {
                if (board[line][colunm] == this.getSimbolo()) {
                    winCondition++;
                }
                if (line < boardSize) {
                    // go to next line if not last
                    line++;
                }

            }
            if (winCondition == boardSize) {
                return 1; // Detect as win path for the next move (right diagonal)
            }
            winCondition = 0;
        }

        // end

        // if the next move don't result in a win or loss return 0
        return 0;
    }

    private int oppositeSymbol() {
        if (this.getSimbolo() == 1) {
            return 0;
        } else {
            return 1;
        }

    }

}