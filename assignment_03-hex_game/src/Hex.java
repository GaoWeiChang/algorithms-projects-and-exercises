/* The Hex game
   https://en.wikipedia.org/wiki/Hex_(board_game)
   desigened by Jean-Christophe Filliâtre

   grid size : n*n

   playable cells : (i,j) with 1 <= i, j <= n

   blue edges (left and right) : i=0 or i=n+1, 1 <= j <= n
    red edges (top and bottom) : 1 <= i <= n, j=0 or j=n+1

      note: the four corners have no color

   adjacence :      i,j-1   i+1,j-1

                 i-1,j    i,j   i+1,j

                    i-1,j+1    i,j+1

*/

class DisjointSet {
  public int[] parent;
  public int[] rank;

  public DisjointSet(int n){
    parent = new int[n];
    rank = new int[n];
  }

  public int find(int x){
    if(parent[x] != x){
      parent[x] = find(parent[x]);
    }
    return parent[x];
  }

  public void union(int x, int y){
    int x_root = find(x);
    int y_root = find(y);
    if(x_root == y_root){
      return;
    }

    if(rank[x_root] > rank[y_root]){
      parent[y_root] = x_root;
    }
    else if(rank[x_root] < rank[y_root]){
      parent[x_root] = y_root;
    }
    else{
      parent[y_root] = x_root;
      rank[x_root]+=1;
    }
  }
}

public class Hex {

  enum Player {
    NOONE, BLUE, RED
  }

  int n; // size of the board
  Player[][] board; 
  Player currPlayer; // current player
  DisjointSet _ds;

  // create an empty board of size n*n
  Hex(int n) {
    this.n = n;
    board = new Player[n+2][n+2];
    currPlayer = Player.RED; // set player RED for first turn
    _ds = new DisjointSet((n+2)*(n+2));

    // initialize the board
    for (int i=0; i<=n+1; i++){
      for(int j=0; j<=n+1; j++){
        _ds.parent[getBoardIdx(i, j)]=getBoardIdx(i, j);
        _ds.rank[getBoardIdx(i, j)]=0;
        board[i][j] = Player.NOONE;
      }
    }

    for(int i=1;i<=n;i++){
      _ds.union(getBoardIdx(i, 0), getBoardIdx(1, 0));
      board[i][0] = Player.RED;
      _ds.union(getBoardIdx(i, n + 1), getBoardIdx(1, n + 1));
      board[i][n+1] = Player.RED;

      _ds.union(getBoardIdx(0, i), getBoardIdx(0, 1));
      board[0][i] = Player.BLUE;
      _ds.union(getBoardIdx(n+1, i), getBoardIdx(n+1, 1));
      board[n+1][i] = Player.BLUE;
    }
  }
  
  // return the color of cell i,j
  Player get(int i, int j) {
    return board[i][j];
  }
  
  int getBoardIdx(int i, int j){
    return i + (board.length) * j; // converts the 2D coordinates (i,j) to a 1D index
  }

  // update the board after the player with the trait plays the cell (i, j).
  // Does nothing if the move is illegal.
  // Returns true if and only if the move is legal.
  boolean click(int i, int j) {
    if((board[i][j]!=Player.NOONE) || (winner()!=Player.NOONE)){
      return false;
    }

    board[i][j] = currPlayer;
    connectToNeighbour(i, j);

    // switch turn
    if(currPlayer == Player.RED){
      currPlayer = Player.BLUE;
    }else{
      currPlayer = Player.RED;
    }

    return true;
  }

  void connectToNeighbour(int i, int j){
    int currIdx = getBoardIdx(i, j);
    int[][] directions = {
      {0, -1},  // up
      {1, -1},  // up-right
      {-1, 0},  // left
      {1, 0},   // right
      {-1, 1},  // down-left
      {0, 1}    // down
    };
    
    for(int[] dir : directions){
      int _i = i + dir[0];
      int _j = j + dir[1];
      
      if(board[_i][_j] == currentPlayer()){
        int neighborIdx = getBoardIdx(_i, _j);
        _ds.union(currIdx, neighborIdx);
      }
    }  
  }

  // return the player with the trait or Player.NOONE if the game is over
  // because of a player's victory.
  Player currentPlayer() {
    return currPlayer;
  }


  // return the winning player, or Player.NOONE if no player has won yet
  Player winner() {
    if(_ds.find(getBoardIdx(0,1)) == _ds.find(getBoardIdx(board.length-1, 1))){
      return Player.BLUE;
    }
    else if(_ds.find(getBoardIdx(1,0)) == _ds.find(getBoardIdx(1, board.length-1))){
      return Player.RED;
    }

    return Player.NOONE;
  }

  int label(int i, int j) {
    int idx = getBoardIdx(i, j);
    return _ds.find(idx);
  }

  public static void main(String[] args) {
    HexGUI.createAndShowGUI();
  }
}
