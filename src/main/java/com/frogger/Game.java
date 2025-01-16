package com.frogger;
import java.util.Scanner;

public class Game {
    public static void main(String[] args) {

        //For moving frog
        Scanner input = new Scanner(System.in);
        String user;

        //Initialize the game
        Frogger frogger = new Frogger();

        Frog frog = frogger.frog;

        //Game loop
        while (true) {

            //start time
            frogger.startTime = System.currentTimeMillis();

            //removing initial position of frog before movement
            frogger.board[frogger.y][frogger.x] = '.';

            //before moving setting prev frog y to current y (each top movement adds 10 to score, used later)
            frogger.prevFrogY = frog.y;

            //user input
            user = input.next();

            //vehicle, log and animal movement
            frogger.moveObstacle(frogger.vehicles);
            frogger.moveObstacle(frogger.logs);
            frogger.moveObstacle(frogger.animals);

            //frog movement
            frog.move(user);

            //end time
            frogger.endTime = System.currentTimeMillis();

            //calculation for time
            frogger.calculateTime();

            //checking collision and drowning and reached home
            frogger.collision = frog.checkCollision(frogger.vehicles);
            frogger.collision1 = frog.checkCollision();
            frogger.home = frog.checkFrogHome(frog);
            frogger.drowned = false;

            //after the strip as the pond begins after the strip
            if (frog.y < 6) {
                frogger.drowned = frog.checkDrowned(frogger.logs, frogger.animals);
            }

            //resetting frog to initial position and decreasing lives by one
            if (frogger.collision || frogger.collision1 || frogger.drowned || frogger.seconds==0){
                frogger.resetFrog();
                frogger.lives--;
            }

            //check if frog is home
            if (frogger.home) {
                //set the placed position on board as F because frog reached home
                frogger.board[0][frog.x] = 'F';

                //no of frogs reached home increase by one
                frogger.frogsHome++;

                //count score
                frogger.score += frogger.countScore(frog, frogger.prevFrogY, frogger.frogsHome);

                //reset frog as needed for levels and lives and time (time reset as frog has reached home)
                frogger.resetFrog();
            }

            else {
                //count score (use method without home as frog did not reach home)
                frogger.score += frogger.countScore(frog, frogger.prevFrogY);
            }

            //updating frog position
            frogger.x = frog.x;
            frogger.y = frog.y;

            //assigning updated frog position to board
            frogger.board[frogger.y][frogger.x] = 'F';

            //if all five frogs are placed
            if(frogger.frogsHome==5){

                System.out.println("Well done!");

                //setting high score equal to score
                frogger.maxScore();

                //play again or user exits after five frogs have reached home
                if(!frogger.startGame()){
                    break;
                }

                //else reset the game
                frogger.resetGame();


            }

            //if lives end
            if(frogger.lives==0){
                System.out.println("You died.");

                //setting high score equal to score
                frogger.maxScore();

                //play again or user exits after dying
                if(!frogger.startGame()){
                    break;
                }

                //else reset the game
                frogger.resetGame();

            }

            //displaying updated board and score
            frogger.displayGame();

        }

        System.out.println("HIGH SCORE: "+frogger.highscore);
        System.out.println();
        System.out.println("Game over.");

    }

}
