package com.kaleideigngraphicstoolfx;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import java.awt.*;


//Super class for all patterns
public class PatternFX extends Application {
   // This is set to true while the user is drawing.
	protected int width = 500;    // Width of the panel.
	protected int height = 400;

	protected int x, y;

	protected boolean mirror = false;
	protected Point center = new Point(width/2, height/2);

	/**
	* Constructor sets the background color to be
	* white and sets it to listen for mouse events on itself.
	*/

	public static void main(String[] args) {
		Application.launch(PatternFX.class, args);
	}
	public ChoiceBox<String> getColorChooser(GraphicsContext graphicsContext){
		ChoiceBox<String> colorChooser = new ChoiceBox<>(
				FXCollections.observableArrayList("Black", "Blue", "Red", "Green", "Brown", "Orange"));
		// Select the first option by default
		colorChooser.getSelectionModel().selectFirst();

		colorChooser.getSelectionModel().selectedIndexProperty().addListener((ChangeListener) (ov, old, newval) -> {
			Number idx = (Number) newval;
			Color newColor;
			switch (idx.intValue()) {
				case 0:
					newColor = Color.BLACK;
					break;
				case 1:
					newColor = Color.BLUE;
					break;
				case 2:
					newColor = Color.RED;
					break;
				case 3:
					newColor = Color.GREEN;
					break;
				case 4:
					newColor = Color.BROWN;
					break;
				case 5:
					newColor = Color.ORANGE;
					break;
				default:
					newColor = Color.BLACK;
					break;
			}
			graphicsContext.setStroke(newColor);

		});
		//colorChooser.setTranslateX(5);
		return colorChooser;
	}

	public ChoiceBox<String> getSizeChooser(GraphicsContext graphicsContext){
		ChoiceBox<String> sizeChooser = new ChoiceBox<>(FXCollections.observableArrayList("1", "2", "3", "4", "5"));
		// Select the first option by default
		sizeChooser.getSelectionModel().selectFirst();

		sizeChooser.getSelectionModel().selectedIndexProperty().addListener((ChangeListener) (ov, old, newval) -> {
			Number idx = (Number) newval;

			switch (idx.intValue()) {
				case 0:
					graphicsContext.setLineWidth(1);
					break;
				case 1:
					graphicsContext.setLineWidth(2);
					break;
				case 2:
					graphicsContext.setLineWidth(3);
					break;
				case 3:
					graphicsContext.setLineWidth(4);
					break;
				case 4:
					graphicsContext.setLineWidth(5);
					break;
				default:
					graphicsContext.setLineWidth(1);
					break;
			}
		});
		sizeChooser.setTranslateX(5);
		return sizeChooser;
	}

	private Button getResetButton(GraphicsContext g){
		Button resetButton = new Button("Reset");
		resetButton.setOnAction(actionEvent -> {
			g.clearRect(0, 0, g.getCanvas().getWidth(),
					g.getCanvas().getHeight());
			g.fill();
			g.setStroke(Color.BLACK);
			g.setLineWidth(1);
			g.strokeRect(0, // x of the upper left corner
					0, // y of the upper left corner
					width, // width of the rectangle
					height); // height of the rectangle
			int radius = (int) Math.ceil(Math.sqrt(center.x * center.x + center.y * center.y));
			Line l = new Line(center.x, center.y - radius, center.x, center.y);
			utilsFX.rotateDraw(g, 6, l, center, false);

		});
		resetButton.setTranslateX(10);
		return resetButton;
	}

	private Button getMirrorButton(){
		Button mirrorButton = new Button("Mirror: "+ mirror);
		mirrorButton.setOnAction(actionEvent -> {
			mirror = !mirror;
			mirrorButton.setText("Mirror: " + mirror);
		});
		mirrorButton.setTranslateX(10);
		return mirrorButton;
	}

	private void addMouseEvents(Canvas canvas, GraphicsContext graphicsContext){
		canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, (MouseEvent event) -> {
			x = (int) event.getX();
			y = (int) event.getY();
			Line l = new Line(x, y, x, y);
			utilsFX.rotateDraw(graphicsContext, 6, l, center, mirror);
		});

		canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, (MouseEvent event) -> {
			Line l = new Line(x, y, event.getX(), event.getY());
			utilsFX.rotateDraw(graphicsContext, 6, l, center, mirror);
			x = (int) event.getX();
			y = (int) event.getY();
		});

		canvas.addEventHandler(MouseEvent.MOUSE_RELEASED, (MouseEvent event) -> {
		});
	}

	public void start(Stage primaryStage) {
		StackPane root = new StackPane(); //stackpane arranges elements visually as a stack
		Canvas canvas = new Canvas(width, height);
		BorderPane container = new BorderPane();
		final GraphicsContext graphicsContext = canvas.getGraphicsContext2D();

		final Button resetButton = getResetButton(graphicsContext);
		final Button mirrorButton = getMirrorButton();

		// Set up the pen color chooser
		ChoiceBox<String> colorChooser = getColorChooser(graphicsContext);

		ChoiceBox<String> sizeChooser = getSizeChooser(graphicsContext);

		HBox buttonBox = new HBox();
		buttonBox.getChildren().addAll(colorChooser, sizeChooser, resetButton, mirrorButton);

		container.setTop(buttonBox);

		container.setCenter(canvas);

		root.getChildren().add(container);

		addMouseEvents(canvas, graphicsContext);

		initDraw(graphicsContext, canvas.getLayoutX(), canvas.getLayoutY());

		Scene scene = new Scene(root, width+50, height+50);
		primaryStage.setTitle("java2s.com");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	private void initDraw(GraphicsContext g, double x, double y) {

		g.fill();
		g.strokeRect(x, // x of the upper left corner
				y, // y of the upper left corner
				width, // width of the rectangle
				height); // height of the rectangle
		int radius = (int) Math.ceil(Math.sqrt(center.x* center.x + center.y* center.y));
		Line l = new Line(center.x, center.y - radius, center.x, center.y);
		utilsFX.rotateDraw(g, 6, l, center, false);

	}


}
