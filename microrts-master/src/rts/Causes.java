package rts;

import java.util.Random;
import rts.GameState;

public class Causes {

    Random random;
    protected int specificCause;

    Causes(int spec) {
        specificCause = spec;
    }

    Causes() {
        specificCause = random.nextInt(5);
    }

    public boolean Check(int cause) {

        return false;
    }
    
}