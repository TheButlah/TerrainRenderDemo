package me.thebutlah.perlinnoise;

import java.util.Random;

public class PerlinNoise {
    private Random rand;
    private int[] perms = new int[256];
    /**
     * Used to initialize a PerlinNoise Generator
     *
     * This constructor is run once to set up all of the gradients for the noise function.
     * Because of its precomputation of gradients, the actual PerlinNoise generator will save on
     * resources and be able to compute the points much faster.
     *
     * @param persistence Defines how much each recursion varies from the last.
     * Should lie between 0 and 1. If this parameter is null, the persistence defaults to 0.5.
     * Values close to 1 will have little variance, while those closer to zero will have large variance.
     *
     * @param recursions Defines the level of detail the generator will be capable of.
     * Recommended value is 6.
     *
     * @param seed Defines the seed of the function. Noise functions initialized with the same seed
     * will have the same output for each point. If this parameter is null, the seed will be random.
     *
     * @param samplesize Defines the number of gradient vectors to be chosen from.
     * A higher number will result in fewer recognizable patterns in the output.
     */
    public PerlinNoise(Long seed) {
        if (seed == null) {
            rand = new Random();
        } else {
            rand = new Random(seed);
        }

        for (int i=0; i<perms.length; i++) {
            perms[i] = (short) rand.nextInt(128);
        }


    }

    public double perlinNoise2D(double x, double y, int recursions, double freq, double ampl) {
        double sum = 0;
        double maxsum = 0;
        for (int i = 0; i<recursions; i++) {
            double amplitude = Math.pow(ampl, i);
            double frequency = Math.pow(freq, i);
            sum += noise2D(x * frequency, y * frequency) * amplitude; //(usually) double the frequency and (usually) halve the amplitude
            maxsum += amplitude;
        }
        return sum/maxsum; //ensures that the output is between -1 and 1
    }

    private double noise2D(double x, double y){

        int x_grid = floor(x);
        int y_grid = floor(y);

        int hashX = x_grid & 127; //ignoring all but the last 7 bits
        int hashY = y_grid & 127;

        double x_deci = x - x_grid;
        double y_deci = y - y_grid;

        double fX = fade(x_deci);
        double fY = fade(y_deci);

        int hash00 = perms[hashX  ] + hashY;		//computing hashes for each point of the square
        int hash10 = perms[hashX+1] + hashY;
        int hash01 = hash00+1;
        int hash11 = hash10+1;


        //Below we construct the 4 scalar values at each integer point of the square based off of the dot-products
        //first digit represents x, second y
        double dot00 = grad(perms[hash00], x_deci, y_deci, 0);
        double dot10 = grad(perms[hash10], x_deci-1, y_deci, 0);
        double dot01 = grad(perms[hash01], x_deci, y_deci-1, 0);
        double dot11 = grad(perms[hash11], x_deci-1, y_deci-1, 0);

        double yint_1 = lerp(dot00, dot10, fX);
        double yint_2 = lerp(dot01, dot11, fX);

        return lerp(yint_1, yint_2, fY);


    }

    private static int floor(double x) {
        return x >= 0 ? (int) x : (int) x - 1;
    }

    private static double grad(int hash, double x, double y, double z) {
        int h = hash & 15;                      // CONVERT LO 4 BITS OF HASH CODE
        double u = h<8 ? x : y,                 // INTO 12 GRADIENT DIRECTIONS.
                v = h<4 ? y : h==12||h==14 ? x : z;
        return ((h&1) == 0 ? u : -u) + ((h&2) == 0 ? v : -v); //TAKE THE DOT PRODUCT
    }

    /**
     * Linear Interpolation based on fade value
     */
    private static double lerp(double a, double b, double fade) {
        return a + fade*(b-a);
    }

    /**
     * Used to skew a number between 0 and 1 towards or away from 0 or 1 to enable smooth interpolation-
     */
    private static double fade(double f) {
        //switch(INT_TYPE) {
        //case 3:
        return f * f* f * (f * (f*6 - 15) + 10);	//6x^5 - 15x^4 + 10x^3
        //case 2:
        //return f * f * (3 - 2f);	//3x^2-2x^3
        //case 1:
        //default:
        //return f;
        //}

    }
}
