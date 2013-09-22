import java.util.ArrayList;

public class Sphere {

	double[] c = new double[3];
	double r;
	Material m;
	static int counter = 0;
	int id;
	// 0 and
	// 1 or
	// 2 minus
	// 3 negate

	int operator;

	double p_a, p_b, p_c, p_d, p_e, p_f, p_g, p_h, p_i, p_j;
	double p_a_1, p_b_1, p_c_1, p_d_1, p_e_1, p_f_1, p_g_1, p_h_1, p_i_1,
			p_j_1;
	ArrayList<Hit> sid_list = new ArrayList<Hit>();

	Sphere(double x, double y, double z, double a, double b, double cc,
			double d, double e, double f, double g, double h, double i,
			double j, Material m, int operator) {

		this.id = counter;
		counter++;
		this.c[0] = x;
		this.c[1] = y;
		this.c[2] = z;

		this.p_a = a;
		this.p_b = b;

		this.p_c = cc;

		this.p_d = d;

		this.p_e = e;

		this.p_f = f;

		this.p_g = g;

		this.p_h = h;
		this.p_i = i;
		this.p_j = j;

		this.p_a_1 = a;
		this.p_b_1 = b;

		this.p_c_1 = cc;

		this.p_d_1 = d;

		this.p_e_1 = e;

		this.p_f_1 = f;

		this.p_g_1 = g;

		this.p_h_1 = h;
		this.p_i_1 = i;
		this.p_j_1 = j;

		this.m = m;

		this.operator = operator;
	}

	public void reset() {
		p_a = p_a_1;
		p_b = p_b_1;

		p_c = p_c_1;

		p_d = p_d_1;

		p_e = p_e_1;

		p_f = p_f_1;

		p_g = p_g_1;

		p_h = p_h_1;
		p_i = p_i_1;
		p_j = p_j_1;
	}

	public void clear() {
		sid_list.clear();
	}

	public int in_or_out(double[] v, double[] w) {
		double t = t(v, w) + 1;
		double value = p_g * (v[0] + t * w[0]) + p_h * (v[1] + t * w[1]) + p_i
				* (v[2] + t * w[2]) + p_j;
		if (value < 0) {
			return 1;
		}
		return -1;
	}

	public boolean second_order(double[] v, double[] w) {
		double aa;
		aa = aa(v, w);
		if (aa == 0) {
			return false;
		}
		return true;
	}

	public double delta(double[] v, double[] w) {
		double aa, bb, cc;

		aa = aa(v, w);
		bb = bb(v, w);
		cc = cc(v, w);

		return Math.pow(bb, 2) - 4 * aa * cc;

	}

	public double t_in(double[] v, double[] w) {
		double aa, bb;
		double delta;
		delta = delta(v, w);

		aa = aa(v, w);

		bb = bb(v, w);

		return (double) (-bb - Math.sqrt(delta)) / (double) (2 * aa);

	}

	private double aa(double[] v, double[] w) {
		return p_a * Math.pow(w[0], 2) + p_f * w[0] * w[1] + p_e * w[0] * w[2]
				+ p_b * Math.pow(w[1], 2) + p_d * w[1] * w[2] + p_c
				* Math.pow(w[2], 2);
	}

	private double bb(double[] v, double[] w) {
		return 2 * p_a * v[0] * w[0] + p_f * v[0] * w[1] + p_e * v[0] * w[2]
				+ p_f * v[1] * w[0] + 2 * p_b * v[1] * w[1] + p_d * v[1] * w[2]
				+ p_e * v[2] * w[0] + p_d * v[2] * w[1] + 2 * p_c * v[2] * w[2]
				+ p_g * w[0] + p_h * w[1] + p_i * w[2];
	}

	private double cc(double[] v, double[] w) {
		return p_a * Math.pow(v[0], 2) + p_f * v[0] * v[1] + p_e * v[0] * v[2]
				+ p_g * v[0] + p_b * Math.pow(v[1], 2) + p_d * v[1] * v[2]
				+ p_h * v[1] + p_c * Math.pow(v[2], 2) + p_i * v[2] + p_j;

	}

	public double t_out(double[] v, double[] w) {
		double aa = aa(v, w);
		double bb = bb(v, w);
		double delta;
		delta = delta(v, w);
		return (double) (-bb + Math.sqrt(delta)) / (double) (2 * aa);

	}

	public double t(double[] v, double[] w) {
		double cc = cc(v, w);
		double bb = bb(v, w);

		return (double) (-cc) / (bb);

	}
}