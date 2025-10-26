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
	protected boolean isErasing = false;

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
			isErasing = false; // Set drawing mode
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

	public ChoiceBox<String> getEraserChooser(GraphicsContext graphicsContext) {
		ChoiceBox<String> eraserChooser = new ChoiceBox<>(FXCollections.observableArrayList("Eraser Off", "Eraser 1", "Eraser 2", "Eraser 3"));
		eraserChooser.getSelectionModel().selectFirst();

		eraserChooser.getSelectionModel().selectedIndexProperty().addListener((ChangeListener) (ov, old, newval) -> {
			Number idx = (Number) newval;

			switch (idx.intValue()) {
				case 0:
					isErasing = false; // Turn off erasing
					break;
				case 1:
					isErasing = true;  // Turn on erasing
					graphicsContext.setLineWidth(5);
					break;
				case 2:
					isErasing = true;  // Turn on erasing
					graphicsContext.setLineWidth(10);
					break;
				case 3:
					isErasing = true;  // Turn on erasing
					graphicsContext.setLineWidth(15);
					break;
				case 4:
					isErasing = true;  // Turn on erasing
					graphicsContext.setLineWidth(30);
					break;	
				default:
					isErasing = false;
					break;
			}
		});
		eraserChooser.setTranslateX(5);
		return eraserChooser;
	}

	private Button getResetButton(GraphicsContext drawingGC){
		Button resetButton = new Button("Reset");
		resetButton.setOnAction(actionEvent -> {
			// Only clear the drawing canvas (top layer) - this reveals the background guides
			drawingGC.clearRect(0, 0, drawingGC.getCanvas().getWidth(),
					drawingGC.getCanvas().getHeight());
			// Reset to drawing mode
			isErasing = false;
			drawingGC.setStroke(Color.BLACK);
			drawingGC.setLineWidth(1);
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
			
			if (isErasing) {
				// For erasing, clear to transparent
				utilsFX.rotateClearRect(graphicsContext, 6, (int)(graphicsContext.getLineWidth()/2), center, mirror);
				// graphicsContext.clearRect(x - graphicsContext.getLineWidth()/2, 
				// 						 y - graphicsContext.getLineWidth()/2, 
				// 						 graphicsContext.getLineWidth(), 
				// 						 graphicsContext.getLineWidth());
			} else {
				// For drawing, draw normally
				Line l = new Line(x, y, x, y);
				utilsFX.rotateDraw(graphicsContext, 6, l, center, mirror);
			}
		});

		canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, (MouseEvent event) -> {
			if (isErasing) {
				// For erasing, clear rectangular areas to make transparent
				double lineWidth = graphicsContext.getLineWidth();
				graphicsContext.clearRect(event.getX() - lineWidth/2, 
										 event.getY() - lineWidth/2, 
										 lineWidth, 
										 lineWidth);
			} else {
				// For drawing, draw lines normally
				Line l = new Line(x, y, event.getX(), event.getY());
				utilsFX.rotateDraw(graphicsContext, 6, l, center, mirror);
			}
			x = (int) event.getX();
			y = (int) event.getY();
		});

		canvas.addEventHandler(MouseEvent.MOUSE_RELEASED, (MouseEvent event) -> {
		});
	}

	public void start(Stage primaryStage) {
		StackPane root = new StackPane(); //stackpane arranges elements visually as a stack
		BorderPane container = new BorderPane();
		
		// Create two-layer canvas system
		// Bottom layer: Background canvas with white background and guides
		Canvas backgroundCanvas = new Canvas(width, height);
		GraphicsContext backgroundGC = backgroundCanvas.getGraphicsContext2D();
		
		// Top layer: Drawing canvas (transparent) for pen and eraser
		Canvas drawingCanvas = new Canvas(width, height);
		GraphicsContext drawingGC = drawingCanvas.getGraphicsContext2D();
		
		// Set up background canvas with white background and guides
		backgroundGC.setFill(Color.WHITE);
		backgroundGC.fillRect(0, 0, width, height);
		initDraw(backgroundGC, 0, 0);
		
		// Stack the canvases (background first, then drawing on top)
		StackPane canvasStack = new StackPane();
		canvasStack.getChildren().addAll(backgroundCanvas, drawingCanvas);

		final Button resetButton = getResetButton(drawingGC);
		final Button mirrorButton = getMirrorButton();

		// Set up the controls - use drawing canvas context for drawing operations
		ChoiceBox<String> colorChooser = getColorChooser(drawingGC);
		ChoiceBox<String> sizeChooser = getSizeChooser(drawingGC);
		ChoiceBox<String> eraserChooser = getEraserChooser(drawingGC);

		HBox buttonBox = new HBox();
		buttonBox.getChildren().addAll(colorChooser, sizeChooser, eraserChooser, resetButton, mirrorButton);

		container.setTop(buttonBox);
		container.setCenter(canvasStack);

		root.getChildren().add(container);

		addMouseEvents(drawingCanvas, drawingGC);

		Scene scene = new Scene(root, width+50, height+50);
		primaryStage.setTitle("Kaleidesign Graphics Tool");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	private void initDraw(GraphicsContext g, double x, double y) {
		// Save current line width
		double originalLineWidth = g.getLineWidth();
		
		// Set thinner lines for guides
		g.setLineWidth(0.5);
		g.fill();
		g.strokeRect(x, // x of the upper left corner
				y, // y of the upper left corner
				width, // width of the rectangle
				height); // height of the rectangle
		int radius = (int) Math.ceil(Math.sqrt(center.x* center.x + center.y* center.y));
		Line l = new Line(center.x, center.y - radius, center.x, center.y);
		utilsFX.rotateDraw(g, 6, l, center, false);
		
		// Restore original line width
		g.setLineWidth(originalLineWidth);
	}


}
