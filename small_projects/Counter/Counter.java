import java.math.BigInteger;

public class Counter {
    public static void main(String[] args){
        long time, t1;
        t1 = System.currentTimeMillis();
        for(double i=0; i<16777216; i++){}
        time = System.currentTimeMillis() - t1;
        time /= 1000.0;
        System.out.println("24: " + time);

        t1 = System.currentTimeMillis();
        for(double i=0; i<33554432; i++){}
        time = System.currentTimeMillis() - t1;
        time /= 1000.0;
        System.out.println("25: " + time);

        t1 = System.currentTimeMillis();
        for(double i=0; i<67108864; i++){}
        time = System.currentTimeMillis() - t1;
        time /= 1000.0;
        System.out.println("26: " + time);

        t1 = System.currentTimeMillis();
        for(double i=0; i<134217728; i++){}
        time = System.currentTimeMillis() - t1;
        time /= 1000.0;
        System.out.println("27: " + time);

        t1 = System.currentTimeMillis();
        for(double i=0; i<268435456; i++){}
        time = System.currentTimeMillis() - t1;
        time /= 1000.0;
        System.out.println("28: " + time);

        t1 = System.currentTimeMillis();
        for(double i=0; i<536870912; i++){}
        time = System.currentTimeMillis() - t1;
        time /= 1000.0;
        System.out.println("29: " + time);

        t1 = System.currentTimeMillis();
        for(double i=0; i<1073741824; i++){}
        time = System.currentTimeMillis() - t1;
        time /= 1000.0;
        System.out.println("30: " + time);

        t1 = System.currentTimeMillis();
        for(double i=0; i<2147483648.0; i++){}
        time = System.currentTimeMillis() - t1;
        time /= 1000.0;
        System.out.println("31: " + time);

        t1 = System.currentTimeMillis();
        for(double i=0; i<4294967296.0; i++){}
        time = System.currentTimeMillis() - t1;
        time /= 1000.0;
        System.out.println("32: " + time);

        t1 = System.currentTimeMillis();
        for(double i=0; i<8589934592.0; i++){}
        time = System.currentTimeMillis() - t1;
        time /= 1000.0;
        System.out.println("33: " + time);
    }
}
