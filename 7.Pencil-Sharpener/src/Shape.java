import java.util.ArrayList;
import java.util.Collections;

public class Shape {
	ArrayList<Sphere> spheres = new ArrayList<Sphere>();
	SphereComparator sc = new SphereComparator();
	ArrayList<Hit> c = new ArrayList<Hit>();
	ArrayList<Hit> d = new ArrayList<Hit>();
	ArrayList<Integer> a_si_list = new ArrayList<Integer>();

	public void negate(ArrayList<Hit> a) {
		for (int i = 0; i < a.size(); i++) {
			a.get(i).sign = (-1) * a.get(i).sign;

		}
	}

	public ArrayList<Hit> intersect(ArrayList<Hit> a, ArrayList<Hit> b) {

		c.clear();
		d.clear();
		a_si_list.clear();

		boolean in_a = (a.size() == 0);
		boolean in_b = (b.size() == 0);

		for (int i = 0; i < a.size(); i++) {
			c.add(a.get(i));
			a_si_list.add(a.get(i).si);
		}

		for (int i = 0; i < b.size(); i++) {
			c.add(b.get(i));
		}

		Collections.sort(c, sc);

		for (int i = 0; i < c.size(); i++) {
			if (c.get(i).sign == 1) {

				if (a_si_list.contains(c.get(i).si)) {
					in_a = true;
				} else {
					in_b = true;
				}
			} else {
				if (a_si_list.contains(c.get(i).si)) {
					in_a = false;
				} else {
					in_b = false;
				}

			}

			if (in_a && in_b) {
				c.get(i).sign = 1;
				d.add(c.get(i));

			} else {
				c.get(i).sign = -1;
				d.add(c.get(i));
			}
		}

		return d;
	}

	@SuppressWarnings("unchecked")
	public ArrayList<Hit> get_boolean(double[] v, double[] w) {
		ArrayList<Hit> cylinder = new ArrayList<Hit>();
		double mini = 0.01;
		for (int si = 0; si < spheres.size(); si++) {
			spheres.get(si).clear();

			if (spheres.get(si).second_order(v, w)) {

				if (spheres.get(si).delta(v, w) > 0) {
					double t_in = spheres.get(si).t_in(v, w);
					double t_out = spheres.get(si).t_out(v, w);
					spheres.get(si).sid_list.add(new Hit(si, t_in, 1));
					spheres.get(si).sid_list.add(new Hit(si, t_out, -1));
					spheres.get(si).sid_list.add(new Hit(si, -9999999, -1));

					if (t_in >= mini) {
						spheres.get(si).sid_list.add(new Hit(si, mini, -1));
					} else if (t_out <= mini) {
						spheres.get(si).sid_list.add(new Hit(si, mini, -1));
					}

				} else {
					spheres.get(si).sid_list.add(new Hit(si, -9999999, -1));
				}

			} else {
				double t_on = spheres.get(si).t(v, w);

				int sign = spheres.get(si).in_or_out(v, w);
				spheres.get(si).sid_list
						.add(new Hit(si, -9999999, (-1) * sign));
				spheres.get(si).sid_list.add(new Hit(si, t_on, sign));
				if (t_on > mini) {
					spheres.get(si).sid_list
							.add(new Hit(si, mini, (-1) * sign));
				} else {
					spheres.get(si).sid_list.add(new Hit(si, mini, sign));
				}
			}

			if (spheres.get(si).operator == 3) {
				negate(spheres.get(si).sid_list);

			}

			cylinder = (ArrayList<Hit>) intersect(spheres.get(si).sid_list, cylinder).clone();

		}
		Collections.sort(cylinder, sc);
		return cylinder;
	}
}
