public class Vector {
	public static double square(double[] vec) {
		double square = 0;
		for (int i = 0; i < 3; i++)
			square += Math.pow(vec[i], 2);
		return square;
	}

	public static double norm(double vec[]) {
		double norm = Math.sqrt(square(vec));
		return norm;
	}

	public static void normalize(double vec[]) {
		double norm = norm(vec);

		for (int i = 0; i < 3; i++)
			vec[i] /= norm;

	}

	public static double[] minus(double a[], double b[]) {
		double[] c = new double[3];
		for (int i = 0; i < 3; i++)
			c[i] = a[i] - b[i];
		return c;
	}

	public static double dot(double a[], double b[]) {
		double dot = 0;
		for (int i = 0; i < 3; i++)
			dot += a[i] * b[i];
		return dot;
	}
}
