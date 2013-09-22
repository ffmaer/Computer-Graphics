import java.awt.*;
import java.util.Random;

public class Stage extends BufferedApplet {
	private static final long serialVersionUID = 1L;

	int width, height;

	double startTime = System.currentTimeMillis() / 1000.0;

	double t = 0, sint = 0;
	Matrix m = new Matrix();
	Random rnd = new Random();

	Geometry world, neck, person, mover;
	Geometry head;

	public void init() {

		width = getWidth();
		height = getHeight();

		Geometry.height = height;
		Geometry.width = width;

		world = shape(0, 0);
		mover = shape(0, 0);
		neck = shape(0, 0);
		person = shape(0, 0);

		head = shape(40, 1);

		head.cylinder();

		world.add(mover);
		mover.add(person);
		person.add(neck);

		neck.add(head);

		m = head.matrix;
		m.scale(0.2, 0.2, 0.2);

	}

	public Geometry shape(int m, int n) {
		Geometry geo = new Geometry(m, n);
		return geo;
	}

	// zbuffer

	// frame buffer

	public void render(Graphics g) {

		Geometry.g = g;
		g.setColor(Color.black);
		g.fillRect(0, 0, width, height);
		g.setColor(Color.white);

		animate();

		m = mover.matrix;
		m.identity();
		m.rotateX(t);

		world.root_matrix.identity();
		world.traverse();

	}

	public void animate() {
		t = System.currentTimeMillis() / 1000.0 - startTime;
	}

}