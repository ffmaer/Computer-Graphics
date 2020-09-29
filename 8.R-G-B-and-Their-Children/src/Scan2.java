// Iterative methods and integer arithmetic
public class Scan2 extends MISApplet {

	private static final long serialVersionUID = 1L;

	// three corners of a triangle
	int[] a = new int[6];
	int[] b = new int[6];
	int[] c = new int[6];

	double frame[][][];
	int scale = 2 ^ 12;

	public void initialize() {

		a[0] = (int) (0.20 * W);
		a[1] = (int) (0.30 * H);
		a[2] = 0;
		a[3] = 255;
		a[4] = 0;
		a[5] = 0;

		b[0] = (int) (0.50 * W);
		b[1] = (int) (0.90 * H);
		b[2] = 0;
		b[3] = 0;
		b[4] = 255;
		b[5] = 0;

		c[0] = (int) (0.80 * W);
		c[1] = (int) (0.60 * H);
		c[2] = 0;
		c[3] = 0;
		c[4] = 0;
		c[5] = 255;

		for (int i = 0; i < 6; i++) {
			a[i] = a[i];
			b[i] = b[i];
			c[i] = c[i];
		}

		frame = new double[W][H][6];

	}

	int[] dly = new int[6];
	int[] dry = new int[6];
	int[] l = new int[6];
	int[] r = new int[6];
	int[] dx = new int[6];
	int[] lx = new int[6];

	public void fill(int[] tl, int[] tr, int[] bl, int[] br) {

		int n = (bl[1] == tl[1]) ? 1 : (bl[1] - tl[1]);

		for (int i = 0; i < 6; i++) {
			dly[i] = scale * (bl[i] - tl[i]) / n;
			dry[i] = scale * (br[i] - tr[i]) / n;

		}
		for (int i = 0; i < 6; i++) {
			l[i] = scale * tl[i];
			r[i] = scale * tr[i];

		}

		for (int yy = tl[1]; yy < bl[1]; yy++) {

			for (int i = 0; i < 6; i++) {
				// INCREMENTALLY UPDATE VALUES ALONG LEFT AND RIGHT
				// EDGES

				l[i] += dly[i];
				r[i] += dry[i];

			}

			int m = ((r[0] - l[0]) == 0) ? 1 : (r[0] - l[0]) / scale;

			for (int i = 0; i < 6; i++) {
				dx[i] = (r[i] - l[i]) / m;
			}

			for (int i = 0; i < 6; i++) {

				lx[i] = l[i];
			}

			for (int xx = l[0]; xx < r[0]; xx += scale) {
				// INCREMENTALLY UPDATE VALUE AT PIXEL

				for (int i = 0; i < 6; i++) {
					lx[i] += dx[i];

					frame[xx / scale][yy][i] = lx[i] / scale;

				}

			}

		}

	}

	public void initFrame(double time) { // INITIALIZE ONE ANIMATION FRAME

		for (int i = 0; i < W; i++) {
			for (int j = 0; j < H; j++) {
				for (int k = 0; k < 6; k++) {
					frame[i][j][k] = 0;
				}
			}
		}

		double t = (double) (c[1] - a[1]) / (double) (b[1] - a[1]);

		int[] tl, bl, tr, br;

		tl = a;
		bl = lerp(t, a, b);
		tr = a;
		br = c;

		fill(tl, tr, bl, br);

		tl = bl;
		bl = b;
		tr = c;
		br = b;

		fill(tl, tr, bl, br);

	}

	private int[] lerp(double t, int[] start, int[] end) {
		int[] output = new int[6];

		for (int i = 0; i < 6; i++) {
			output[i] = (int) ((end[i] - start[i]) * t + start[i]);
		}
		return output;

	}

	public void setPixel(int x, int y, int rgb[]) { // SET ONE PIXEL'S COLOR
		rgb[0] = (int) frame[x][y][3];
		rgb[1] = (int) frame[x][y][4];
		rgb[2] = (int) frame[x][y][5];
	}
}
