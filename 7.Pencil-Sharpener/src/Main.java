
/* Ken Perlin:
 * 
 * Homework 7, due before class, on Thursday April 12:
 * 
 * Your homework this week is to make a cool animated ray traced scene that contains more interesting shapes than just spheres.
 * 
 * You will need to implement ray tracing to transformed first order surfaces and to transformed second order surfaces. That means applying the same matrices you have been using all semester to the transform math I've described in the last section of these notes.
 * 
 * At the very least, create shapes that are the result of convex intersections, such as cylinders and cubes. This will allow you to use the simple intersection trick we discussed in class: taking the maximum of all "in" hits, and the minimum of all "out" hits.
 * 
 * For extra credit, implement the more general intersection algorithm that handles non-convex shapes. This will allow you to create far more interesting things.
 * 
 * For extra credit, implement refraction, atmospheric effects, sub-sampling for speed, and super-sampling for anti-aliasing.
 * 
 * https://web.archive.org/web/20150918171426/http://mrl.nyu.edu/~perlin/courses/spring2012/0405.html */

import java.util.ArrayList;

@SuppressWarnings("serial")
public class Main extends MISApplet {

	int nCols;
	int nRows;
	double focalLength = 10;
	double scale = 10;
	double epsilon = 0.1;

	double[] v = { 0, 0, focalLength };
	double[] w = new double[3];
	Light[] ls;
	double[] ray_rgb;

	Matrix mat1 = new Matrix();
	Matrix mat2 = new Matrix();
	Matrix p = new Matrix();
	double[] rgb_sum = new double[3];
	double grid;
	double xx, yy;

	boolean test = false;

	ArrayList<Shape> shapes = new ArrayList<Shape>();
	double[] colorReturn = new double[4];
	double[] surface_p = new double[3];
	double[] surface_n = new double[3];
	double[] reflection_v = new double[3];
	double[] phong = new double[3];
	Intersector inter = new Intersector();

	private double[] getRay(double[] v, double[] w) {

		colorReturn[0] = 0.0;
		colorReturn[1] = 0.0;
		colorReturn[2] = 0.0;
		colorReturn[3] = -1;

		inter.intersect(v, w, shapes);

		if (inter.intersect) {

			Material m = inter.front_sphere.m;

			for (int i = 0; i < 3; i++) {
				surface_p[i] = w[i] * inter.min_t + v[i];
			}

			surface_n[0] = 2 * inter.front_sphere.p_a * surface_p[0] + inter.front_sphere.p_e * surface_p[2]
					+ inter.front_sphere.p_f * surface_p[1] + inter.front_sphere.p_g;
			surface_n[1] = 2 * inter.front_sphere.p_b * surface_p[1] + inter.front_sphere.p_d * surface_p[2]
					+ inter.front_sphere.p_f * surface_p[0] + inter.front_sphere.p_h;
			surface_n[2] = 2 * inter.front_sphere.p_c * surface_p[2] + inter.front_sphere.p_d * surface_p[1]
					+ inter.front_sphere.p_e * surface_p[0] + inter.front_sphere.p_i;

			Vector.normalize(surface_n);

			for (int i = 0; i < 3; i++) {
				reflection_v[i] = w[i] - 2 * Vector.dot(surface_n, w) * surface_n[i];
				phong[i] = m.a_rgb[i];
			}

			for (Light l : ls) {

				for (int i = 0; i < 3; i++) {

					phong[i] += l.rgb[i] * (m.d_rgb[i] * Math.max(Vector.dot(l.xyz, surface_n), 0)
							+ m.s_rgb[i] * Math.max(Math.pow(Vector.dot(l.xyz, reflection_v), m.p), 0));
				}

			}

			for (int i = 0; i < 3; i++) {
				colorReturn[i] = phong[i] * (1.0 - m.mc_rgb[i]);
			}

			colorReturn[3] = inter.front_sphere.id;

		}

		return colorReturn;

	}

	public void initFrame(double time) { // INITIALIZE ONE ANIMATION FRAME
		for (int shape_id = 0; shape_id < shapes.size(); shape_id++) {
			ArrayList<Sphere> spheres = shapes.get(shape_id).spheres;
			for (int si = 0; si < spheres.size(); si++) {
				Sphere s = spheres.get(si);
				s.reset();

				mat1.identity();
				mat1.translate(s.c[0], s.c[1], s.c[2]);
//				mat1.rotateX(time);
				mat1.rotateY(time);
//				mat1.rotateZ(time);

				mat1.inverse();
				mat2.copy(mat1);

				mat1.transpose();

				p.set(0, 0, s.p_a);
				p.set(0, 1, s.p_f);
				p.set(0, 2, s.p_e);
				p.set(0, 3, s.p_g);

				p.set(1, 0, 0);
				p.set(1, 1, s.p_b);
				p.set(1, 2, s.p_d);
				p.set(1, 3, s.p_h);

				p.set(2, 0, 0);
				p.set(2, 1, 0);
				p.set(2, 2, s.p_c);
				p.set(2, 3, s.p_i);

				p.set(3, 0, 0);
				p.set(3, 1, 0);
				p.set(3, 2, 0);
				p.set(3, 3, s.p_j);

				mat1.multiply(p);
				mat1.multiply(mat2);

				s.p_a = mat1.get(0, 0);
				s.p_f = mat1.get(0, 1) + mat1.get(1, 0);
				s.p_e = mat1.get(0, 2) + mat1.get(2, 0);
				s.p_g = mat1.get(0, 3) + mat1.get(3, 0);

				s.p_b = mat1.get(1, 1);
				s.p_d = mat1.get(1, 2) + mat1.get(2, 1);
				s.p_h = mat1.get(1, 3) + mat1.get(3, 1);

				s.p_c = mat1.get(2, 2);
				s.p_i = mat1.get(2, 3) + mat1.get(3, 2);

				s.p_j = mat1.get(3, 3);

			}
		}

	}

	public void initialize() {

		// Argb (ambient color, with 3 values)
		// Drgb (diffuse color, with 3 values)
		// Srgb (specular color, with 3 values)
		double[][][] rgbs = { { { 66, 66, 66 }, { 66, 66, 66 }, { 255, 255, 255 } },
				{ { 0, 255, 0 }, { 0, 255, 0 }, { 255, 255, 255 } },
				{ { 33, 33, 33 }, { 33, 33, 33 }, { 255, 255, 255 } } };
		for (double[][] group : rgbs) {
			for (double[] color : group) {
				for (int i = 0; i < 3; i++) {
					color[i] /= 255.0;
				}
			}
		}

		Material red = new Material(rgbs[0], 100, 0.2);
		Material green = new Material(rgbs[1], 100, 0.01);
		Material blue = new Material(rgbs[2], 6, 0.15);

		ls = new Light[4];
		ls[0] = new Light(2, 2, 6, 0.7, 0.7, 0.7);
		ls[1] = new Light(-2, -2, -6, 0.7, 0.7, 0.7);
		ls[2] = new Light(2, -2, -6, 0.7, 0.7, 0.7);
		ls[3] = new Light(-2, 2, -6, 0.7, 0.7, 0.7);

		nCols = getWidth();
		nRows = getHeight();

		shapes.add(new Shape());
		shapes.get(shapes.size() - 1).spheres.add(new Sphere(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, -0.5, green, 0));
		shapes.get(shapes.size() - 1).spheres.add(new Sphere(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1, -2, green, 0));
		shapes.get(shapes.size() - 1).spheres.add(new Sphere(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, -2, green, 0));
		shapes.get(shapes.size() - 1).spheres.add(new Sphere(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1, 0, -2, green, 0));
		shapes.get(shapes.size() - 1).spheres.add(new Sphere(0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, -2, green, 0));
		shapes.get(shapes.size() - 1).spheres.add(new Sphere(0, 0, 0, 0, 0, 0, 0, 0, 0, -1, 0, 0, -2, green, 0));
		shapes.get(shapes.size() - 1).spheres.add(new Sphere(0, -1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, -0.1, blue, 3));
		shapes.get(0).spheres.add(new Sphere(0, 1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, -0.2, red, 3));

	}

	public void setPixel(int x, int y, int rgb[], int host[], boolean super_sampling) { // SET ONE PIXEL'S COLOR

		if (x == 255 && y == 255) {

			test = true;
		} else {
			test = false;
		}

		if (super_sampling) {
			grid = 2;
		} else {
			grid = 1;
		}

		rgb_sum[0] = rgb_sum[1] = rgb_sum[2] = 0;

		for (int i = 0; i < grid; i++) {
			for (int j = 0; j < grid; j++) {

				xx = x + j * 1 / grid;
				yy = y + i * 1 / grid;

				w[0] = (xx - 0.5 * nCols) / nCols * scale;
				w[1] = (0.5 * nRows - yy) / nCols * scale;
				w[2] = -focalLength;

				Vector.normalize(w);
				ray_rgb = getRay(v, w);

				rgb_sum[0] += ray_rgb[0];
				rgb_sum[1] += ray_rgb[1];
				rgb_sum[2] += ray_rgb[2];

			}
		}

		rgb_sum[0] /= (Math.pow(grid, 2));
		rgb_sum[1] /= (Math.pow(grid, 2));
		rgb_sum[2] /= (Math.pow(grid, 2));

		rgb[0] = (int) Math.max(Math.min(rgb_sum[0] * 255, 255), 0);
		rgb[1] = (int) Math.max(Math.min(rgb_sum[1] * 255, 255), 0);
		rgb[2] = (int) Math.max(Math.min(rgb_sum[2] * 255, 255), 0);

		host[0] = (int) ray_rgb[3];

	}

}