package ch.sbb.tms.l.javafx;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.function.Function;

public class JavaFXCanvas extends Application {

    private static final int NUM_NODES = 1000;
    private static final int NUM_MOVING_NODES = 100;
    private static final double FPS = 20;
    private NodeState[] rectangles;
    private NodeState[] triangles;
    private NodeState[] lines;
    private NodeState[] currentNodes;
    private boolean animate = true;
    private Scene rootScene;

    public static void main(String[] args) {
		launch(args);
	}
 
	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("Drawing Operations Test");
		Group root = new Group();
        root.setManaged(false);
        currentNodes = rectangles = createRects(NUM_NODES);
        triangles = createTriangles(NUM_NODES);
        lines = createLines(NUM_NODES);
        update(root);
        BorderPane borderPane = new BorderPane(root);
        Button cmdShape = new Button("Shape");
        cmdShape.setOnAction(e -> {
            if (currentNodes == rectangles) {
                currentNodes = triangles;
            } else if (currentNodes == triangles) {
                currentNodes = lines;
            } else {
                currentNodes = rectangles;
            }
            update(root);
        });
        Button cmdAnimate = new Button("Animate");
        cmdAnimate.setOnAction(e -> {
            animate = !animate;
        });
        borderPane.setTop(new HBox(cmdShape, cmdAnimate));
        rootScene = new Scene(borderPane, 1024, 768);
        primaryStage.setScene(rootScene);
		primaryStage.show();
		new FpsAnimationTimer(FPS).start();

	}

	private void update(Group g) {
        g.getChildren().clear();
        g.getChildren().addAll(tf(currentNodes, n -> n.node));
    }

	private <S, T> T[] tf(S[] src, Function<S, T> fn) {
	    T[] tgt = (T[]) new Object[src.length];
        for (int i = 0; i < src.length; i++) {
            tgt[i] = fn.apply(src[i]);
        }
        return tgt;
    }

    private double rnd() {
        return Math.random();
    }

    private NodeState[] createTriangles(int num) {
        NodeState[] nodes = new NodeState[num];
	    for (int i = 0; i < num; i++) {
            Polygon p = new Polygon();
            p.getPoints().addAll(new Double[] {
                    0.0, 0.0,
                    -10.0, 20.0,
                    10.0, 20.0
            });
            p.setFill(new Color(rnd(), rnd(), rnd(), 1));
            p.setRotate(rnd() * 360);
            nodes[i] = new NodeState(p, 10 * rnd(), rnd(), rnd());
        }
        return nodes;
    }

    private NodeState[] createLines(int num) {
        NodeState[] nodes = new NodeState[num];
        for (int i = 0; i < num; i++) {
            Polyline l = new Polyline();
            l.getPoints().addAll(new Double[] {
                    0.0, 0.0,
                    -10.0, 20.0,
                    10.0, 20.0
            });
            l.setStroke(new Color(rnd(), rnd(), rnd(), 1));
            l.setRotate(rnd() * 360);
            l.setStrokeWidth(3);
            nodes[i] = new NodeState(l, 10 * rnd(), rnd(), rnd());
        }
        return nodes;
    }

    private NodeState[] createRects(int num) {
        NodeState[] nodes = new NodeState[num];
        for (int i = 0; i < num; i++) {
            Rectangle r = new Rectangle(20, 20, new Color(rnd(), rnd(), rnd(), 1));
            r.setRotate(rnd() * 360);
            nodes[i] = new NodeState(r, 10 * rnd(), rnd(), rnd());
        }
        return nodes;
    }

    private class FpsAnimationTimer extends AnimationTimer {


        private final double fps;
        private int loopNum;
        private long started;

        public FpsAnimationTimer(double fps) {
            this.fps = fps;
        }

        @Override
        public void handle(long now) {
            if (started == 0) {
                started = now;
            }
            if (started + 1_000_000_000/fps * loopNum > now) {
                return;
            }
            loopNum++;
            if (!animate) {
                return;
            }
            NodeState[] nodes = currentNodes;
            for (int i = 0; i < nodes.length; i++) {
                if (rnd() > (double)NUM_MOVING_NODES / NUM_NODES) {
                    continue;
                }
                NodeState n = nodes[i];
                double nx = n.node.getTranslateX() + n.speed * n.dirX;
                double ny = n.node.getTranslateY() + n.speed * n.dirY;
                if (nx < 0 || nx > rootScene.getWidth()) {
                    n.dirX = -n.dirX;
                }
                if (ny < 0 || ny > rootScene.getHeight()) {
                    n.dirY = -n.dirY;
                }
                n.node.setTranslateX(nx);
                n.node.setTranslateY(ny);
//                    n.node.setRotate(n.node.getRotate() + 1);
            }
        }
    }

    private static class NodeState {
	    Node node;
	    double speed;
	    double dirX;
	    double dirY;

        public NodeState(Node node, double speed, double dirX, double dirY) {
            this.node = node;
            this.speed = speed;
            this.dirX = dirX;
            this.dirY = dirY;
        }
    }

}
