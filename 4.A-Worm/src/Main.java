import java.awt.Color;
import java.awt.Graphics;

/*
Ken Perlin:

Transformation Hierarchy:

YOUR HOMEWORK, DUE BEFORE CLASS STARTS ON THURSDAY MARCH 1:

Create your own animated hierarchical model. It can be anything that's fun and interesting, and that can be built from the shape primitives that you have implemented. Possibilities include a person running, two people dancing. a cat walking, a spider walking, a butterfly in flight, a twisting worm, a pool table with pool balls, an automobile with rolling wheels and a steering wheel, an electric motor, the solar system As you can see, the possibilities are endless.

For this assignment, please incorporate perspective, as described in class and in the on-line notes.

https://web.archive.org/web/20150915113310/http://mrl.nyu.edu/~perlin/courses/spring2012/hierarchy.html

*/
@SuppressWarnings("serial")
public class Main extends BufferedApplet
{

	int width = 0, height = 0;
	double x=0,y=0,z=0;
	double startTime = System.currentTimeMillis() / 1000.0;
	double duration = 0, sint=0;
	Matrix matrix = new Matrix();

	final static int MATRIX_STACK_SIZE = 100;
	int matrixStackTop = 0;
	Matrix matrixStack[] = new Matrix[MATRIX_STACK_SIZE];
	{
		for (int n = 0 ; n < MATRIX_STACK_SIZE ; n++) {
			matrixStack[n] = new Matrix();
		}
	}

	Geometry[] balls = new Geometry[20];
	
	public void init(){
		width = getWidth();
		height = getHeight();
		Geometry.height = height;
		Geometry.width = width;
		for(int i=0;i<balls.length;i++){
			balls[i] = new Geometry();
			balls[i].globe(8,4);
		}
	}

	// CLEAR THE STACK BEFORE PROCESSING THIS ANIMATION FRAME

	public void mclear() {
		matrixStackTop = 0;
		matrixStack[0].identity();
	}

	// PUSH/COPY THE MATRIX ON TOP OF THE STACK -- RETURN false IF OVERFLOW

	public boolean mpush() {
		if (matrixStackTop + 1 >= MATRIX_STACK_SIZE)
			return false;

		matrixStack[matrixStackTop + 1].copy(matrixStack[matrixStackTop]);
		matrixStackTop++;
		return true;
	}

	// POP OFF THE MATRIX ON TOP OF THE STACK -- RETURN false IF UNDERFLOW

	public boolean mpop() {
		if (matrixStackTop <= 0)
			return false;

		--matrixStackTop;
		return true;
	}

	// RETURN THE MATRIX CURRENTLY ON TOP OF THE STACK

	public Matrix m() {
		return matrixStack[matrixStackTop];
	}


	public void render(Graphics g) {

		Geometry.g = g;

		g.setColor(Color.black);
		g.fillRect(0, 0, width, height);		
		g.setColor(Color.green);

		animate();

		// CLEAR THE MATRIX STACK FOR THIS ANIMATION FRAME

		mclear();
		
		double s = 0.04;
		m().scale(s, s, s);
		double translate_x = 13-duration;
		m().translate(translate_x, 0, 0);
		m().rotateX(duration);
		for(int i=0;i<balls.length;i++){
			m().rotateX(-Math.PI / balls.length);
			m().translate(1.3, 0.8, 0);
			m().scale(0.9, 0.9, 0.9);
			transformAndRender(balls[i], m());
		}
	}

	public void transformAndRender(Geometry geo, Matrix m){
		geo.matrix = m;
		geo.action();
	}

	public void animate(){
		duration = System.currentTimeMillis() / 1000.0 - startTime;
		if(duration > 60) {
			startTime = System.currentTimeMillis() / 1000.0;
		}
	}

}

