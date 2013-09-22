public class Material{
	double[] a_rgb = new double[3];
	double[] d_rgb = new double[3];
	double[] s_rgb = new double[3];
	double[] mc_rgb = new double[3];
	int p;
	
	Material(double[][] a, int p, double mc){
		System.arraycopy(a[0],0,this.a_rgb,0,3);
		System.arraycopy(a[1],0,this.d_rgb,0,3);
		System.arraycopy(a[2],0,this.s_rgb,0,3);
		 
		this.p = p;
		
		this.mc_rgb[0] = mc;
		this.mc_rgb[1] = mc;
		this.mc_rgb[2] = mc;
		
	}
	
	
}