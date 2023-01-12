package utils.math;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class barryCentric {

    public static float barryCentric(Vector3f p1, Vector3f p2, Vector3f p3, Vector2f pos) {
        /*
		float det = (p2.z - p3.z) * (p1.x - p3.x) + (p3.x - p2.x) * (p1.z - p3.z);
		float l1 = ((p2.z - p3.z) * (pos.x - p3.x) + (p3.x - p2.x) * (pos.y - p3.z)) / det;
		float l2 = ((p3.z - p1.z) * (pos.x - p3.x) + (p1.x - p3.x) * (pos.y - p3.z)) / det;
		float l3 = 1.0f - l1 - l2;
		return l1 * p1.y + l2 * p2.y + l3 * p3.y;
         */

        float dist1 = distance(pos, new Vector2f(p1.x, p1.z));
        float dist2 = distance(pos, new Vector2f(p2.x, p2.z));
        float dist3 = distance(pos, new Vector2f(p3.x, p3.z));
        float biggest = Math.max(dist1, Math.max(dist2, dist3));
        dist1 /= biggest;
        dist2 /= biggest;
        dist3 /= biggest;
        float dists = dist1 + dist2 + dist3;

        /*
		float answer = ((dists - dist1) * p1.y +
				(dists - dist2) * p2.y +
				(dists - dist3) * p3.y
				) / ((dists - dist2)+(dists - dist1)+(dists - dist3));
         */
        float answer = (dist1 * p1.y
                + dist2 * p2.y
                + dist3 * p3.y) / dists;

        /*
		System.out.print(
				roundTo(pos.x,2)+
				"  "+
				roundTo(pos.y,2)+
				"      "+
				roundTo(p1.y,2)+
				"  "+
				roundTo(dist1*biggest,2)+
				"      "+
				roundTo(p2.y,2)+
				"  "+
				roundTo(dist2*biggest,2)+
				"      "+
				roundTo(p3.y,2)+
				"  "+
				roundTo(dist3*biggest,2)+
				"      "+
				roundTo(dists,2)+
				"  "+
				answer+
				"      ");
         */
        return answer;

    }

    private static float distance(Vector2f from, Vector2f to) {
        /*return (float) Math.sqrt( Math.sqrt(
					Math.pow( Math.max(from.x,to.x) - Math.min(from.x, to.x) , 2)+
					Math.pow( Math.max(from.z,to.z) - Math.min(from.z, to.z) , 2)
				)+  Math.pow( Math.max(from.y,to.y) - Math.min(from.y, to.y) , 2));*/

        return (float) Math.sqrt(
                Math.pow(Math.max(from.x, to.x) - Math.min(from.x, to.x), 2)
                + Math.pow(Math.max(from.y, to.y) - Math.min(from.y, to.y), 2)
        );
    }

    private static float roundTo(float number, int decimalPlaces) {
        decimalPlaces = (int) Math.pow(10, decimalPlaces);
        float answer = (int) (number * decimalPlaces);
        answer /= decimalPlaces;
        return answer;
    }
}
