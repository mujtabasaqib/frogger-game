package com.frogger;
import java.util.Scanner;
public class Frogger {

    Frog frog;
    Vehicle[][] vehicles;
    Log[][] logs;
    Animal[][] animals;
    char[][] board;
    int x, y, indx, indy, prevFrogY, score, highscore, lives, frogsHome,seconds;
    long startTime, endTime, diff, elapsedSecs;
    boolean collision, collision1, drowned, home;

    //Use this constructor in main to initialize or set up the game
    public Frogger() {

        //Frog
        frog = new Frog(9, 12, 1, 1, "/game/frogg.png");

        //Vehicles
        //x and y values are given according to game board
        //multiple vehicles with some vehicles start from right some from left hence the difference in x
        vehicles = new Vehicle[][]{
                {new Vehicle(0, 11, 1, 1, "/game/truck.png"), new Vehicle(4, 11, 1, 1, "/game/truck.png"), new Vehicle(8, 11, 1, 1,"/game/truck.png")},
                {new Vehicle(18, 10, 1, 1,"/game/truck.png"), new Vehicle(14, 10, 1, 1,"/game/truck.png")},
                {new Vehicle(0, 9, 1, 1,"/game/truck.png"), new Vehicle(5, 9, 1, 1,"/game/truck.png")},
                {new Vehicle(18, 8, 1, 1,"/game/truck.png"), new Vehicle(15, 8, 1, 1,"/game/truck.png"), new Vehicle(12, 8, 1, 1,"/game/truck.png")},
                {new Vehicle(0, 7, 1, 1,"/game/truck.png"), new Vehicle(4, 7, 1, 1,"/game/truck.png")}
        };

        //Logs similar to vehicles
        logs = new Log[][]{
                {new Log(0, 5, 1, 1,"/game/log.png"), new Log(4, 5, 1, 1,"/game/log.png"), new Log(8, 5, 1, 1,"/game/log.png")},
                {new Log(0, 3, 1, 1,"/game/log.png"), new Log(4, 3, 1, 1,"/game/log.png"), new Log(8, 3, 1, 1,"/game/log.png")},
                {new Log(0, 1, 1, 1,"/game/log.png"), new Log(4, 1, 1, 1,"/game/log.png"), new Log(8, 1, 1, 1,"/game/log.png")}
        };

        //Animals similar to above two
        animals = new Animal[][]{
                {new Animal(17, 4, 1, 1, "/game/animal.png"), new Animal(13, 4, 1, 1,"/game/turtle.jpeg"), new Animal(9, 4, 1, 1,"/game/animal.png"), new Animal(5, 4, 1, 1,"/game/turtle.jpeg")},
                {new Animal(17, 2, 1, 1,"/game/turtle.jpeg"), new Animal(13, 2, 1, 1,"/game/animal.png"), new Animal(9, 2, 1, 1,"/game/turtle.jpeg"), new Animal(5, 2, 1, 1,"/game/animal.png")}
        };

        //Game board
        board = new char[13][19];

        //Keeps track of score
        score = 0;
        highscore = 0;

        //Initial lives for player
        lives = 4;

        //Number of frogs which have reached home (the end of board)
        frogsHome = 0;

        //Starting time
        seconds = 60;

        //assigning frog to board
        assignFrog();

        //assigning strips to board
        assignStrips();

        //assigning vehicles, logs and animals on board
        assignObstacles(vehicles);
        assignObstacles(logs);
        assignObstacles(animals);

        //prompt and starting game
        displayPrompt();

        //if user exits in the beginning
        if(!startGame()){
            System.exit(0);
        }

        //Initial game after set up
        displayGame();

    }

    public void assignFrog(){
        x = frog.x;
        y = frog.y;

        board[y][x] = 'F';
    }

    public void assignObstacles(GameElement[][] obj){

        if(obj instanceof Log[][]) {
            //assigning logs to board
            for (Log[] log : logs) {
                for (Log l : log) {
                    indx = l.x;
                    indy = l.y;

                    board[indy][indx] = 'L';
                }
            }
        }

        else if(obj instanceof Animal[][]){
            //assigning animals to board
            for (Animal[] animal : animals) {
                for (Animal a : animal) {
                    indx = a.x;
                    indy = a.y;

                    board[indy][indx] = 'A';
                }
            }

        }

        else if(obj instanceof Vehicle[][]){
            //assigning vehicles to board
            for (Vehicle[] vehicle : vehicles) {
                for (Vehicle v : vehicle) {
                    indx = v.x;
                    indy = v.y;

                    board[indy][indx] = 'V';
                }
            }
        }

    }

    public void assignStrips(){

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < 19; j++) {
                if (i == 6 || i == 0) {
                    //frog placement, there has to be five places in which to place the frog
                    if (i == 0 && (j == 2 || j == 5 || j == 9 || j == 13 || j == 16)) {
                        board[i][j] = '-';
                    }
                    //strips for rest
                    else {
                        board[i][j] = 'S';
                    }
                }
            }
        }

    }

    public void displayGame(){
        System.out.println("Score: "+score);
        displayBoard(board);
        System.out.println("Time: "+seconds);
        displayLives(lives);
    }

    public void displayBoard(char[][] board) {

        for (char[] row : board) {
            for (char cell : row) {
                System.out.print(cell == 0 ? '.' : cell);
            }
            System.out.println();
        }

    }

    public void displayLives(int lives){
        for(int i=0; i<lives; i++){
            System.out.print('♡');
        }
        System.out.println();
    }

    public void moveObstacle(Vehicle[][] vehicles) {
        //vehicle movement

        for (Vehicle[] vehicle : vehicles) {
            for (Vehicle v : vehicle) {

                //right movement
                if (v.y == 11 || v.y == 9 || v.y == 7) {
                    if (v.x == 18) {
                        board[v.y][v.x] = '.'; // setting the prev vehicle as it goes out of array to empty cell
                        v.x = 0;  //reset to initial starting point if it goes out of bounds
                    } else {
                        //set previous 'V' to empty
                        board[v.y][v.x] = '.';
                        //move the vehicle right if its y is 11 or 9 or 7
                        v.x++;
                    }
                }

                //left movement
                if (v.y == 10 || v.y == 8) {
                    if (v.x == 0) {
                        board[v.y][v.x] = '.'; // setting the prev vehicle as it goes out of array to empty cell
                        v.x = 18;  //reset to initial starting point if it goes out of bounds
                    } else {
                        //set previous 'V' to empty
                        board[v.y][v.x] = '.';
                        //move the vehicle left if its y is 10 or 8
                        v.x--;
                    }
                }

                indx = v.x;
                indy = v.y;

                board[indy][indx] = 'V';
            }
        }
    }

    public void moveObstacle(Log[][] logs){
        //log movement
        //same logic as vehicle except logs are moving towards right only

        for (Log[] log : logs) {
            for (Log l : log) {

                if (l.y == 1 || l.y == 3 || l.y == 5) {
                    if (l.x == 18) {
                        board[l.y][l.x] = '.'; // setting the prev log as it goes out of array to empty cell
                        l.x = 0;
                    } else {
                        //set previous 'L' to empty
                        board[l.y][l.x] = '.';
                        l.x++;
                    }
                }
                indx = l.x;
                indy = l.y;

                board[indy][indx] = 'L';
            }
        }
    }

    public void moveObstacle(Animal[][] animals) {
        //animal movement
        //same logic as vehicle and log except animals are moving towards left only

        for (Animal[] animal : animals) {
            for (Animal a : animal) {
                if (a.y == 2 || a.y == 4) {
                    if (a.x == 0) {
                        board[a.y][a.x] = '.'; // setting the prev animal as it goes out of array to empty cell
                        a.x = 18;
                    } else {
                        //set previous 'A' to empty
                        board[a.y][a.x] = '.';
                        a.x--;
                    }
                }

                indx = a.x;
                indy = a.y;

                board[indy][indx] = 'A';
            }
        }
    }

    public void resetFrog(){
        seconds = 60;
        frog.x = 9;
        frog.y = 12;
    }

    public void calculateTime(){
        diff = endTime - startTime;
        elapsedSecs = diff / 1000;
        seconds -= elapsedSecs;
    }

    public int countScore(GameElement obj, int y) {

        int score = 0;

        //add 10 points to score for each forward step i.e. top
        if (obj.y < y) {
            score += 10;
        }

        return score;
    }

    public int countScore(GameElement obj, int y, int frogsHome) {

        int score = 0;

        //add 10 points to score for each forward step i.e. top
        if (obj.y < y) {
            score += 10;
        }

        //guiding each frog home gives 50 points
        if(frogsHome>0){
            score += 50;
        }

        //add 1000 points as all 5 frogs reached home so level ended
        if(frogsHome==5){
            score += 1000;
        }

        return score;
    }

    public void displayPrompt(){
        System.out.println(" mmmmmm                                          \n" +
                " #       m mm   mmm    mmmm   mmmm   mmm    m mm \n" +
                " #mmmmm  #\"  \" #\" \"#  #\" \"#  #\" \"#  #\"  #   #\"  \"\n" +
                " #       #     #   #  #   #  #   #  #\"\"\"\"   #    \n" +
                " #       #     \"#m#\"  \"#m\"#  \"#m\"#  \"#mm\"   #    \n" +
                "                       m  #   m  #               \n" +
                "                        \"\"     \"\"                ");
        System.out.println();
        System.out.println("To play: Press 'w' to move the frog up,\nPress 's' to move the frog down,\nPress 'd' to move the frog right,\nPress 'a' to move the frog left,");
        System.out.println("Press 'x' to not make the frog move, \nPress 'z' to match the frog's movement with log and animal after the strip.");
    }

    public boolean startGame(){

        Scanner input = new Scanner(System.in);
        int choice;

        System.out.println("To start: Press 1,\nTo exit: Press 2");
        choice = input.nextInt();

        //start game or end game based on condition
        return choice == 1;

    }

    public void maxScore(){
        if(score > highscore){
            highscore = score;
        }
    }

    public void resetGame(){
        resetFrog();

        //set lives to initial
        lives = 4;

        //set score to initial
        score = 0;

        //set frog placement positions to empty
        board[0][2] = '-';
        board[0][5] = '-';
        board[0][9] = '-';
        board[0][13] = '-';
        board[0][16] = '-';
    }

        }
