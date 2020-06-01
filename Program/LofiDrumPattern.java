public class LofiDrumPattern
{
  private String[] drumNames;
  private double[] distanceToStart;
  LofiDrumPattern(String[] drumNamesIn, double[] distanceToStartIn)
  {
    drumNames = drumNamesIn;
    distanceToStart = distanceToStartIn;
  }
  public String[] getNames()
  {
    return drumNames;
  }
  public int[] getBeat(int bpm, int sampleRate)
  {
    double multiplyer = (60.0/(double)(bpm));
    int[] returnStuff = new int[distanceToStart.length];
    for(int i = 0; i < distanceToStart.length; i++)
    {
      returnStuff[i] = (int)((distanceToStart[i]*multiplyer)*sampleRate);
    }
    return returnStuff;
  }
}
