import java.awt.*;

public class TwistedCubes extends BufferedApplet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	double points[][][] = { { { -1, 1, 1 }, { 1, 1, 1 }, { 1, -1, 1 },
			{ -1, -1, 1 }, { -1, 1, 1 }, { -1, 1, -1 }, { 1, 1, -1 },
			{ 1, -1, -1 }, { -1, -1, -1 }, { -1, 1, -1 }, { -1, 1, 1 },
			{ 1, -1, -1 }, { 1, -1, 1 }, { 1, 1, 1 }, { 1, 1, -1 },
			{ -1, -1, 1 }, { -1, -1, -1 } } };

	int width = 0, height = 0;
	double a[] = { 0, 0, 0 }, b[] = { 0, 0, 0 };
	int pa[] = { 0, 0 }, pb[] = { 0, 0 };

	double startTime = System.currentTimeMillis() / 1000.0;
	double t = 0, sint = 0;
	Matrix m = new Matrix();

	int grey = 0;
	float color = 0;

	public void render(Graphics g) {
		width = getWidth();
		height = getHeight();

		g.setColor(Color.red);
		g.fillRect(0, 0, width, height);

		// change color

		grey += 2;
		grey = grey % 255;
		color = (float) ((255 - grey) / 255.0);
		g.setColor(new Color(color, color, color));

		// control shapes

		t = System.currentTimeMillis() / 1000.0 - startTime;
		sint = Math.sin(t) / 5;

		m.identity();
		m.rotateX(t);
		m.rotateY(t);
		m.rotateZ(t);
		m.scale(sint, sint, sint);

		// draw the shape

		for (int i = 0; i < points.length; i++)
			for (int j = 1; j < points[i].length; j++) {// LOOP THROUGH ALL THE
														// LINES IN THE SHAPE
				m.transform(points[i][j - 1], a); // TRANSFORM BOTH ENDPOINTS OF
													// LINE
				m.transform(points[i][j], b);
				viewport(a, pa);
				viewport(b, pb);
				g.drawLine(pa[0], pa[1], pb[0], pb[1]); // DRAW ONE LINE ON THE
														// SCREEN

			}

	}

	public void viewport(double src[], int dst[]) {
		dst[0] = (int) (0.5 * width + src[0] * width);
		dst[1] = (int) (0.5 * height - src[1] * width);
	}
}
