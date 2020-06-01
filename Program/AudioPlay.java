public class AudioPlay
{
  private int counter = 0;
  private int [] audio;
  private double volume;
  public AudioPlay(int [] sound, double volumeIn)
  {
    volume = volumeIn;
    audio = sound;
  }
  public int update()
  {
    counter++;
    if(counter-1 < audio.length)
      return (int)(audio[counter-1]*volume);
    return 0;
  }
  public boolean isDone()
  {
    return counter >= audio.length;
  }
}
