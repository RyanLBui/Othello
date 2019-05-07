/*
    Ryan Bui
    CS 2336.501
*/

/*
    How to run:
    1.) Run Project
    2.) User (Black) will go first, possible move locations will have a *.
    3.) To enter move: Type Column first (A,B,C,D) then row (1,2,3,4).
        ex. B1 or C4
    4.) After you select your move, the computer will make its move.
    5.) Continue with until game is finished.
*/

import java.util.HashSet;
import java.util.Scanner;  
import java.util.Set;
import java.util.Random;

class Board
{ 
    public class Point
    {
        int x, y;
        Point(int x, int y)
        {
            this.x = x;
            this.y = y;
        }
        
        @Override
        public String toString()
        {
            return "["+x+", "+y+"]";
        }
        
        @Override
        public boolean equals(Object o)
        {
            return o.hashCode()==this.hashCode();
        }
        
        @Override
        public int hashCode()
        {
            return Integer.parseInt(x+""+y);
        }
    }
    
    private final char[][] board;
    int whiteScore, blackScore, remaining;
    private final char boardX[] = new char[]{'A','B','C','D'};
    
    // Default 4x4 board.
    public Board(){ 
        board = new char[][]
        {
            {'_','_','_','_',},
            {'_','W','B','_',},
            {'_','B','W','_',},
            {'_','_','_','_',},
        };  
    }
    
    // Displays board to console.
    public void displayBoard(Board b)
    {  
        System.out.print("\n  ");
        
        for(int i=0;i<4;++i)
        {
            System.out.print(boardX[i]+" "); // Display column letters (A B C D)
        } 
        System.out.println();
        for(int i=0;i<4;++i)
        {
            System.out.print((i+1)+" "); // Displays Row numbers (1 2 3 4)
            for(int j=0;j<4;++j)
            {
                 System.out.print(b.board[i][j]+" ");
            }
            System.out.println();
        }
        System.out.println(); 
    }
    
    // Used to find all the placeable locations and puts into hashset.
    private void findPlaceableLocations(char player, char opponent, HashSet<Point> placeablePositions){ 
        for(int i=0;i<4;++i)
        {
            for(int j=0;j<4;++j)
            {
                if(board[i][j] == opponent)
                {
                    int I = i, J = j;  
                    if(i-1>=0 && j-1>=0 && board[i-1][j-1] == '_')
                    { 
                        i = i+1; j = j+1;
                        while(i<3 && j<3 && board[i][j] == opponent)
                        {
                            i++;j++;
                        }
                        if(i<=3 && j<=3 && board[i][j] == player) 
                        {
                            placeablePositions.add(new Point(I-1, J-1));
                        }
                    } 
                    i=I;j=J;
                    if(i-1>=0 && board[i-1][j] == '_')
                    {
                        i = i+1;
                        while(i<3 && board[i][j] == opponent) i++;
                        if(i<=3 && board[i][j] == player)
                        {
                            placeablePositions.add(new Point(I-1, J));
                        }
                    } 
                    i=I;
                    if(i-1>=0 && j+1<=3 && board[i-1][j+1] == '_')
                    {
                        i = i+1; j = j-1;
                        while(i<3 && j>0 && board[i][j] == opponent){i++;j--;}
                        if(i<=3 && j>=0 && board[i][j] == player)
                        {
                            placeablePositions.add(new Point(I-1, J+1));
                        }
                    }  
                    i=I;j=J;
                    if(j-1>=0 && board[i][j-1] == '_')
                    {
                        j = j+1;
                        while(j<3 && board[i][j] == opponent)j++;
                        if(j<=3 && board[i][j] == player)
                        {
                            placeablePositions.add(new Point(I, J-1));
                        }
                    }
                    j=J;
                    if(j+1<=3 && board[i][j+1] == '_')
                    {
                        j=j-1;
                        while(j>0 && board[i][j] == opponent)j--;
                        if(j>=0 && board[i][j] == player)
                        {
                            placeablePositions.add(new Point(I, J+1));
                        }
                    }
                    j=J;
                    if(i+1<=3 && j-1>=0 && board[i+1][j-1] == '_')
                    {
                        i=i-1;j=j+1;
                        while(i>0 && j<3 && board[i][j] == opponent){i--;j++;}
                        if(i>=0 && j<=3 && board[i][j] == player) 
                        {
                            placeablePositions.add(new Point(I+1, J-1));
                        }
                    }
                    i=I;j=J;
                    if(i+1 <= 3 && board[i+1][j] == '_')
                    {
                        i=i-1;
                        while(i>0 && board[i][j] == opponent) i--;
                        if(i>=0 && board[i][j] == player) 
                        {
                            placeablePositions.add(new Point(I+1, J));
                        }
                    }
                    i=I;
                    if(i+1 <= 3 && j+1 <= 3  && board[i+1][j+1] == '_')
                    {
                        i=i-1;j=j-1;
                        while(i>0 && j>0 && board[i][j] == opponent){i--;j--;}
                        if(i>=0 && j>=0 && board[i][j] == player)
                        {
                            placeablePositions.add(new Point(I+1, J+1));
                        }
                    }
                    i=I;j=J;
                    }
                } 
        } 
    } 
    
    // Used to get placeable locations from hashset for white and black.
    public HashSet<Point> getPlaceableLocations(char player, char opponent)
    { 
        HashSet<Point> placeablePositions = new HashSet<>();
        findPlaceableLocations(player, opponent, placeablePositions);
        return placeablePositions;
    }
     
    // This displays the grid. Possible locations will have a *.
    public void showPlaceableLocations(HashSet<Point> locations, char player, char opponent)
    {
        for(Point p:locations)
        {
            board[p.x][p.y]='*';
        }
        
        displayBoard(this);
        
        for(Point p:locations)
        {
            board[p.x][p.y]='_';
        }   
    }
    
    //Used to place move on the board for both white and black.
    public void placeMove(Point p, char player, char opponent){
        int i = p.x, j = p.y;
        board[i][j] = player; 
        int I = i, J = j;  
        
        if(i-1>=0 && j-1>=0 && board[i-1][j-1] == opponent)
        { 
            i = i-1; j = j-1;
            while(i>0 && j>0 && board[i][j] == opponent)
            {
                i--;j--;
            }
            if(i>=0 && j>=0 && board[i][j] == player) 
            {
                while(i!=I-1 && j!=J-1)
                board[++i][++j]=player;
            }
        } 
        i=I;j=J; 
        if(i-1>=0 && board[i-1][j] == opponent)
        {
            i = i-1;
            while(i>0 && board[i][j] == opponent) i--;
            if(i>=0 && board[i][j] == player)
            {
                while(i!=I-1)
                board[++i][j]=player;
            }
        } 
        i=I; 
        if(i-1>=0 && j+1<=3 && board[i-1][j+1] == opponent)
        {
            i = i-1; j = j+1;
            while(i>0 && j<3 && board[i][j] == opponent)
            {
                i--;j++;
            }
            if(i>=0 && j<=3 && board[i][j] == player)
            {
                while(i!=I-1 && j!=J+1)board[++i][--j] = player;
            }
        }   
        i=I;j=J;
        if(j-1>=0 && board[i][j-1] == opponent)
        {
            j = j-1;
            while(j>0 && board[i][j] == opponent)
                j--;
            if(j>=0 && board[i][j] == player) 
            {
                while(j!=J-1)
                    board[i][++j] = player;
            }
        }
        j=J; 
        if(j+1<=3 && board[i][j+1] == opponent)
        {
            j=j+1;
            while(j<3 && board[i][j] == opponent)j++;
            if(j<=3 && board[i][j] == player) 
            {
                while(j!=J+1)board[i][--j] = player;
            }
        }
        j=J; 
        if(i+1<=3 && j-1>=0 && board[i+1][j-1] == opponent)
        { 
            i=i+1;j=j-1;
            while(i<3 && j>0 && board[i][j] == opponent)
            {
                i++;j--;
            }
            if(i<=3 && j>=0 && board[i][j] == player) 
            {
                while(i!=I+1 && j!=J-1)board[--i][++j] = player;
            }
        }
        i=I;j=J; 
        if(i+1 <= 3 && board[i+1][j] == opponent)
        { 
            i=i+1;
            while(i<3 && board[i][j] == opponent) 
                i++;
            if(i<=3 && board[i][j] == player)
            {
                while(i!=I+1)board[--i][j] = player;
            }
        }
        i=I;

        if(i+1 <= 3 && j+1 <=3 && board[i+1][j+1] == opponent)
        {
            i=i+1;j=j+1;
            while(i<3 && j<3 && board[i][j] == opponent)
            {
                i++;j++;
            }
            if(i<=3 && j<=3 && board[i][j] == player)
            {
                while(i!=I+1 && j!=J+1)
                {
                    board[--i][--j] = player;
                }        
            }
        }
    }  
    
    // Goes through 2d array and counts the scores.
    public void updateScores(){
        whiteScore = 0; blackScore = 0; remaining = 0;
        for(int i=0;i<4;++i)
        {
            for(int j=0;j<4;++j)
            {
                switch (board[i][j]) {
                    case 'W':
                        whiteScore++;
                        break;
                    case 'B':
                        blackScore++;
                        break;
                    default:
                        remaining++;
                        break;
                }
            }
        }
    }
    
    // Used to update scores, and checks if game is over.
    public int gameResult(Set<Point> whitePlaceableLocations, Set<Point> blackPlaceableLocations)
    { 
        updateScores();
        if(remaining == 0)
        {
            if(whiteScore > blackScore) return 1;
            else if(blackScore > whiteScore) return -1;
            else return 0; //Draw
        }
        if(whiteScore==0 || blackScore == 0)
        {
            if(whiteScore > 0) return 1;
            else if(blackScore > 0) return -1; 
        }  
        if(whitePlaceableLocations.isEmpty() && blackPlaceableLocations.isEmpty())
        {
            if(whiteScore > blackScore) return 1;
            else if(blackScore > whiteScore) return -1;
            else return 0; //Draw
        } 
        return -2;
    } 
    
    // Checks to see if char entered is valid (A, B, C, D)
    public int coordinateX(char x)
    {
        for(int i=0;i<4;++i)
        {
            if(boardX[i]==Character.toLowerCase(x)||boardX[i]==Character.toUpperCase(x))
            {
                return i;
            }
        }
        return -1; // Illegal move received
    }
} 



public class Reversi 
{
    public static void onePlayer(Board b)
    {  
        Scanner scan = new Scanner(System.in);
        Board.Point move = b.new Point(-1, -1); 
        
        int result;
        Boolean skip;
        String input;
        
        System.out.println("Enter valid move: Letter first, then number.");
        System.out.println("Black Moves first"); 
        
        OUTER: 
        OUTER_1:
        while (true) {
            skip = false;
            
            // Get placeable locations
            HashSet<Board.Point> blackPlaceableLocations = b.getPlaceableLocations('B', 'W');
            HashSet<Board.Point> whitePlaceableLocations = b.getPlaceableLocations('W', 'B');
            
            // Display placeable locations with a *.
            b.showPlaceableLocations(blackPlaceableLocations, 'B', 'W');
            
            // Checks result of game to see if game is over or not.
            result = b.gameResult(whitePlaceableLocations, blackPlaceableLocations);
            switch (result) 
            {
                case 0:
                    System.out.println("It is a draw.");
                    break OUTER;
                case 1:
                    System.out.println("Game over. White wins: "+b.whiteScore+":"+b.blackScore);
                    break OUTER;
                case -1: 
                    System.out.println("Game over. Black wins: "+b.blackScore+":"+b.whiteScore);
                    break OUTER;
                default:
                    break;
            }
            
            if(blackPlaceableLocations.isEmpty())
            {
                System.out.println("No possible moves for black, Passing turn to white");
                skip = true;
            }
            
            if(!skip){
                System.out.println("Place move (Black): "); // Get the input from user.
                input = scan.next();
                move.y = b.coordinateX(input.charAt(0));
                move.x = (Integer.parseInt(input.charAt(1)+"")-1); 
                
                while(!blackPlaceableLocations.contains(move)) // if not valid move, ask for input again.
                {
                    System.out.println("Invalid move!\n\nPlace move (Black): ");
                    input = scan.next();
                    move.y = b.coordinateX(input.charAt(0));
                    move.x = Integer.parseInt((input.charAt(1)+""))-1;  
                }
                b.placeMove(move, 'B', 'W'); // This makes the move.
                b.updateScores(); // Updates score
                System.out.println("\nBlack Score: "+b.blackScore+" White Score: "+b.whiteScore); // Displays score.
            }
            
            skip = false; // set skip to false so can get white move.
            
            // Get placeable locations again.
            whitePlaceableLocations = b.getPlaceableLocations('W', 'B');
            blackPlaceableLocations = b.getPlaceableLocations('B', 'W');
            
            // Displays grid with placeable locations with a *.
            b.showPlaceableLocations(whitePlaceableLocations, 'W', 'B');
            
            // Checks to see if the game is over, if not will continue
            result = b.gameResult(whitePlaceableLocations, blackPlaceableLocations);
            switch (result) {
                case 0:
                    System.out.println("It is a draw.");
                    break OUTER_1;
                case 1:
                    System.out.println("White wins: "+b.whiteScore+":"+b.blackScore);
                    break OUTER_1;
                case -1:
                    System.out.println("Black wins: "+b.blackScore+":"+b.whiteScore);
                    break OUTER_1;
                default:
                    break;
            }
            
            if(whitePlaceableLocations.isEmpty())
            {
                System.out.println("No possible moves for white, Passing turn to Black");
                skip = true; 
            }
            
            if(!skip)
            {
                System.out.println("Place move White ");
                Random rand = new Random();
                char y = '0';
                int x =0;
                
                // Generates random move for the computer.
                if (!whitePlaceableLocations.isEmpty())
                {
                    while(!whitePlaceableLocations.contains(move))
                    {
                        y = (char)(rand.nextInt(4)+ 'a');
                        x = rand.nextInt(4) + 0;
                        move.y = b.coordinateX(y);
                        move.x = x;
                    }
                }
                
                b.placeMove(move, 'W', 'B');
                b.updateScores();
                System.out.println("Sucess: White move at: "+ "( "+ y + ", "+ (x+1) + " )");
                System.out.println( "\nWhite Score: "+b.whiteScore+ " Black Score: "+b.blackScore);
            }
        }
    }
        
    public static void main(String[] args){
        Board b = new Board(); 
        onePlayer(b); 
    }
}
