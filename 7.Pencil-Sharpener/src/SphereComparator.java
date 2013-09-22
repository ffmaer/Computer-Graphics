import java.util.Comparator;
public class SphereComparator implements Comparator<Hit> {
	public int compare(Hit h1, Hit h2) {
		int result = 0;
		if (h1.t < h2.t) {
			result = -1;
		} else if (h1.t > h2.t) {
			result = 1;
		}
		return result;

	}

}
