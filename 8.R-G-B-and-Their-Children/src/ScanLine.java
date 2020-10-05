// Linear interpolation

/*
Ken Perlin:

Homework 8 due Thursday April 26

For your homework, which is due next Thursday, you should at least demonstrate that you can split a triangle into two trapezoids, and then apply the above math to interpolate [r,g,b,pz] values down to the pixel level. To do this, I suggest you write a Java applet that extends MISApplet (which is what you did for ray tracing), and show that your applet correctly interpolates given any choices of locations [x,y] and colors [r,g,b] at each of the three triangle vertices.

https://web.archive.org/web/20201005105230/https://mrl.nyu.edu/~perlin/courses/spring2012/scan-conversion.html

*/


@SuppressWarnings("serial")
public class ScanLine extends MISApplet {

	// three corners of a triangle
	double[] a = new double[6];
	double[] b = new double[6];
	double[] c = new double[6];

	public void initialize() {

		a[0] = 0.28 * W;
		a[1] = 0.20 * H;
		a[2] = 0;
		a[3] = 255;
		a[4] = 0;
		a[5] = 0;

		b[0] = 0.51 * W;
		b[1] = 0.92 * H;
		b[2] = 0;
		b[3] = 0;
		b[4] = 255;
		b[5] = 0;

		c[0] = 0.88 * W;
		c[1] = 0.65 * H;
		c[2] = 0;
		c[3] = 0;
		c[4] = 0;
		c[5] = 255;

	}

	public void initFrame(double time) { // INITIALIZE ONE ANIMATION FRAME

	}

	private double[] lerp(double t, double[] start, double[] end) {
		double[] output = new double[6];

		for (int i = 0; i < output.length; i++) {
			output[i] = (end[i] - start[i]) * t + start[i];
		}
		return output;

	}

	private int[] getRGB(int x, int y, double[] tl, double[] bl, double[] tr,
			double[] br) {
		int rgb[] = new int[3];
		rgb[0] = rgb[1] = rgb[2] = 0;

		// COMPUTE TOP-TO-BOTTOM INTERPOLATION FRACTION FOR THIS SCAN LINE

		double tly = tl[1];
		double bly = bl[1];
		double ty = (y - tly) / (bly - tly);
		if (ty >= 0 && ty <= 1) {

			// LINEARLY INTERPOLATE TO GET VALUES AT LEFT AND RIGHT EDGES OF
			// THIS
			// SCAN LINE

			double[] l = lerp(ty, tl, bl);
			double[] r = lerp(ty, tr, br);

			// COMPUTE LEFT-TO-RIGHT INTERPOLATION FRACTION OF THE PIXEL

			double lx = l[0];
			double rx = r[0];
			double tx = (x - lx) / (rx - lx);
			if (tx >= 0 && tx <= 1) {
				// LINEARLY INTERPOLATE TO GET VALUE AT PIXEL

				double[] value = lerp(tx, l, r);

				rgb[0] = (int) value[3];
				rgb[1] = (int) value[4];
				rgb[2] = (int) value[5];
			}

		}

		return rgb;
	}

	public void setPixel(int x, int y, int rgb[]) { // SET ONE PIXEL'S COLOR

		double y_mid = (c[1] - a[1]) / (b[1] - a[1]);
		double[] tl, bl, tr, br;
		if (y < c[1]) {

			tl = a;
			bl = lerp(y_mid, a, b);
			tr = a;
			br = c;
		} else {
			tl = lerp(y_mid, a, b);
			bl = b;
			tr = c;
			br = b;
		}
		int[] color = getRGB(x, y, tl, bl, tr, br);
		rgb[0] = color[0];
		rgb[1] = color[1];
		rgb[2] = color[2];

	}
}
