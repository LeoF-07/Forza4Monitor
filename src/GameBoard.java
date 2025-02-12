public class GameBoard {

    final int ROWS = 6;
    final int COLS = 7;
    final int DELAY = 500;

    boolean disponibile;
    private PlayerID[][] board;

    public void inizializzaTabella(){
        for(int i = 0; i < ROWS; i++){
            for(int j = 0; j < COLS; j++){
                board[i][j] = PlayerID.NONE;
            }
        }
    }

    public GameBoard(){
        this.disponibile = false;
        this.board = new PlayerID[ROWS][COLS];
        inizializzaTabella();
    }

    public void printBoard(){
        for(int i = 0; i < ROWS; i++){
            for(int j = 0; j < COLS; j++){
                System.out.print(" " + board[i][j].SIMBOLO + " ");
            }
            System.out.println();
        }

        System.out.println();
    }

    public boolean fullBoard(){
        for(int i = 0; i < ROWS; i++){
            for(int j = 0; j < COLS; j++){
                if(board[i][j] == PlayerID.NONE) return false;
            }
        }

        return true;
    }

    public PlayerID controlloOrizzontale(int row, int col){
        try{
            if(board[row][col] == PlayerID.X && board[row][col + 1] == PlayerID.X && board[row][col + 2] == PlayerID.X && board[row][col + 3] == PlayerID.X) return PlayerID.X;
            if(board[row][col] == PlayerID.O && board[row][col + 1] == PlayerID.O && board[row][col + 2] == PlayerID.O && board[row][col + 3] == PlayerID.O) return PlayerID.O;
        }catch(ArrayIndexOutOfBoundsException ex){
            return PlayerID.NONE;
        }

        return PlayerID.NONE;
    }

    public PlayerID controlloVerticale(int row, int col){
        try {
            if (board[row][col] == PlayerID.X && board[row - 1][col] == PlayerID.X && board[row - 2][col] == PlayerID.X && board[row - 3][col] == PlayerID.X) return PlayerID.X;
            if (board[row][col] == PlayerID.O && board[row - 1][col] == PlayerID.O && board[row - 2][col] == PlayerID.O && board[row - 3][col] == PlayerID.O) return PlayerID.O;
        }catch(ArrayIndexOutOfBoundsException ex){
            return PlayerID.NONE;
        }

        return PlayerID.NONE;
    }

    public PlayerID controlloObliquoDestro(int row, int col){
        try {
            if (board[row][col] == PlayerID.X && board[row - 1][col + 1] == PlayerID.X && board[row - 2][col + 2] == PlayerID.X && board[row - 3][col + 3] == PlayerID.X) return PlayerID.X;
            if (board[row][col] == PlayerID.O && board[row - 1][col + 1] == PlayerID.O && board[row - 2][col + 2] == PlayerID.O && board[row - 3][col + 3] == PlayerID.O) return PlayerID.O;
        }catch(ArrayIndexOutOfBoundsException ex){
            return PlayerID.NONE;
        }

        return PlayerID.NONE;
    }

    public PlayerID controlloObliquoSinistro(int row, int col){
        try {
            if (board[row][col] == PlayerID.X && board[row - 1][col - 1] == PlayerID.X && board[row - 2][col - 2] == PlayerID.X && board[row - 3][col - 3] == PlayerID.X) return PlayerID.X;
            if (board[row][col] == PlayerID.O && board[row - 1][col - 1] == PlayerID.O && board[row - 2][col - 2] == PlayerID.O && board[row - 3][col - 3] == PlayerID.O) return PlayerID.O;
        }catch(ArrayIndexOutOfBoundsException ex){
            return PlayerID.NONE;
        }

        return PlayerID.NONE;
    }

    public synchronized PlayerID checkWinner(){
        for(int i = ROWS - 1; i >= 0; i--){
            for(int j = 0; j < COLS; j++){
                PlayerID vincitore;

                vincitore = controlloOrizzontale(i, j);
                if(vincitore != PlayerID.NONE) return vincitore;

                vincitore = controlloVerticale(i, j);
                if(vincitore != PlayerID.NONE) return vincitore;

                vincitore = controlloObliquoDestro(i, j);
                if(vincitore != PlayerID.NONE) return vincitore;

                vincitore = controlloObliquoSinistro(i, j);
                if(vincitore != PlayerID.NONE) return vincitore;
            }
        }

        return PlayerID.NONE;
    }

    public synchronized PlayerID mossa(PlayerID playerID){
        if(playerID == PlayerID.X) {
            while (!disponibile) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        else{
            while (disponibile) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        if(fullBoard()){
            notifyAll();
            return PlayerID.NONE;
        }

        PlayerID idVincitore = checkWinner();
        if(idVincitore != PlayerID.NONE){
            notifyAll();
            return idVincitore;
        }

        int colonna;
        boolean inserimentoRiuscito;

        do{
            colonna = (int) (Math.random() * COLS);
            inserimentoRiuscito = inserisciPedina(playerID, colonna);
        }while(!inserimentoRiuscito);

        printBoard();

        try {
            wait(DELAY);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        if(playerID == PlayerID.O) {
            this.disponibile = true;
            notifyAll();
            return checkWinner();
        }
        else{
            this.disponibile = false;
            notifyAll();
            return checkWinner();
        }
    }

    public boolean inserisciPedina(PlayerID playerID, int colonna){
        for(int i = ROWS - 1; i >= 0; i--){
            if(board[i][colonna] == PlayerID.NONE){
                board[i][colonna] = playerID;
                return true;
            }
        }
        return false;
    }

}
