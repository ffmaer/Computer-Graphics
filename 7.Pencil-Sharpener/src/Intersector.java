import java.util.ArrayList;

public class Intersector {
	Sphere front_sphere;
	double min_t = 999999999;
	boolean intersect = false;

	public void intersect(double[] v, double[] w, ArrayList<Shape> shapes) {
		front_sphere = null;
		min_t = 999999999;
		intersect = false;

		for (int id = 0; id < shapes.size(); id++) {
			ArrayList<Hit> hits = shapes.get(id).get_boolean(v, w);
			for (int hid = 0; hid < hits.size(); hid++) {
				if (hits.get(hid).t > 0 && hits.get(hid).t < min_t
						&& hits.get(hid).sign == 1) {
					min_t = hits.get(hid).t;
					front_sphere = shapes.get(id).spheres.get(hits.get(hid).si);
					intersect = true;
					break;
				}
			}
		}
	}
}
