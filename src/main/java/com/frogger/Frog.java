package com.frogger;

public class Frog extends GameElement {

    public Frog(int x, int y, int width, int height, String pathToImage) {
        super(x, y, width, height, pathToImage);
    }

    public void move(String key){

        //up
        if(key.equals("w")){
            this.y--;
        }

        //down
        else if(key.equals("s")){
            if(this.y==12){
                return; //frog does not go out of board
            }
            this.y++;
        }

        //left
        else if(key.equals("a")){
            if(this.x==0){
                return; //frog does not go out of board
            }
            this.x--;
        }

        //right
        else if(key.equals("d")){
            if(this.x==18){
                return; //frog does not go out of board
            }
            this.x++;
        }

        //do nothing
        else if(key.equals("x")){
            return;
        }

        //match frog with log and animal
        else if(key.equals("z")){
            matchFrog();
        }

    }

    public boolean checkCollision(Vehicle[][] vehicles){

        for(Vehicle[] vehicle : vehicles) {
            for (Vehicle v : vehicle) {
                if (this.x == v.x && this.y == v.y) {
                    System.out.println("Collision");
                    return true;
                }
            }
        }

        return false;
    }

    //overloading if frog not placed home
    public boolean checkCollision(){

        //if frog is not in one of the defined places the frog has collided
        if(this.y==0 && !(this.x==2 || this.x==5 || this.x==9 || this.x==13 || this.x==16)){
            return true;
        }

        return false;
    }

    public boolean checkDrowned(Log[][] logs, Animal[][] animals){

        //Frog is on log
        for(Log[] log : logs) {
            for (Log l : log) {
                if (this.x == l.x && this.y == l.y) {
                    return false;
                }
            }
        }

        //Frog is on animal
        for(Animal[] animal : animals) {
            for (Animal a : animal) {
                if (this.x == a.x && this.y == a.y) {
                   return false;
                }
            }
        }

        //Frog is in one of the placements
        if(this.y==0 && (this.x==2 || this.x==5 || this.x==9 || this.x==13 || this.x==16)){
            return false;
        }

        System.out.println("Drowned");
        return true;

    }

    public void matchFrog(){
        //match with log as logs are going towards right
        if(this.y==1 || this.y==3 || this.y==5){
            this.x++;
        }

        //match with animal as animals are going towards left
        else if(this.y==2 || this.y==4){
            this.x--;
        }
    }

    public boolean checkFrogHome(GameElement obj) {

        //if frog is on the same coordinates as the five defined places
        if (obj.y == 0 && (obj.x == 2 || obj.x == 5 || obj.x == 9 || obj.x == 13 || obj.x == 16)) {
            System.out.println("Home");
            return true;
        }

        return false;
    }

}
