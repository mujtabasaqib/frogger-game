package com.frogger;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.ArrayList;

public class Interface extends Application {

    private Animal[][] animals;
    private Log[][] logs;
    private Vehicle[][] vehicles;
    private Frog frog;
    private int row;
    private int column;
    private int score = 0;
    private int frogsHome = 0;
    private int lives = 4;
    private double duration = 0.5;
    private double time = 60;
    private boolean died = false;
    private int level = 1;
    private GridPane pane;
    private ImageView frogSprite;
    private ArrayList<ImageView> obstacleSprites;
    private HBox h1;
    private HBox h2;
    private Label scoring;
    private Label tm;
    private Label lvl;
    private Label sc;
    private Label dd;
    private ImageView l1;
    private ImageView l2;
    private ImageView l3;
    private ImageView l4;
    private Scene scn;
    private Scene scn3;
    private Button again;
    private Button quit;
    private Handler gameHandler;
    private Timeline game;
    private KeyFrame keyFrame;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {

        //game board
        setPane(new GridPane());

        //pane for enter screen
        BorderPane start = new BorderPane();

        //sets frog,vehicle,animals,logs
        initializeObjects();

        //image container for obstacle sprites
        setObstacleSprites(new ArrayList<>());

        //frog sprite
        setFrogSprite(new ImageView(getFrog().sprite));
        getFrogSprite().setFitWidth(20);
        getFrogSprite().setFitHeight(20);

        //constraints so the grid does not resize
        setUpGrid(getPane());

        //adding sprites and empty nodes to make 13 cols x 14 rows grid
        for (int m = 0; m < 14; m++) {
            for (int n = 0; n < 13; n++) {
                if (containsAnimalsOrLogs(getAnimals(), getLogs(), n, m, getPane(), getObstacleSprites()) || containsVehicles(getVehicles(), n, m, getPane(), getObstacleSprites())) {
                    continue;
                } else {
                    Label emptyNode = new Label(" ");
                    emptyNode.setPrefHeight(27);
                    emptyNode.setPrefWidth(27);
                    getPane().add(emptyNode, n, m);
                }
            }
        }

        //bg img for board
        Image backgroundImage = new Image("/game/background.jpeg");
        BackgroundSize size = new BackgroundSize(550, 571, true, true, true, false);
        BackgroundImage background = new BackgroundImage(backgroundImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, size);
        getPane().setBackground(new Background(background));

        //bg img for entry pane
        Image backgroundImage1 = new Image("/game/frogger2.jpeg");
        BackgroundSize size1 = new BackgroundSize(475, 400, true, true, true, true);
        BackgroundImage background1 = new BackgroundImage(backgroundImage1, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, size1);
        start.setBackground(new Background(background1));

        Button button = new Button("Click to start");
        start.setCenter(button);

        //specific setting for grid
        getPane().setHgap(4);
        getPane().setVgap(10);

        //pane does not resize in its parent container
        getPane().setPrefSize(516, 504);
        getPane().setMinSize(516, 504);
        getPane().setMaxSize(516, 504);

        //adding sprite to pane
        getPane().add(getFrogSprite(), getFrog().x, getFrog().y);

        //pane.setGridLinesVisible(true);

        //scoring,timer,level on top of screen
        setScoring(new Label("Score: " + getScore()));
        getScoring().setFont(Font.font("Impact", FontWeight.LIGHT, 11));
        setTm(new Label("Timer: " + getTime()));
        getTm().setFont(Font.font("Impact", FontWeight.LIGHT, 11));
        setLvl(new Label("Level: " + getLevel()));
        getLvl().setFont(Font.font("Impact", FontWeight.LIGHT, 11));

        getScoring().setTextFill(Color.DEEPPINK);
        getTm().setTextFill(Color.DEEPPINK);
        getLvl().setTextFill(Color.DEEPPINK);

        //vbox for containing nodes
        VBox root = new VBox();

        //container for score,timer,level,lives
        setH1(new HBox());
        setH2(new HBox());

        //top of pane
        getH1().getChildren().add(getScoring());
        getH1().getChildren().add(getLvl());
        getH1().getChildren().add(getTm());
        getH1().setSpacing(175);

        //images for frog lives
        int hw = 17;

        Image frogLive = new Image("/game/lives.jpeg");
        setL1(new ImageView(frogLive));
        getL1().setFitHeight(hw);
        getL1().setFitWidth(hw);

        setL2(new ImageView(frogLive));
        getL2().setFitHeight(hw);
        getL2().setFitWidth(hw);

        setL3(new ImageView(frogLive));
        getL3().setFitHeight(hw);
        getL3().setFitWidth(hw);

        setL4(new ImageView(frogLive));
        getL4().setFitHeight(hw);
        getL4().setFitWidth(hw);

        //adding images to h2
        getH2().getChildren().add(getL1());
        getH2().getChildren().add(getL2());
        getH2().getChildren().add(getL3());
        getH2().getChildren().add(getL4());

        getH2().setSpacing(5);

        //adding nodes to vbox container
        root.getChildren().add(getH1());
        root.getChildren().add(getPane());
        root.getChildren().add(getH2());
        root.setAlignment(Pos.CENTER);
        getH1().setAlignment(Pos.CENTER);
        getH2().setAlignment(Pos.BOTTOM_LEFT);

        //background
        root.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));

        //play again screen
        BorderPane playAgain = new BorderPane();
        VBox buttonsReset = new VBox();

        //buttons for play again plus score
        setDd(new Label("You died"));
        getDd().setFont(Font.font("Roboto", FontWeight.BOLD, 13));
        getDd().setTextFill(Color.SNOW);

        setSc(new Label());
        getSc().setFont(Font.font("Roboto", FontWeight.BOLD, 13));
        getSc().setTextFill(Color.SNOW);

        setAgain(new Button("Play again"));
        setQuit(new Button("Quit"));

        //adding nodes to pane
        buttonsReset.getChildren().add(getDd());
        buttonsReset.getChildren().add(getSc());
        buttonsReset.getChildren().add(getAgain());
        buttonsReset.getChildren().add(getQuit());
        buttonsReset.setSpacing(15);
        buttonsReset.setAlignment(Pos.CENTER);

        //bg color for pane
        playAgain.setBackground(new Background(new BackgroundFill(Color.GREEN, null, null)));

        //adding vbox to borderpane
        playAgain.setCenter(buttonsReset);

        //scenes
        setScn(new Scene(root, 516, 550));
        Scene scn2 = new Scene(start, 475, 400);
        setScn3(new Scene(playAgain, 475, 450));

        //initial position of frog
        setRow(13);
        setColumn(6);

        //Game handle plus keyframe
        setGameHandler(new Handler(stage));
        setKeyFrame(new KeyFrame(Duration.seconds(getDuration()), getGameHandler()));

        getScn().setOnKeyPressed(event -> moveFrog(event, getFrogSprite()));
        setGame(new Timeline(getKeyFrame()));
        getGame().setCycleCount(Timeline.INDEFINITE);

        stage.setTitle("Frogger");

        //initially enter screen is showed
        stage.setScene(scn2);

        //when button is clicked scene changes game starts
        button.setOnAction(e -> {
            stage.setScene(getScn());
            getGame().play();
        });
        stage.show();

    }

    public void initializeObjects() {
        setFrog(new Frog(6, 13, 1, 1, "/game/frogg.png"));

        //Vehicles
        //x and y values are given according to game board
        //y is row, x is column
        //multiple vehicles with some vehicles start from right some from left hence the difference in x
        setVehicles(new Vehicle[][]{
                {new Vehicle(0, 8, 1, 1, "/game/rightcar1.jpeg"), new Vehicle(4, 8, 1, 1, "/game/truck1.png"), new Vehicle(8, 8, 1, 1, "/game/rightcar3.jpeg")},
                {new Vehicle(12, 9, 1, 1, "/game/leftcar2.jpeg"), new Vehicle(9, 9, 1, 1, "/game/leftcar1.jpeg")},
                {new Vehicle(0, 10, 1, 1, "/game/truck1.png"), new Vehicle(5, 10, 1, 1, "/game/truck1.png")},
                {new Vehicle(12, 11, 1, 1, "/game/truck.png"), new Vehicle(11, 11, 1, 1, "/game/leftcar1.jpeg"), new Vehicle(6, 11, 1, 1, "/game/leftcar2.jpeg")},
                {new Vehicle(0, 12, 1, 1, "/game/rightcar3.jpeg"), new Vehicle(3, 12, 1, 1, "/game/rightcar4.jpeg")}
        });

        //Logs similar to vehicles
        setLogs(new Log[][]{
                {new Log(0, 2, 1, 1, "/game/log.png"), new Log(5, 2, 1, 1, "/game/log.png"), new Log(9, 2, 1, 1, "/game/log.png")},
                {new Log(0, 4, 1, 1, "/game/log.png"), new Log(8, 4, 1, 1, "/game/log.png"), new Log(12, 4, 4, 1, "/game/log.png")},
                {new Log(0, 6, 1, 1, "/game/log.png"), new Log(3, 6, 1, 1, "/game/log.png"), new Log(8, 6, 1, 1, "/game/log.png")}
        });

        //Animals similar to above two
        setAnimals(new Animal[][]{
                {new Animal(12, 3, 1, 1, "/game/animal1.jpeg"), new Animal(9, 3, 1, 1, "/game/turtle.jpeg"), new Animal(6, 3, 1, 1, "/game/animal1.jpeg"), new Animal(3, 3, 1, 1, "/game/turtle.jpeg")},
                {new Animal(12, 5, 1, 1, "/game/turtle.jpeg"), new Animal(9, 5, 1, 1, "/game/animal1.jpeg"), new Animal(6, 5, 1, 1, "/game/turtle.jpeg"), new Animal(3, 5, 1, 1, "/game/animal1.jpeg")}
        });
    }

    public boolean containsAnimalsOrLogs(Animal[][] animals, Log[][] logs, int col, int row, GridPane pane, ArrayList<ImageView> sprites) {

        for (Animal[] animal : animals) {
            for (Animal a : animal) {
                if (col == a.x && row == a.y) {
                    //if they match add the sprite
                    ImageView sprite = new ImageView(a.sprite);
                    sprite.setFitHeight(20);
                    sprite.setFitWidth(30);

                    //0-7 animal sprites as there are 8 animals
                    sprites.add(sprite);
                    pane.add(sprite, col, row);
                    return true;
                }
            }
        }

        for (Log[] log : logs) {
            for (Log l : log) {
                if (col == l.x && row == l.y) {
                    //if they match add the sprite
                    ImageView sprite = new ImageView(l.sprite);
                    sprite.setFitHeight(20);
                    sprite.setFitWidth(40);

                    //8-16 log sprites as there are 9 logs
                    sprites.add(sprite);
                    pane.add(sprite, col, row);
                    return true;
                }
            }
        }

        return false;
    }

    public boolean containsVehicles(Vehicle[][] vehicles, int col, int row, GridPane pane, ArrayList<ImageView> sprites) {

        for (Vehicle[] vehicle : vehicles) {
            for (Vehicle v : vehicle) {
                if (col == v.x && row == v.y) {
                    //if they match add the sprite
                    ImageView sprite = new ImageView(v.sprite);
                    sprite.setFitHeight(20);
                    sprite.setFitWidth(40);

                    //17-29 vehicle sprites as there are 12 vehicle
                    sprites.add(sprite);
                    pane.add(sprite, col, row);
                    return true;
                }
            }
        }

        return false;
    }

    public void moveFrog(KeyEvent event, ImageView sprite) {

        String key = event.getText();

        int frogRow = GridPane.getRowIndex(sprite);
        int frogCol = GridPane.getColumnIndex(sprite);

        //for exception handling
        //if frogs position is illegal, set it to previous one
        int prevRow = frogRow;
        int prevCol = frogCol;

        try {
            //up
            if (key.equals("w") || key.equals("W")) {
                frogRow--;
                setRow(getRow() - 1);
                //each top movement adds 10 to score
                setScore(getScore() + 10);
            }

            //down
            else if (key.equals("s") || key.equals("S")) {
                //using this condition because of grid design
                if (frogRow > 12) {
                    System.out.println("Out of bounds.");
                    return;
                } else {
                    frogRow++;
                    setRow(getRow() + 1);
                }
            }

            //left
            else if (key.equals("a") || key.equals("A")) {
                frogCol--;
                setColumn(getColumn() - 1);
            }

            //right
            else if (key.equals("d") || key.equals("D")) {
                //using this condition because of grid design
                if (frogCol >= 12) {
                    System.out.println("Out of bounds.");
                    return;
                } else {
                    frogCol++;
                    setColumn(getColumn() + 1);
                }
            } else {
                System.out.println("Press w,d,a,s to move the frog.");
                return;
            }

            //setting frog to new position after key event
            GridPane.setRowIndex(sprite, frogRow);
            GridPane.setColumnIndex(sprite, frogCol);

        } catch (IllegalArgumentException e) {
            System.out.println("Out of bounds.");
            //setting frog to prev position after key event
            GridPane.setRowIndex(sprite, prevRow);
            GridPane.setColumnIndex(sprite, prevCol);
        }

    }

    public void setUpGrid(GridPane pane) {

        //columns as there are 13
        for (int col = 0; col < 13; col++) {
            ColumnConstraints constraints = new ColumnConstraints();
            constraints.setPercentWidth(100.0 / 13);
            pane.getColumnConstraints().add(constraints);
        }

        //rows as there are 14
        for (int row = 0; row < 14; row++) {
            RowConstraints constraints = new RowConstraints();
            constraints.setPercentHeight(100.0 / 14);
            pane.getRowConstraints().add(constraints);
        }

    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getFrogsHome() {
        return frogsHome;
    }

    public void setFrogsHome(int frogsHome) {
        this.frogsHome = frogsHome;
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public boolean isDied() {
        return died;
    }

    public void setDied(boolean died) {
        this.died = died;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public GridPane getPane() {
        return pane;
    }

    public void setPane(GridPane pane) {
        this.pane = pane;
    }

    public ImageView getFrogSprite() {
        return frogSprite;
    }

    public void setFrogSprite(ImageView frogSprite) {
        this.frogSprite = frogSprite;
    }

    public ArrayList<ImageView> getObstacleSprites() {
        return obstacleSprites;
    }

    public void setObstacleSprites(ArrayList<ImageView> obstacleSprites) {
        this.obstacleSprites = obstacleSprites;
    }

    public HBox getH1() {
        return h1;
    }

    public void setH1(HBox h1) {
        this.h1 = h1;
    }

    public HBox getH2() {
        return h2;
    }

    public void setH2(HBox h2) {
        this.h2 = h2;
    }

    public Label getScoring() {
        return scoring;
    }

    public void setScoring(Label scoring) {
        this.scoring = scoring;
    }

    public Label getTm() {
        return tm;
    }

    public void setTm(Label tm) {
        this.tm = tm;
    }

    public Label getLvl() {
        return lvl;
    }

    public void setLvl(Label lvl) {
        this.lvl = lvl;
    }

    public Label getSc() {
        return sc;
    }

    public void setSc(Label sc) {
        this.sc = sc;
    }

    public Label getDd() {
        return dd;
    }

    public void setDd(Label dd) {
        this.dd = dd;
    }

    public ImageView getL1() {
        return l1;
    }

    public void setL1(ImageView l1) {
        this.l1 = l1;
    }

    public ImageView getL2() {
        return l2;
    }

    public void setL2(ImageView l2) {
        this.l2 = l2;
    }

    public ImageView getL3() {
        return l3;
    }

    public void setL3(ImageView l3) {
        this.l3 = l3;
    }

    public ImageView getL4() {
        return l4;
    }

    public void setL4(ImageView l4) {
        this.l4 = l4;
    }

    public Scene getScn() {
        return scn;
    }

    public void setScn(Scene scn) {
        this.scn = scn;
    }

    public Scene getScn3() {
        return scn3;
    }

    public void setScn3(Scene scn3) {
        this.scn3 = scn3;
    }

    public Button getAgain() {
        return again;
    }

    public void setAgain(Button again) {
        this.again = again;
    }

    public Button getQuit() {
        return quit;
    }

    public void setQuit(Button quit) {
        this.quit = quit;
    }

    public Handler getGameHandler() {
        return gameHandler;
    }

    public void setGameHandler(Handler gameHandler) {
        this.gameHandler = gameHandler;
    }

    public Timeline getGame() {
        return game;
    }

    public void setGame(Timeline game) {
        this.game = game;
    }

    public KeyFrame getKeyFrame() {
        return keyFrame;
    }

    public void setKeyFrame(KeyFrame keyFrame) {
        this.keyFrame = keyFrame;
    }

    public Animal[][] getAnimals() {
        return animals;
    }

    public void setAnimals(Animal[][] animals) {
        this.animals = animals;
    }

    public Log[][] getLogs() {
        return logs;
    }

    public void setLogs(Log[][] logs) {
        this.logs = logs;
    }

    public Vehicle[][] getVehicles() {
        return vehicles;
    }

    public void setVehicles(Vehicle[][] vehicles) {
        this.vehicles = vehicles;
    }

    public Frog getFrog() {
        return frog;
    }

    public void setFrog(Frog frog) {
        this.frog = frog;
    }

    private class Handler implements EventHandler<ActionEvent> {

        int frogRow = getRow();
        int frogColumn = getColumn();
        boolean matched = true;
        ImageView home;
        ArrayList<ImageView> homes = new ArrayList<>();
        int i = 0;
        Stage stage; //for changing scene to display play again scene

        public Handler(Stage stage) {
            this.stage = stage;
        }

        @Override
        public void handle(ActionEvent event) {

            //handling game state at first
            //updating the score and time
            getScoring().setText("Score: " + getScore());
            setTime(getTime() - Math.ceil(getDuration()));
            getTm().setText("Time: " + (int) getTime());

            //rotating lives
            rotateSprites();

            if (getTime() == 0 || getLives() == 0) {
                //all frog lives have depleted
                setDied(true);
            }

            if (isDied()) {
                //score for play again screen, died label
                getSc().setText("Score: " + getScore());
                getDd().setText("You died");
                //play again screen
                stage.setScene(getScn3());
                getAgain().setOnAction(e -> resetGame(stage, home));
                getQuit().setOnAction(e -> Platform.exit());
            }

            if (getLevel() > 5) {
                //score for play again screen, died label
                getSc().setText("Score: " + getScore());
                getDd().setText("You win!");
                //play again screen
                stage.setScene(getScn3());
                getAgain().setOnAction(e -> resetGame(stage, home));
                getQuit().setOnAction(e -> Platform.exit());
            }

            frogRow = getRow();
            frogColumn = getColumn();

            //always moving obstacles
            moveObstacles(getObstacleSprites());

            if (checkCollision(getObstacleSprites(), frogRow, frogColumn)) {
                //frog has collided set it to initial starting position
                setRow(13);
                setColumn(6);
                GridPane.setColumnIndex(getFrogSprite(), getColumn());
                GridPane.setRowIndex(getFrogSprite(), getRow());

                //lives decrease by one
                setLives(getLives() - 1);
            }

            if (checkFrogHome(frogRow, frogColumn)) {

                //pane is used only here to add the frog home
                Frog frogHome = new Frog(frogColumn, frogRow, 1, 1, "/game/home.png");
                homes.add(new ImageView(frogHome.sprite));
                getPane().add(homes.get(i), frogColumn, frogRow);
                i++;

                //guiding each frog home gives 50 points
                setScore(getScore() + 50);

                //counting how many frogs have reached home
                setFrogsHome(getFrogsHome() + 1);

                //reset the timer so other frogs can be placed
                setTime(60);

                //lvl complete
                if (getFrogsHome() == 5) {
                    //guiding all 5 frogs gives 1000 points
                    setScore(getScore() + 1000);

                    //updating level, speeding game
                    setLevel(getLevel() + 1);
                    getLvl().setText("Level: " + getLevel());
                    setDuration(getDuration() - 0.04);

                    //removing placed frogs
                    emptyPlacements(homes);
                    setFrogsHome(0);

                    //updating keyframe, timeline as duration changed
                    setKeyFrame(new KeyFrame(Duration.seconds(getDuration()), getGameHandler()));
                    getGame().stop();
                    getGame().getKeyFrames().clear();
                    getGame().getKeyFrames().add(getKeyFrame());
                    getGame().play();
                }

                //frog is home so frog reset
                setRow(13);
                setColumn(6);
                GridPane.setColumnIndex(getFrogSprite(), getColumn());
                GridPane.setRowIndex(getFrogSprite(), getRow());

                System.out.println("home");
            }

            //pond starts less than 7
            else if (getRow() < 7 && checkDrowned(getObstacleSprites(), getFrogSprite(), frogRow, frogColumn)) {
                //frog has drowned set it to initial starting position
                setRow(13);
                setColumn(6);
                GridPane.setColumnIndex(getFrogSprite(), getColumn());
                GridPane.setRowIndex(getFrogSprite(), getRow());

                //lives decrease by one
                setLives(getLives() - 1);
            } else {
                //match frog with log or animal
                matchFrog();
            }
        }

        public boolean checkFrogHome(int row, int col) {

            //frog home placements
            if (row == 1 && (col == 1 || col == 3 || col == 6 || col == 9 || col == 11)) {
                return true;
            } else {
                return false;
            }

        }

        public void emptyPlacements(ArrayList<ImageView> sprites) {
            for (ImageView sprite : sprites) {
                getPane().getChildren().remove(sprite);
            }
        }

        public void matchFrog() {

            try {
                //based on row have to increase or decrease column
                int matchedCol = getColumn();

                if (getRow() == 2 || getRow() == 4 || getRow() == 6) {
                    //logs go right
                    matchedCol++;
                    setColumn(getColumn() + 1);
                    if (matchedCol == 13) {
                        //out of bounds
                        matched = false;
                    }
                    GridPane.setColumnIndex(getFrogSprite(), matchedCol);
                    GridPane.setRowIndex(getFrogSprite(), getRow());
                } else if (getRow() == 3 || getRow() == 5) {
                    //animals go left
                    matchedCol--;
                    setColumn(getColumn() - 1);
                    if (matchedCol == 0) {
                        //out of bounds
                        matched = false;
                    }

                    GridPane.setColumnIndex(getFrogSprite(), matchedCol);
                    GridPane.setRowIndex(getFrogSprite(), getRow());
                }

            } catch (IllegalArgumentException e) {
                System.out.println("Out of bounds.");
                matched = false;
            }
        }

        public boolean checkCollision(ArrayList<ImageView> obstacleSprites, int frogRow, int frogCol) {

            int row, col;

            for (ImageView sprite : obstacleSprites) {

                row = GridPane.getRowIndex(sprite);
                col = GridPane.getColumnIndex(sprite);

                //vehicles
                if (row >= 8 && row <= 12) {
                    //vehicle collision
                    if (frogRow == row && frogCol == col || frogRow == row && frogCol == col - 1 || frogRow == row && frogCol == col + 1) {
                        System.out.println("Collision.");
                        return true;
                    }

                }
            }
            return false;
        }

        public boolean checkDrowned(ArrayList<ImageView> obstacleSprites, ImageView frog, int frogRow, int frogCol) {
            //invert the logic of checkCollision
            int row, col;

            for (ImageView sprite : obstacleSprites) {
                row = GridPane.getRowIndex(sprite);
                col = GridPane.getColumnIndex(sprite);

                //animals and logs
                if (!(row >= 8 && row <= 12)) {
                    //frog is on log or animal
                    if (frogRow == row && frogCol == col - 1 || frogRow == row && frogCol == col + 1 || frogRow == row && frogCol == col) {
                        return false;
                    }

                }
            }
            System.out.println("Drowned");
            return true;
        }

        public void rotateSprites() {
            if (getLives() == 3) {
                getL4().setRotate(180);
            } else if (getLives() == 2) {
                getL3().setRotate(180);
            } else if (getLives() == 1) {
                getL2().setRotate(180);
            } else if (getLives() == 0) {
                getL1().setRotate(180);
            }
        }

        public void undoRotate() {
            getL1().setRotate(0);
            getL2().setRotate(0);
            getL3().setRotate(0);
            getL4().setRotate(0);
        }

        public void resetGame(Stage stage, ImageView sprite) {
            //resetting the conditions of game
            setFrogsHome(0);
            setLevel(1);
            setScore(0);
            setLives(4);
            setRow(13);
            setColumn(6);
            GridPane.setRowIndex(frogSprite, 13);
            GridPane.setColumnIndex(frogSprite,6);
            setTime(60);
            setDied(false);
            undoRotate();

            getLvl().setText("Level: " + getLevel());

            emptyPlacements(homes);

            //resetting frames as diff levels have diff speeds
            setDuration(0.5);
            setKeyFrame(new KeyFrame(Duration.seconds(getDuration()), getGameHandler()));
            getGame().stop();
            getGame().getKeyFrames().clear();
            getGame().getKeyFrames().add(getKeyFrame());
            getGame().play();

            //changing scene to game's scene
            stage.setScene(getScn());
        }

        public void moveObstacles(ArrayList<ImageView> sprites) {

            int row, col;

            for (int i = 0; i < 29; i++) {
                //row remains constant in each case

                row = GridPane.getRowIndex(sprites.get(i));
                col = GridPane.getColumnIndex(sprites.get(i));

                //animals are on row 3, 5
                if (row == 3 || row == 5) {
                    //animals have to move left so columns decrease
                    if (col == 0) {
                        col = 12;
                    } else {
                        col--;
                    }
                    GridPane.setRowIndex(sprites.get(i), row);
                    GridPane.setColumnIndex(sprites.get(i), col);

                }

                //logs are on row 2,4,6
                else if (row == 2 || row == 4 || row == 6) {
                    //for logs increase as they go right
                    //out of bounds
                    if (col == 12) {
                        col = 0;
                    } else {
                        col++;
                    }

                    GridPane.setRowIndex(sprites.get(i), row);
                    GridPane.setColumnIndex(sprites.get(i), col);

                }

                //vehicles are on rows 8,9,10,11,12
                else if (row == 8 || row == 10 || row == 12) {
                    //vehicles going right
                    //out of bounds
                    if (col == 12) {
                        col = 0;
                    } else {
                        col++;
                    }

                    GridPane.setRowIndex(sprites.get(i), row);
                    GridPane.setColumnIndex(sprites.get(i), col);


                } else if (row == 9 || row == 11) {
                    //vehicles going left
                    //out of bounds

                    if (col == 0) {
                        col = 12;
                    } else {
                        col--;
                    }

                    GridPane.setRowIndex(sprites.get(i), row);
                    GridPane.setColumnIndex(sprites.get(i), col);

                }
            }
        }

    }
}
