import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;
@SuppressWarnings("serial")
public class Stage extends BufferedApplet
{

	int width = 0, height = 0;
	double x=0,y=0,z=0;
	double startTime = System.currentTimeMillis() / 1000.0;
	double timeStarted = 0, sint=0;
	Matrix matrix = new Matrix();
	Geometry shape;
	Random rnd = new Random();

	final static int MATRIX_STACK_SIZE = 100;
	int matrixStackTop = 0;
	Matrix matrixStack[] = new Matrix[MATRIX_STACK_SIZE];
	{
		for (int n = 0 ; n < MATRIX_STACK_SIZE ; n++)
			matrixStack[n] = new Matrix();
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

		g.setColor(Color.white);
		g.fillRect(0, 0, width, height);		
		g.setColor(Color.blue);



		animate();

		// CLEAR THE MATRIX STACK FOR THIS ANIMATION FRAME

		mclear();
		mpush();
		m().scale(0.04, 0.04, 0.04);
		m().translate(-timeStarted, 0, 0);
		m().rotateX(timeStarted);
		for(int i=0;i<balls.length;i++){
			mpush();
			m().rotateX(-Math.PI / balls.length);
			m().translate(1.3, 0.8, 0);
			m().scale(0.9, 0.9, 0.9);
			transformAndRender(balls[i], m());
		}
		for(int i=0;i<balls.length;i++){
			mpop();
		}
		mpop();


	}


	public void transformAndRender(Geometry geo, Matrix m){
		geo.matrix = m;
		geo.action();
	}


	public void animate(){
		timeStarted = System.currentTimeMillis() / 1000.0 - startTime;
	}

}

