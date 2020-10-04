/*Ken Perlin: 
Creating a Material object:
You should create an object class Material to store material data:
For now, Material will have 10 phong algorithm values:
      Argb (ambient color, with 3 values)
      Drgb (diffuse color, with 3 values)
      Srgb (specular color, with 3 values)
      p (specular power, with 1 value)
plus, in the case of ray tracing, the "mirror color" mcrgb, with 3 values:
If mcrgb is black (that is, [0,0,0]), there is no mirror reflection.
If mcrgb is white (that is, [1,1,1]), the surface acts like a perfect mirror.
If mcrgb is red (that is, [1,0,0]), the surface acts like a mirror tinted red.

https://web.archive.org/web/20150915113252/http://mrl.nyu.edu/~perlin/courses/spring2012/0329.html
*/	
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