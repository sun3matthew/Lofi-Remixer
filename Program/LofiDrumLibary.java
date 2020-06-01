import java.util.*;

public class LofiDrumLibary
{
  private ArrayList<LofiDrumPattern> libary;
  public LofiDrumLibary()
  {
    libary = new ArrayList<LofiDrumPattern>();

    libary.add(new LofiDrumPattern(new String[]{"hat_5", "hat_5", "kick_5", "snare_17", "hat_5", "hat_5" ,"hat_5", "snare_17"} , new double[]{1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0}));
    libary.add(new LofiDrumPattern(new String[]{"kick_2", "snare_7", "kick_2", "snare_7", "kick_2", "snare_7" ,"snare_7"} , new double[]{1.0, 2.0, 3.5, 4.0, 5.0, 6.0, 8.0}));
    libary.add(new LofiDrumPattern(new String[]{"kick_6" , "snare_13", "kick_6", "snare_13"} , new double[]{1.5, 2.5, 3.5, 4.0}));
  }
  public LofiDrumPattern getPattern(int index)
  {
    return libary.get(index);
  }
}
