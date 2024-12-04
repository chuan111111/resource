package lab10;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SpaceInvaderGame extends Application {

    public static final int WIN_W = 600;
    public static final int WIN_H = 600;

    public static final int PLAYER_X = 350;
    public static final int PLAYER_Y = 550;
    public static final int PLAYER_W = 40;
    public static final int PLAYER_H = 40;
    private double interval = 0;
    public static final int ENEMY_X_START = 90;
    public static final int ENEMY_Y = 100;
    public static final int ENEMY_W = 30;
    public static final int ENEMY_H = 30;
    public static final int ENEMY_INTERVAL = 100;

    private final Pane root = new Pane();

    private final List<Line> EnemyBullet = new ArrayList<>();
    private final List<Line> PlayerBullet = new ArrayList<>();

    private final List<Sprite> EnemyList = new ArrayList<>();

    private final Sprite player = new Sprite(PLAYER_X, PLAYER_Y, PLAYER_W, PLAYER_H, "player", Color.BLUE);

    private Parent createContent() {
        root.setPrefSize(WIN_W, WIN_H);
        root.getChildren().add(player);

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
            }
        };
        timer.start();

        createEnemies();

        return root;
    }

    private void createEnemies() {
        for (int i = 0; i < 5; i++) {
            Sprite s = new Sprite(ENEMY_X_START + i * ENEMY_INTERVAL, ENEMY_Y, ENEMY_W, ENEMY_H, "enemy", Color.RED);
            EnemyList.add(s);
            root.getChildren().add(s);
        }
    }

    private void update() {
        interval += 0.02;
        for (Sprite enemy : EnemyList) {
            if (enemy.isAlive() && interval > Math.random() * 2 + 1) { // 调整时间间隔
                Line bullet = shoot(enemy);
                EnemyBullet.add(bullet);
                interval = 0; // 重置间隔计时器
            }
        }

        for (Line bullet : EnemyBullet) {
            bullet.setStartY(bullet.getStartY() + 5);
            bullet.setEndY(bullet.getEndY() + 5);

            if (bullet.getBoundsInParent().intersects(player.getBoundsInParent())) {
                player.setDead(true);
            }
            if (bullet.getStartY() > WIN_H) {
                EnemyBullet.remove(bullet);
                root.getChildren().remove(bullet);
            }
        }

        for (Line bullet : PlayerBullet) {
            bullet.setStartY(bullet.getStartY() - 5);
            bullet.setEndY(bullet.getEndY() - 5);

            for (Sprite enemy : EnemyList) {
                if (enemy.isAlive() && bullet.getBoundsInParent().intersects(enemy.getBoundsInParent())) {
                    enemy.setDead(true);
                    root.getChildren().remove(enemy);
                    PlayerBullet.remove(bullet);
                    root.getChildren().remove(bullet); // 移除子弹
                    break;
                }
            }
            if (bullet.getStartY() < 0) {
                PlayerBullet.remove(bullet);
                root.getChildren().remove(bullet);
            }
        }

        root.getChildren().removeIf(n -> n instanceof Sprite && ((Sprite) n).isAlive() == false);
     //   EnemyBullet.removeIf(bullet -> bullet.getStartY() > WIN_H); // Remove bullets that are off-screen
   //     PlayerBullet.removeIf(bullet -> bullet.getStartY() < 0); // Remove bullets that are off-screen
        if (interval > 3) {
            interval = 0;
        }

    }

    private Line shoot(Sprite who) {
        if (!who.isAlive()) {
            return null;
        }

        Line line = new Line(
                who.getTranslateX() + who.getWidth() / 2,
                who.getTranslateY(),
                who.getTranslateX() + who.getWidth() / 2,
                who.getTranslateY() - 10
        );
        line.setStroke(who.getType().equals("player") ? Color.BLUE : Color.RED);
        line.setStrokeWidth(2);
        root.getChildren().add(line);
        return line;
    }

    @Override
    public void start(Stage stage) throws Exception {
        Scene scene = new Scene(createContent());
        scene.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case LEFT:
                    player.moveLeft();
                    break;
                case RIGHT:
                    player.moveRight();
                    break;
                case UP:
                    player.moveUp();
                    break;
                case DOWN:
                    player.moveDown();
                    break;
                case SPACE:
                    Line temp = shoot(player);
                    if (temp != null) {
                        PlayerBullet.add(temp);
                    }
                    break;
            }
        });

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private static class Sprite extends Rectangle {
        private boolean dead = false;
        private final String type;

        public Sprite(int x, int y, int w, int h, String type, Color color) {
            super(w, h, color);
            this.type = type;
            setTranslateX(x);
            setTranslateY(y);
        }

        public boolean isAlive() {
            return !dead;
        }

        public void setDead(boolean dead) {
            this.dead = dead;
        }

        public String getType() {
            return type;
        }

        public void moveLeft() {
            setTranslateX(getTranslateX() - 5);
        }

        public void moveRight() {
            setTranslateX(getTranslateX() + 5);
        }

        public void moveUp() {
            setTranslateY(getTranslateY() - 5);
        }

        public void moveDown() {
            setTranslateY(getTranslateY() + 5);
        }
    }
}