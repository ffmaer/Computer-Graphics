public class Light{
	double xyz[] = new double[3];
	double rgb[] = new double[3];
	
	Light(double[] xyz, double[] rgb){

		System.arraycopy(xyz,0,this.xyz,0,3);
		System.arraycopy(rgb,0,this.rgb,0,3);
	}
}