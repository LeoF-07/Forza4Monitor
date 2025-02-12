public class Player extends Thread {

    private PlayerID id;
    private GameBoard gameBoard;

    public Player(PlayerID id, GameBoard gameBoard){
        this.id = id;
        this.gameBoard = gameBoard;
    }

    @Override
    public void run() {
        PlayerID vincitore;
        do{
            vincitore = gameBoard.mossa(this.id);
        }while(vincitore == PlayerID.NONE && !gameBoard.fullBoard());
    }

}
