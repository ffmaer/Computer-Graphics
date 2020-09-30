import java.awt.*;
import java.util.Random;

// It's a person jumping around.
// It's about hierarchy among components.

@SuppressWarnings("serial")
public class Stage extends BufferedApplet
{

	int width, height; // canvas width and height

	double startTime = System.currentTimeMillis() / 1000.0;
	double timePassed = 0, sint=0;
	int timePassedMiliseconds = 0;
	Matrix m = new Matrix();
	Random rnd = new Random();

	Geometry world,neck,person,l_shoulder,r_shoulder,l_joint,r_joint,mover;
	Geometry body,head,l_leg,r_leg,l_arm,r_arm;


	
	public void init(){


		width = getWidth();
		height = getHeight();

		Geometry.height = height;
		Geometry.width = width;

		world=shape();
		mover=shape();
		neck=shape();
		person=shape();
		l_shoulder=shape();
		r_shoulder=shape();
		l_joint=shape();
		r_joint=shape();
		
		
		
		body=shape();
		head=shape();
		l_leg=shape();
		r_leg=shape();
		l_arm=shape();
		r_arm=shape();
		
		
		
		body.cylinder(10);
		head.globe(10,5);
		l_leg.cylinder(7);
		r_leg.cylinder(7);
		l_arm.cylinder(7);
		r_arm.cylinder(7);
		


		world.add(mover);
		mover.add(person);
		person.add(body);
		person.add(neck);
		person.add(l_shoulder);
		person.add(r_shoulder);
		person.add(l_joint);
		person.add(r_joint);
		
		l_shoulder.add(l_arm);
		r_shoulder.add(r_arm);
		l_joint.add(l_leg);
		r_joint.add(r_leg);
		
		neck.add(head);
		
		m = person.matrix;
		m.rotateX(Math.PI/6);	
		m.scale(0.05,0.05,0.05);
	
		m = body.matrix;
		m.scale(0.5,1,1);
		m.rotateX(Math.PI/2);	
	
		m = head.matrix;
		m.translate(0,1.2,0);	
		m.scale(0.3,0.3,0.3);
		
		m = l_shoulder.matrix;
		m.translate(0,1,-1);	
		m.rotateX(Math.PI/10);
		
		m = r_shoulder.matrix;
		m.translate(0,1,1);	
		m.rotateX(-Math.PI/10);
		
		
		m = l_arm.matrix;
		m.translate(0,1.5,0);	
		m.scale(0.2,1.5,0.2);
		m.rotateX(Math.PI/2);
			
		m = r_arm.matrix;
		m.translate(0,1.5,0);	
		m.scale(0.2,1.5,0.2);
		m.rotateX(Math.PI/2);	
		
		
		m = l_leg.matrix;
		m.translate(0,-2,-0.3);	
		m.scale(0.3,0.8,0.3);
		m.rotateX(Math.PI/2);	
		
		m = r_leg.matrix;
		m.translate(0,-2,0.3);	
		m.scale(0.3,0.8,0.3);
		m.rotateX(Math.PI/2);
		

	}


	public Geometry shape(){
		Geometry geo = new Geometry();
		return geo;
	}


	double pair_x[][] = { 	{0,0}, {0.25,-0.5}, {0.5,0.5}, {0.75,0},
							{1,0}, {1.25,-0.4}, {1.5,0.4}, {1.75,0}, 
							{2,0}, {2.25,-0.3}, {2.5,0.3}, {2.75,0}, 
							{3,0}, {3.25,-0.2}, {3.5,0.2}, {3.75,0}, 
							{4,0}, {4.25,-0.1}, {4.5,0.1}, {4.75,0}};




	double p1,p4,r1,r4; // for spline()
	double time;
	double slope_at_timek,slope_at_timek1;

	double wave = 1.5;

	int counter = -1;
	int direction = 1; // 1 or -1, move left or move right
	public void render(Graphics g) {
		
		Geometry.g = g;
		if(counter == 0) {
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, width, height);	
		}
	
		g.setColor(Color.GREEN);
		
		double x = spline(pair_x);
		
		animate();



		m = mover.matrix;
		m.identity();
		m.translate(x,0,0);
		
		m = person.matrix;
		m.rotateY(0.05);
		
		m = l_shoulder.matrix;

		
		if(counter < 20){
			m = r_shoulder.matrix;
			m.rotateX(direction * 0.1*wave);
			
			m = l_shoulder.matrix;
			m.rotateX(-direction * 0.1*wave);
			counter++;
			
			m = person.matrix;
			m.translate(0,-direction * 0.1*wave,0);
			
		}
		else
		{
			counter = 0;
			direction = direction * (-1);
		}



		world.root_matrix.identity();
		world.traverse();

	}
	public double spline(double pair[][]){
		
		double value = 0;
		int n = pair.length-1;
		
		int k = (int)timePassed;



		if(k<n){
			double tt = timePassed-k;

			if(k==0){
				slope_at_timek = 2 * (pair[1][1] - pair[0][1])/(pair[1][0]-pair[0][0]);

			}
			else if(k==n){
				slope_at_timek = (pair[n][1] - pair[n-1][1])/(pair[n][0]-pair[n-1][0]);

			}
			else{
				slope_at_timek = (pair[k+1][1] - pair[k-1][1])/(pair[k+1][0]-pair[k-1][0]);
			}

			if((k+1)==n){
				slope_at_timek1 = (pair[n][1] - pair[n-1][1])/(pair[n][0]-pair[n-1][0]);	
			}
			else{
				slope_at_timek1 = (pair[k+1+1][1] - pair[k+1-1][1])/(pair[k+1+1][0]-pair[k+1-1][0]);
			}



			p1=pair[k][1];
			p4=pair[k+1][1];
			r1=slope_at_timek*(pair[k+1][0]-pair[k][0]);
			r4=slope_at_timek1*(pair[k+1][0]-pair[k][0]);

			time = pair[k][0] + tt * (pair[k+1][0]-pair[k][0]);


			value = p1 *(2*Math.pow(tt,3)-3*Math.pow(tt,2)+1) + 
				p4* (-2*Math.pow(tt,3)+3*Math.pow(tt,2)) + 
				r1* (Math.pow(tt,3)-2*Math.pow(tt,2)+tt) + 
				r4* (Math.pow(tt,3)-Math.pow(tt,2));
		}
		
		return value;
		
	}

	public void animate(){
		timePassed = System.currentTimeMillis() / 1000.0 - startTime;
	}

}

