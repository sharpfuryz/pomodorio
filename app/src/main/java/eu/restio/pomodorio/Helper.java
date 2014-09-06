package eu.restio.pomodorio;

import java.util.Random;

/**
 * Created by sovremenius on 06.09.14.
 */
public class Helper {
    public static String[] mm = {"Just do it!","Don't stop!","Don't stress!","You can!","Move, move, move!","Concentrate!","One task!","Don't distracted!","It's your life!"};
    public static String[] rm = {"Relax","Rest","you're done!","Congratulations!"};

    public static String get_random_motivation_message(){
        int idx = new Random().nextInt(mm.length);
        return (mm[idx]);
    }

    public static String get_random_relax_message(){
        int idx = new Random().nextInt(rm.length);
        return (rm[idx]);
    }
}
