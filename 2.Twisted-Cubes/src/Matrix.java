public class Matrix {
	private double data[][];
	private double temp_data[][];

	public Matrix() {
		data = new double[4][4];
		temp_data = new double[4][4];
	}

	public void set(int i, int j, double value) {
		data[i][j] = value;
	}

	public double get(int i, int j) {
		return data[i][j];
	}

	public double[][] getData() {
		return data;
	}

	public void copy(Matrix src) {
		for (int row = 0; row < 4; row++)
			for (int col = 0; col < 4; col++)
				data[row][col] = src.get(row, col);
	}

	private void createIdentityData(double destination[][]) {
		for (int row = 0; row < 4; row++)
			for (int col = 0; col < 4; col++) {
				if (row == col)
					destination[row][col] = 1;
				else
					destination[row][col] = 0;
			}
	}

	private void createTranslationData(double x, double y, double z,
			double destination[][]) {
		createIdentityData(destination);
		destination[0][3] = x;
		destination[1][3] = y;
		destination[2][3] = z;
	}

	private void createXRotationData(double theta, double destination[][]) {
		createIdentityData(destination);
		destination[1][1] = Math.cos(theta);
		destination[1][2] = -Math.sin(theta);
		destination[2][1] = Math.sin(theta);
		destination[2][2] = Math.cos(theta);
	}

	private void createYRotationData(double theta, double destination[][]) {

		createIdentityData(destination);
		destination[0][0] = Math.cos(theta);
		destination[2][0] = -Math.sin(theta);
		destination[0][2] = Math.sin(theta);
		destination[2][2] = Math.cos(theta);
	}

	private void createZRotationData(double theta, double destination[][]) {
		createIdentityData(destination);
		destination[0][0] = Math.cos(theta);
		destination[0][1] = -Math.sin(theta);
		destination[1][0] = Math.sin(theta);
		destination[1][1] = Math.cos(theta);
	}

	private void createScaleData(double x, double y, double z,
			double destination[][]) {
		createIdentityData(destination);
		destination[0][0] = x;
		destination[1][1] = y;
		destination[2][2] = z;
	}

	public void multiply(double[][] src) {
		for (int row = 0; row < 4; row++) {
			for (int col = 0; col < 4; col++) {
				temp_data[row][col] = data[row][col];
			}
		}
		for (int row = 0; row < 4; row++) {
			for (int col = 0; col < 4; col++) {
				data[row][col] = 0;
				for (int i = 0; i < 4; i++) {
					data[row][col] += temp_data[row][i] * src[i][col];
				}
			}
		}
	}

	public void identity() {
		createIdentityData(data);
	}

	public void translate(double a, double b, double c) {
		double temp_data[][] = new double[4][4];
		createTranslationData(a, b, c, temp_data);
		multiply(temp_data);
	}

	public void rotateX(double theta) {
		double temp_data[][] = new double[4][4];
		createXRotationData(theta, temp_data);
		multiply(temp_data);
	}

	public void rotateY(double theta) {
		double temp_data[][] = new double[4][4];
		createYRotationData(theta, temp_data);
		multiply(temp_data);
	}

	public void rotateZ(double theta) {
		double temp_data[][] = new double[4][4];
		createZRotationData(theta, temp_data);
		multiply(temp_data);
	}

	public void scale(double a, double b, double c) {
		double temp_data[][] = new double[4][4];
		createScaleData(a, b, c, temp_data);
		multiply(temp_data);
	}

	public void transform(double src[], double destination[]) {
		for (int row = 0; row < destination.length; row++) {
			destination[row] = 0;
			for (int i = 0; i < src.length; i++) {
				destination[row] += data[row][i] * src[i];
			}
		}
	}

}