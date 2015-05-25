package run;

import java.util.Random;
import persistence.Persistence;
import conf.ConfReader;

public interface LeboncoinRunner extends Runnable
{
    static ConfReader  confReader  = ConfReader.getInstance();

    static Persistence persistence = Persistence.getInstance();

    static Random      random      = new Random();
}
