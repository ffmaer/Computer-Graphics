import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;

public class Geometry {
	static Graphics g;
	static int height;
	static int width;
	
	Matrix matrix;
	Matrix root_matrix;
	Matrix root_matrix_2;

	int m, n;
	double f = 1;

	double vertices[][];
	double transformed_vertices[][];

	double normal[][];
	double transformed_normal[][];

	int faces[][];

	double a[] = { 0, 0, 0 }, b[] = { 0, 0, 0 };
	int pa[] = { 0, 0 }, pb[] = { 0, 0 };

	int shape = 999;

	ArrayList<Geometry> children = new ArrayList<Geometry>();

	double[][][] rgbs = { { { 228 , 28 , 28  },{ 197 , 198 , 200  },{ 255 , 255 , 255  } },
			{ { 10 , 200 , 10  },{ 70 , 40 , 255  },{ 255 , 240 , 215  } },
			{ { 26 , 26 , 196  },{ 51 , 87 , 255  },{ 255 , 214 , 196  } } };

	Material red,green,blue;

	Material selected_material;

	double[] surface_n = new double[3];
	double[] phong = new double[3];

	Light[] ls;
	float zbuffer[][][];
	
	public Geometry(int m, int n) {
		
		for (double[][] group : rgbs) {
			for (double[] color : group) {
				for (int i = 0; i < 3; i++) {
					color[i] /= 255.0;
				}
			}
		};
		
		red = new Material(rgbs[0], 100, 0.2);
		green = new Material(rgbs[1], 100, 0.01);
		blue = new Material(rgbs[2], 6, 0.15);
		selected_material = blue; // you can change the color to red/gree/blue
		
		ls = new Light[1];
		matrix = new Matrix();
		matrix.identity();
		root_matrix = new Matrix();
		root_matrix_2 = new Matrix();
		root_matrix.identity();

		ls[0] = new Light(2, 2, 6, 0.7, 0.7, 0.7); // x y z r g b

		zbuffer = new float[height][width][4];

		vertices = new double[(m + 1) * (n + 1)][4];
		normal = new double[(m + 1) * (n + 1)][4];

		transformed_vertices = new double[(m + 1) * (n + 1)][6];
		transformed_normal = new double[(m + 1) * (n + 1)][4];
		faces = new int[m * n][4];

		this.m = m;
		this.n = n;
	}
	public void add(Geometry child) {
		children.add(child);
	}

	public void remove(Geometry child) {
		children.remove(child);
	}

	public int getNumChildren() {
		return children.size();
	}

	public Geometry getChild(int index) {
		return children.get(index);
	}

	

	public void traverse() {
		this.root_matrix.multiply(this.matrix);
		this.action();

		if (this.getNumChildren() > 0) {
			Iterator<Geometry> iter = children.iterator();
			while (iter.hasNext()) {
				Geometry child = iter.next();
				child.root_matrix.identity();
				child.root_matrix.multiply(this.root_matrix);
				child.traverse();
			}
		}
	}

	public void generate_faces() {

		int counter = 0;

		for (int j = 0; j < n; j++) {
			for (int i = 0; i < m; i++) {
				int p1 = i + (m + 1) * j;
				int p2 = i + 1 + (m + 1) * j;
				int p3 = i + 1 + (m + 1) * (j + 1);
				int p4 = i + (m + 1) * (j + 1);

				int[] face = { p1, p2, p3, p4 };
				faces[counter] = face;
				counter++;

			}
		}
	}

	public void global() {

		generate_faces();
		draw();

	}

	public void sphere() {

		for (int j = 0; j <= n; j++) {
			for (int i = 0; i <= m; i++) {
				double theta1 = (double) i / (double) m * 2 * Math.PI;
				double theta2 = -Math.PI / 2 + (double) j / (double) n
						* Math.PI;
				int index = i + (m + 1) * j;

				double x = Math.cos(theta1) * Math.cos(theta2);
				double y = Math.sin(theta1) * Math.cos(theta2);
				double z = Math.sin(theta2);
				double[] xyz = { x, y, z, 1 };

				vertices[index] = xyz;

				double[] nxyz = { x, y, z, 1 };
				normal[index] = nxyz;
			}
		}
		global();
	}

	public void cylinder_top() {

		for (int j = 0; j <= n; j++) {
			for (int i = 0; i <= m; i++) {
				double theta1 = (double) i / (double) m * 2 * Math.PI;
				double r = (double) j / (double) n * 1;

				int index = i + (m + 1) * j;

				double x = Math.cos(theta1) * r;
				double y = Math.sin(theta1) * r;
				double[] xyz = { x, y, 1, 1 };
				vertices[index] = xyz;

				double[] nxyz = { 0, 0, 1, 1 };
				normal[index] = nxyz;

			}
		}
		global();
	}

	public void cylinder_bottom() {

		for (int j = 0; j <= n; j++) {
			for (int i = 0; i <= m; i++) {
				double theta1 = (double) i / (double) m * 2 * Math.PI;
				double r = (double) j / (double) n * 1;

				int index = i + (m + 1) * j;

				double x = Math.cos(theta1) * r;
				double y = Math.sin(theta1) * r;
				double[] xyz = { x, y, -1, 1 };
				vertices[index] = xyz;

				double[] nxyz = { 0, 0, -1, 1 };
				normal[index] = nxyz;

			}
		}
		global();
	}

	public void cylinder_center() {

		for (int j = 0; j <= n; j++) {
			for (int i = 0; i <= m; i++) {
				double theta1 = (double) i / (double) m * 2 * Math.PI;
				double z = -1 + (double) j / (double) n * 2;

				int index = i + (m + 1) * j;

				double x = Math.cos(theta1);
				double y = Math.sin(theta1);
				double[] xyz = { x, y, z, 1 };
				vertices[index] = xyz;

				double[] nxyz = { x, y, 0, 1 };
				normal[index] = nxyz;
			}
		}
		global();

	}

	public void action() {

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				zbuffer[x][y][0] = -9999;

				for (int i = 1; i < 4; i++) {
					zbuffer[x][y][i] = 0;
				}
			}
		}

		if (shape == 0) {
			cylinder_bottom();
			cylinder_center();
			cylinder_top();
		} else if (shape == 1) {
			sphere();
		}

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				Color c = new Color(zbuffer[x][y][1], zbuffer[x][y][2],
						zbuffer[x][y][3]);
				g.setColor(c);
				g.drawRect(x, y, 1, 1);
			}
		}
	}

	public void globe() {

		this.shape = 1;
	}

	public void cylinder() {

		this.shape = 0;
	}


	public void draw() {

		root_matrix_2.copy(root_matrix);
		root_matrix_2.inverse();
		root_matrix_2.transpose();

		for (int j = 0; j < faces.length; j++) {// LOOP THROUGH ALL THE LINES IN
												// THE SHAPE
			for (int i = 0; i < faces[j].length; i++) {
				root_matrix.transform(vertices[faces[j][i]], a); // TRANSFORM
																	// BOTH
																	// ENDPOINTS
																	// OF LINE
				root_matrix.transform(vertices[faces[j][(i + 1)
						% faces[j].length]], b);

				root_matrix_2.transform(normal[faces[j][i]],
						transformed_normal[faces[j][i]]);

				surface_n[0] = transformed_normal[faces[j][i]][0];
				surface_n[1] = transformed_normal[faces[j][i]][1];
				surface_n[2] = transformed_normal[faces[j][i]][2];

				Vector.normalize(surface_n);

				for (int k = 0; k < 3; k++) {
					phong[k] = selected_material.a_rgb[k];
				}

				for (Light l : ls) {

					for (int k = 0; k < 3; k++) {

						phong[k] += l.rgb[k]
								* (selected_material.d_rgb[k] * Math.max(
										Vector.dot(l.xyz, surface_n), 0));
					}

				}

				transformed_vertices[faces[j][i]][3] = phong[0]
						* (1.0 - selected_material.mc_rgb[0]);
				transformed_vertices[faces[j][i]][4] = phong[1]
						* (1.0 - selected_material.mc_rgb[1]);
				transformed_vertices[faces[j][i]][5] = phong[2]
						* (1.0 - selected_material.mc_rgb[2]);

				viewport(a, pa);
				viewport(b, pb);

				transformed_vertices[faces[j][i]][0] = pa[0];
				transformed_vertices[faces[j][i]][1] = pa[1];
				transformed_vertices[faces[j][i]][2] = a[2];

				// g.drawLine(pa[0], pa[1], pb[0], pb[1]);

			}

			double[] pt_0 = transformed_vertices[faces[j][0]];
			double[] pt_1 = transformed_vertices[faces[j][1]];
			double[] pt_2 = transformed_vertices[faces[j][2]];
			double[] pt_3 = transformed_vertices[faces[j][3]];

			fill(pt_2, pt_0, pt_1);
			fill(pt_3, pt_0, pt_2);

		}

	}

	public void fill(double[] a, double[] b, double[] c) {
		double[] tl, bl, tr, br;
		double[] top, middle, bottom, larger, smaller, largest;

		if (a[1] >= b[1]) {
			larger = a;
			smaller = b;
		} else {
			larger = b;
			smaller = a;
		}

		if (larger[1] >= c[1]) {
			largest = larger;
			if (c[1] >= smaller[1]) {
				top = largest;
				middle = c;
				bottom = smaller;
			} else {
				top = largest;
				middle = smaller;
				bottom = c;
			}
		} else {
			top = c;
			middle = larger;
			bottom = smaller;
		}

		if (top[1] != bottom[1]) {
			double y_mid = (middle[1] - bottom[1]) / (top[1] - bottom[1]);

			tl = top;
			double[] middle_2 = lerp(y_mid, bottom, top);
			if (middle_2[0] >= middle[0]) {
				bl = middle;
				br = middle_2;
			} else {
				bl = middle_2;
				br = middle;
			}
			tr = top;
			setRGB(tl, bl, tr, br);

			bl = bottom;
			if (middle_2[0] >= middle[0]) {
				tl = middle;
				tr = middle_2;
			} else {
				tl = middle_2;
				tr = middle;
			}
			br = bottom;
			setRGB(tl, bl, tr, br);
		} else {

			if (bottom[0] >= middle[0]) {
				bl = middle;
				br = bottom;
			} else {
				bl = bottom;
				br = middle;
			}

			tr = top;
			tl = top;
			setRGB(tl, bl, tr, br);

		}

	}

	private void setRGB(double[] tl, double[] bl, double[] tr, double[] br) {

		// COMPUTE TOP-TO-BOTTOM INTERPOLATION FRACTION FOR THIS SCAN LINE

		double tly = tl[1];
		double bly = bl[1];

		for (int y = (int) bly; y < tly; y++) {

			double ty;
			if (bly == tly) {
				ty = 0;
			} else {
				ty = (y - bly) / (tly - bly);
			}

			// LINEARLY INTERPOLATE TO GET VALUES AT LEFT AND RIGHT EDGES OF
			// THIS
			// SCAN LINE

			double[] l = lerp(ty, bl, tl);
			double[] r = lerp(ty, br, tr);

			// COMPUTE LEFT-TO-RIGHT INTERPOLATION FRACTION OF THE PIXEL

			double lx = l[0];
			double rx = r[0];

			for (int x = (int) lx; x < rx; x++) {
				double tx;
				if (rx == lx) {
					tx = 0;
				} else {
					tx = (x - lx) / (rx - lx);
				}
				// LINEARLY INTERPOLATE TO GET VALUE AT PIXEL

				double[] values = lerp(tx, l, r);

				values[3] = Math.min(1, Math.max(0, values[3]));
				values[4] = Math.min(1, Math.max(0, values[4]));
				values[5] = Math.min(1, Math.max(0, values[5]));

				if (zbuffer[x][y][0] <= values[2]) {
					zbuffer[x][y][0] = (float) values[2];
					zbuffer[x][y][1] = (float) values[3];
					zbuffer[x][y][2] = (float) values[4];
					zbuffer[x][y][3] = (float) values[5];

				}
			}

		}

	}

	private double[] lerp(double t, double[] start, double[] end) {
		double[] output = new double[6];

		for (int i = 0; i < output.length; i++) {
			output[i] = (end[i] - start[i]) * t + start[i];
		}
		return output;

	}

	public void viewport(double src[], int dst[]) {

		dst[0] = (int) ((0.5 * width + f * src[0] / (f - src[2]) * width));
		dst[1] = (int) ((0.5 * height - f * src[1] / (f - src[2]) * width));

	}

}
