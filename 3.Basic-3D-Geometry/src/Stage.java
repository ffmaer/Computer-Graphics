import java.awt.*;
import java.util.Random;
@SuppressWarnings("serial")
public class Stage extends BufferedApplet
{






	int width = 0, height = 0;


	double startTime = System.currentTimeMillis() / 1000.0;
	double t = 0, sint=0;
	Matrix matrix = new Matrix();
	Geometry shape;
	Random rnd = new Random();


	public void render(Graphics g) {
		width = getWidth();
		height = getHeight();

		g.setColor(Color.black);
		g.fillRect(0, 0, width, height);		



		animate();


// cube 1
		matrix.identity();
		matrix.rotateX(t);
		matrix.rotateY(t);
		matrix.rotateZ(t);
	    matrix.translate(.3+Math.sin(t)/10, 0, 0);
		matrix.scale(.1, .1, .1);
		g.setColor(Color.green);
		shape = new Geometry( matrix,  g,  height,  width, 4, 10);
		shape.cylinder();
		
// cube 2
		matrix.identity();
		matrix.rotateX(t);
		matrix.rotateY(t);
		matrix.rotateZ(t);
	    matrix.translate(-.3-Math.sin(t)/10, 0, 0);
		matrix.scale(.1, .1, .1);
		g.setColor(Color.green);
		shape = new Geometry( matrix,  g,  height,  width, 4, 10);
		shape.cylinder();
		
// cube 3
		matrix.identity();
		matrix.rotateX(t);
		matrix.rotateY(t);
		matrix.rotateZ(t);
	    matrix.translate(0, .3+Math.sin(t)/10, 0);
		matrix.scale(.1, .1, .1);
		g.setColor(Color.green);
		shape = new Geometry( matrix,  g,  height,  width, 4, 10);
		shape.cylinder();

// cube 4		
		matrix.identity();
		matrix.rotateX(t);
		matrix.rotateY(t);
		matrix.rotateZ(t);
	    matrix.translate(0, -.3-Math.sin(t)/10, 0);
		matrix.scale(.1, .1, .1);
		g.setColor(Color.green);
		shape = new Geometry( matrix,  g,  height,  width, 4, 10);
		shape.cylinder();
		
// the long cylinder
		matrix.identity();
		matrix.rotateX(t);
		matrix.rotateY(t);
		matrix.rotateZ(t);
	    matrix.translate(0, 0, 0);
		matrix.scale(.05, .05, 1);
		g.setColor(Color.green);
		shape = new Geometry( matrix,  g,  height,  width, 20, 1);
		shape.cylinder();

// the center sphere
		matrix.identity();
		matrix.rotateX(t);
		matrix.rotateY(t);
		matrix.rotateZ(t);
		matrix.scale(.2, .2, .2);
		g.setColor(Color.green);
		shape = new Geometry( matrix,  g,  height,  width, 50, 25);
		shape.sphere();


		
	}



	public void animate(){
		t = System.currentTimeMillis() / 1000.0 - startTime;
	}

}

