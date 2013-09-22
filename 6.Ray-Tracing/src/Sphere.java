public class Sphere{
	
	double[] c = new double[3];	
	double r;
	Material m;
	
	Sphere(double[] xyz, double r, Material m){
		System.arraycopy(xyz,0,this.c,0,3);
		
		this.r = r;
		this.m = m;
	}
	
}