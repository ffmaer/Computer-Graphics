public class Light{
	double xyz[] = new double[3];
	double rgb[] = new double[3];
	
	Light(double x, double y, double z, double r, double g, double b){

		this.xyz[0]=x;
		this.xyz[1]=y;
		this.xyz[2]=z;
			
		Vector.normalize(this.xyz);
		
		this.rgb[0]=r;
		this.rgb[1]=g;
		this.rgb[2]=b;

	}
}