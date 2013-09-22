import java.awt.*;
public class Geometry 
{

	Matrix matrix;
	static Graphics g;
	static int height, width;
	int m,n;
	double f = 1;

	double vertices[][];
	int faces[][];

	double a[] = {0,0,0}, b[] = {0,0,0};
	int pa[] = {0,0}, pb[] = {0,0};	

	int shape;

	public Geometry(){

	}
	


	public void generate_faces(){
		faces = new int[m*n][4];

		int counter = 0;

		for(int j=0;j<n;j++){
			for(int i=0;i<m;i++){
				int p1=i+(m+1)*j;
				int p2=i+1+(m+1)*j;
				int p3=i+1+(m+1)*(j+1);
				int p4=i+(m+1)*(j+1);

				int[] face = {p1,p2,p3,p4};
				faces[counter] = face;
				counter++;

			}
		}

	}

	public void global(){
		generate_faces();
		draw();
	}
	public void sphere(){
		vertices = new double[(m+1)*(n+1)][4];

		for(int j=0;j<=n;j++){
			for(int i=0;i<=m;i++){
				double theta1 = (double)i/(double)m * 2 * Math.PI;
				double theta2 = -Math.PI/2 + (double)j/(double)n * Math.PI;
				int index = i+(m+1)*j;

				double x = Math.cos(theta1)*Math.cos(theta2);
				double y = Math.sin(theta1)*Math.cos(theta2);
				double z = Math.sin(theta2);
				double[] xyz = {x,y,z,1};

				vertices[index] = xyz;
			}
		}
		global();
	}

	public void cylinder_top(){

		vertices = new double[(m+1)*(n+1)][4];

		for(int j=0;j<=n;j++){
			for(int i=0;i<=m;i++){
				double theta1 = (double)i/(double)m * 2 * Math.PI;
				double r = (double)j/(double)n * 1;

				int index = i+(m+1)*j;


				double x = Math.cos(theta1) * r;
				double y = Math.sin(theta1) * r;
				double[] xyz = {x,y,1,1};

				vertices[index] = xyz;


			}
		}
		global();
	}
	
	
	public void cylinder_bottom(){

		vertices = new double[(m+1)*(n+1)][4];

		for(int j=0;j<=n;j++){
			for(int i=0;i<=m;i++){
				double theta1 = (double)i/(double)m * 2 * Math.PI;
				double r = (double)j/(double)n * 1;

				int index = i+(m+1)*j;


				double x = Math.cos(theta1) * r;
				double y = Math.sin(theta1) * r;
				double[] xyz = {x,y,-1,1};

				vertices[index] = xyz;


			}
		}
		global();
	}

	public void cylinder_center(){
		vertices = new double[(m+1)*(n+1)][4];

		for(int j=0;j<=n;j++){
			for(int i=0;i<=m;i++){
				double theta1 = (double)i/(double)m * 2 * Math.PI;
				double z = -1 + (double)j/(double)n * 2;

				int index = i+(m+1)*j;

				double x = Math.cos(theta1);
				double y = Math.sin(theta1);
				double[] xyz = {x,y,z,1};

				vertices[index] = xyz;
			}
		}
		global();

	}

	public void action(){
		// cylinder
		if(shape == 0){
			cylinder_bottom();
			cylinder_center();
			cylinder_top();
		}else if(shape == 1){
			sphere();
		}
	}

	public void globe(int m,int n){
		this.m = m;
		this.n = n;
		this.shape = 1;
	}
	public void cylinder(int m){
		this.m = m;
		this.n = 1;
		this.shape = 0;
	}

	public void draw(){
		
		for( int j = 0; j < faces.length; j++ ){// LOOP THROUGH ALL THE LINES IN THE SHAPE
			for(int i = 0;i<faces[j].length;i++){
				matrix.transform(vertices[faces[j][i]], a);				 // TRANSFORM BOTH ENDPOINTS OF LINE
				matrix.transform(vertices[faces[j][(i+1) % faces[j].length]], b);
				viewport(a, pa);
				viewport(b, pb);
				g.drawLine(pa[0], pa[1], pb[0], pb[1]);	 // DRAW ONE LINE ON THE SCREEN
			}
		}
	}

	public void viewport(double src[], int dst[]) {

		dst[0] = (int) ( ( 0.5 * width  + f*src[0] / (f-src[2]) * width ));
		dst[1] = (int) ( ( 0.5 * height - f*src[1] / (f-src[2]) * width ));
		
		
	}

}

