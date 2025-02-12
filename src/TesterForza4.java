public class TesterForza4 {

    public static void main(String[] args) {
        System.out.println("Forza4\n\n");

        GameBoard gameBoard = new GameBoard();

        Player player1 = new Player(PlayerID.X, gameBoard);
        Player player2 = new Player(PlayerID.O, gameBoard);

        player1.start();
        player2.start();

        try {
            player1.join();
            player2.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        PlayerID vincitore = gameBoard.checkWinner();
        if(vincitore == PlayerID.NONE) System.out.println("Pareggio, tabella piena");
        else System.out.println("Vincitore: " + vincitore);
    }

}