import java.awt.Color;
import java.awt.Graphics;
@SuppressWarnings("serial")
public class TwistedCubes extends BufferedApplet {

	double endPoints[][][] = { { { -1, 1, 1 }, { 1, 1, 1 }, { 1, -1, 1 },
			{ -1, -1, 1 }, { -1, 1, 1 }, { -1, 1, -1 }, { 1, 1, -1 },
			{ 1, -1, -1 }, { -1, -1, -1 }, { -1, 1, -1 }, { -1, 1, 1 },
			{ 1, -1, -1 }, { 1, -1, 1 }, { 1, 1, 1 }, { 1, 1, -1 },
			{ -1, -1, 1 }, { -1, -1, -1 } } }; // 17 points specifying 16 edges of a cube

	int canvasWidth, canvasHeight;
	double startTime = System.currentTimeMillis() / 1000.0;
	double timePassed = 0, sineScale = 0;
	int pointA[] = { 0, 0 }, pointB[] = { 0, 0 };
	int grey = 0;
	float color = 0;
	int ovalSize = 5;
	Matrix m = new Matrix();

	public void render(Graphics g) {
		canvasWidth = getWidth();
		canvasHeight = getHeight();

		//clean the canvas
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, canvasWidth, canvasHeight);
		
		// change color
		g.setColor(Color.BLACK);
		
		// rotate and scale
		timePassed = System.currentTimeMillis() / 1000.0 - startTime;
		sineScale = Math.sin(timePassed) / 5;
		m.identity();
		m.rotateX(timePassed);
		m.rotateY(timePassed);
		m.rotateZ(timePassed);
		m.scale(sineScale, sineScale, sineScale);


		// draw the shape
		for (int i = 0; i < endPoints.length; i++)
			for (int j = 1; j < endPoints[i].length; j++) {// LOOP THROUGH ALL THE LINES IN THE SHAPE
				// apply matrix transformation on both end points of a line
				double transformedEndPointA[] = { 0, 0, 0 }, transfomredEndPointB[] = { 0, 0, 0 };
				m.transform(endPoints[i][j - 1], transformedEndPointA);
				m.transform(endPoints[i][j], transfomredEndPointB);
				viewport(transformedEndPointA, pointA);
				viewport(transfomredEndPointB, pointB);
				g.drawOval(pointA[0], pointA[1], ovalSize, ovalSize);
				g.drawOval(pointB[0], pointB[1], ovalSize, ovalSize);
				g.drawLine(pointA[0], pointA[1], pointB[0], pointB[1]); // DRAW ONE LINE ON THE SCREEN
			}
	}

	public void viewport(double src[], int dst[]) {
		// src[0] contains numbers less than 1, need to scale it up
		// move the graphics to the center of the canvas
		dst[0] = (int) (0.5 * canvasWidth + src[0] * canvasWidth);
		dst[1] = (int) (0.5 * canvasHeight + src[1] * canvasWidth);
	}
}

//https://docs.oracle.com/javase/8/docs/api/java/awt/Graphics.html
