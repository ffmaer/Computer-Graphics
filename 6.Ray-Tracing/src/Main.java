
/* Ken Perlin:
 * Homework 6, due Thursday April 5, before class starts:
 * Implement a simple ray tracer, as per the above notes. 
 * You should create an original scene consisting of spheres.
 * Your scene should demonstrate both Phong shading and mirror reflection.
 * https://web.archive.org/web/20150915113252/http://mrl.nyu.edu/~perlin/courses/spring2012/0329.html */

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

@SuppressWarnings("serial")
public class Main extends BufferedApplet {

	Random rnd = new Random();

	int nCols;
	int nRows;
	double focalLength = 10;
	double scale = 10;
	double epsilon = 0.1;

	Sphere[] spheres = new Sphere[3];

	public double square(double[] vec) {
		double square = 0;
		for (int i = 0; i < 3; i++)
			square += Math.pow(vec[i], 2);
		return square;
	}

	public double norm(double vec[]) {
		double norm = Math.sqrt(square(vec));
		return norm;
	}

	public void normalize(double vec[]) {
		double norm = norm(vec);

		for (int i = 0; i < 3; i++)
			vec[i] /= norm;

	}

	public double[] minus(double a[], double b[]) {
		double[] c = new double[3];
		for (int i = 0; i < 3; i++)
			c[i] = a[i] - b[i];
		return c;
	}

	public double dot(double a[], double b[]) {
		double dot = 0;
		for (int i = 0; i < 3; i++)
			dot += a[i] * b[i];
		return dot;
	}

	double[] v = { 0, 0, focalLength };
	double[] w = new double[3];
	double[] c = new double[3];
	double r;
	double[] surface_p = new double[3];
	double[] surface_n = new double[3];
	double[] reflection_v = new double[3];

	Light[] ls = new Light[1];
	double l_xyz[] = new double[3];
	double l_rgb[] = new double[3];

	double wv_c, t1, t2, t, delta;
	double[] v_c;
	double xyz[] = new double[3];

	public static int shadow_on = 1;

	public void init() {

		addKeyListener(this);

		// double [][][] rgbs = {
		// {{228.0/255.0,229.0/255.0,231.0/255.0},{197.0/255.0,198.0/255.0,200.0/255.0},{255.0/255.0,255.0/255.0,255.0/255.0}},
		// {{255.0/255.0,200.0/255.0,100.0/255.0},{70.0/255.0,40.0/255.0,255.0/255.0},{255.0/255.0,240.0/255.0,215.0/255.0}},
		// {{255.0/255.0,214.0/255.0,196.0/255.0},{225.0/255.0,87.0/255.0,51.0/255.0},{255.0/255.0,214.0/255.0,196.0/255.0}}
		// };

		double[][][] rgbs = {
				{ { 228.0 / 255.0, 28.0 / 255.0, 28.0 / 255.0 }, { 197.0 / 255.0, 198.0 / 255.0, 200.0 / 255.0 },
						{ 255.0 / 255.0, 255.0 / 255.0, 255.0 / 255.0 } },
				{ { 10.0 / 255.0, 200.0 / 255.0, 10.0 / 255.0 }, { 70.0 / 255.0, 40.0 / 255.0, 255.0 / 255.0 },
						{ 255.0 / 255.0, 240.0 / 255.0, 215.0 / 255.0 } },
				{ { 26.0 / 255.0, 26.0 / 255.0, 196.0 / 255.0 }, { 225.0 / 255.0, 87.0 / 255.0, 51.0 / 255.0 },
						{ 255.0 / 255.0, 214.0 / 255.0, 196.0 / 255.0 } } };

		// light 1
		l_xyz[0] = 10;
		l_xyz[1] = 0;
		l_xyz[2] = 10;
		normalize(l_xyz);
		l_rgb[0] = 0.7;
		l_rgb[1] = 0.7;
		l_rgb[2] = 0.7;
		ls[0] = new Light(l_xyz, l_rgb);

		// // light 2
		// l_xyz[0]=-10;
		// l_xyz[1]=-20;
		// l_xyz[2]=-10;
		// normalize(l_xyz);
		// l_rgb[0] = 0.7;
		// l_rgb[1] = 0.7;
		// l_rgb[2] = 0.7;
		// ls[1] = new Light(l_xyz,l_rgb);

		nCols = getWidth();
		nRows = getHeight();

		// sphere 1
		xyz[0] = -1;
		xyz[1] = 0;
		xyz[2] = -1;
		spheres[0] = new Sphere(xyz, 1, new Material(rgbs[0], 1, 0.2));

		// sphere 2
		xyz[0] = 0;
		xyz[1] = 0;
		xyz[2] = 0;
		spheres[1] = new Sphere(xyz, 1, new Material(rgbs[1], 100, 0.01));

		// sphere 3
		xyz[0] = 1;
		xyz[1] = 0;
		xyz[2] = 1;
		spheres[2] = new Sphere(xyz, 1, new Material(rgbs[2], 6, 0.15));
	}

	boolean intersect;
	boolean shadow;

	double tnear;
	int nearsi;

	double[] zero_vector = { 0, 0, 0 };

	double newv[] = new double[3];
	double neww[] = new double[3];

	double[] colorReturn = new double[3];
	double[] reflection = new double[3];

	double term1, term2;
	Material m;

	public double[] getRay(double[] v, double[] w) {
		double[] phong = new double[3];

		intersect = false;
		tnear = 999999999;

		for (int si = 0; si < spheres.length; si++) {

			c = spheres[si].c;
			r = spheres[si].r;

			v_c = minus(v, c);
			wv_c = dot(w, v_c);

			delta = Math.pow(wv_c, 2) - square(v_c) + Math.pow(r, 2);

			if (delta > 0) {
				t = -wv_c - Math.sqrt(delta);
				if (t > 0) {
					intersect = true;
				}
				if (t > 0 && t < tnear) {
					nearsi = si;
					tnear = t;
				}
			}
		}

		if (!intersect) {
			colorReturn[0] = 0.0;
			colorReturn[1] = 0.0;
			colorReturn[2] = 0.0;
		} else {
			c = spheres[nearsi].c;
			r = spheres[nearsi].r;

			t = tnear;

			m = spheres[nearsi].m;

			for (int i = 0; i < 3; i++) {
				surface_p[i] = w[i] * t + v[i];
				surface_n[i] = (surface_p[i] - c[i]) / r;
			}

			for (int i = 0; i < 3; i++) {
				reflection_v[i] = w[i] - 2 * dot(surface_n, w) * surface_n[i];
				phong[i] = m.a_rgb[i];

			}

			for (Light l : ls) {
				shadow = false;

				if (shadow_on == 1) {
					v = surface_p;
					w = l.xyz;

					for (int si = 0; si < spheres.length; si++) {

						if (si != nearsi) {
							c = spheres[si].c;
							r = spheres[si].r;

							v_c = minus(v, c);
							wv_c = dot(w, v_c);

							delta = Math.pow(wv_c, 2) - square(v_c) + Math.pow(r, 2);

							if (delta > 0) {
								t = -wv_c - Math.sqrt(delta);
								if (t > 0) {
									shadow = true;
									break;
								}
							}
						}

					}
				}

				if (!shadow) {
					for (int i = 0; i < 3; i++) {
						term1 = Math.max(dot(l_xyz, surface_n), 0);
						term2 = Math.max(Math.pow(dot(l.xyz, reflection_v), m.p), 0);

						phong[i] += l.rgb[i] * (m.d_rgb[i] * term1 + m.s_rgb[i] * term2);
					}
				}

			}

			for (int i = 0; i < 3; i++) {
				newv[i] = surface_p[i] + epsilon * reflection_v[i];
				neww[i] = reflection_v[i];
			}

			normalize(neww);

			reflection = getRay(newv, neww);

			for (int i = 0; i < 3; i++) {

				colorReturn[i] = phong[i] * (1.0 - m.mc_rgb[i]) + reflection[i] * m.mc_rgb[i];
			}
		}
		return colorReturn;
	}

	double[] rgb = new double[3];
	Color drawRGB;
	float red, green, blue;
	int num;

	OpenSimplex2S[] open_simplexes = new OpenSimplex2S[] {
			new OpenSimplex2S(rnd.nextLong()),new OpenSimplex2S(rnd.nextLong()),new OpenSimplex2S(rnd.nextLong())
	};
	

	public void render(Graphics g) {
		secondsPassed = System.currentTimeMillis() / 1000.0 - startTime;
		for(int i=0;i<spheres.length;i++) {
			spheres[i].c[1] = open_simplexes[i].noise2(secondsPassed, secondsPassed) * 2;
		}

		g.setColor(Color.black);
		g.fillRect(0, 0, nCols, nRows);

		for (int row = 0; row < nRows; row++) {

			for (int col = 0; col < nCols; col++) {

				w[0] = (col - 0.5 * nCols) / nCols * scale;
				w[1] = (0.5 * nRows - row) / nCols * scale;
				w[2] = -focalLength;
				normalize(w);

				rgb = getRay(v, w);
				red = (float) Math.max(Math.min(rgb[0], 1), 0);
				green = (float) Math.max(Math.min(rgb[1], 1), 0);
				blue = (float) Math.max(Math.min(rgb[2], 1), 0);

				drawRGB = new Color(red, green, blue);
				g.setColor(drawRGB);
				g.drawRect(col, row, 1, 1);

			}
		}
	}

	double startTime = System.currentTimeMillis() / 1000.0;
	double secondsPassed = 0;

}