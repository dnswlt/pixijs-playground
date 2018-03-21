package ch.sbb.tms.l.javafx;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;

import java.util.function.Function;

public class JavaFXCanvas extends Application {

    private static final int NUM_NODES = 1000;


    public static void main(String[] args) {
		launch(args);
	}
 
	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("Drawing Operations Test");
		Group root = new Group();
		NodeState[] nodes = createNodes(NUM_NODES);
		root.getChildren().addAll(tf(nodes, ns -> ns.node));
        Label label = new Label();
        Scene rootScene = new Scene(new VBox(label, root), 1024, 768);
        primaryStage.setScene(rootScene);
		primaryStage.show();
		new ThrottledAnimationTimer(20.0) {
            @Override
            public void handleThrottled(long now, double frameRate) {
                label.setText(String.format("Current frame rate: %.3f", frameRate));
                for (int i = 0; i < nodes.length; i++) {
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
                }
            }
        }.start();

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

    private NodeState[] createNodes(int num) {
        NodeState[] nodes = new NodeState[num];
	    for (int i = 0; i < num; i++) {
            Polygon p = new Polygon();
            p.getPoints().addAll(new Double[] {
                    0.0, 0.0,
                    -10.0, 20.0,
                    10.0, 20.0
            });
            p.setFill(new Color(rnd(), rnd(), rnd(), 1));
            nodes[i] = new NodeState(p, 10 * rnd(), rnd(), rnd());
        }
        return nodes;
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
