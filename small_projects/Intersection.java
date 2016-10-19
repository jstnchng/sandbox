import java.lang.Math;

public class Intersection{
public static void main (String[] args)
{
    double cdx = Math.abs(-0.99757 - -1.95051);
    double cdy = Math.abs(-0.00000974 - 2.49904);

    if (cdx > (rect.width/2 + circle.r)) { return false; }
    if (cdy > (rect.height/2 + circle.r)) { return false; }

    if (circleDistance.x <= (rect.width/2)) { return true; }
    if (circleDistance.y <= (rect.height/2)) { return true; }

    cornerDistance_sq = (circleDistance.x - rect.width/2)^2 +
                         (circleDistance.y - rect.height/2)^2;

    return (cornerDistance_sq <= (circle.r^2));
}
}
circle: -0.99757
-0.000972841
radius: 0.5
bbox.min: -2.1001
-0.102018
bbox.max -1.80092
5.1001
centroid: -1.95051
2.49904

cdx = .95294
cdy = 2.5000128
width = 0.3
height = 5.2
