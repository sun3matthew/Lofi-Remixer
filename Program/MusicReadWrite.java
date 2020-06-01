import java.io.*;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioFileFormat;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
public class MusicReadWrite
{
  AudioInputStream audioInputStream;
  public MusicReadWrite(String name)
  {
    try{
      File fileIn = new File(name);
      audioInputStream = AudioSystem.getAudioInputStream(fileIn);
    }catch(Exception e)
    {
      System.out.println("Ahh shit, here we go again " + name);
    }
  }
  public int[] read()
  {
    int totalFramesRead = 0;
    try {
      System.out.println(audioInputStream.getFormat());
      int bytesPerFrame = audioInputStream.getFormat().getFrameSize();
        if (bytesPerFrame == AudioSystem.NOT_SPECIFIED) {
        bytesPerFrame = 1;
      }
      int numBytes = 1024 * bytesPerFrame;
      byte[] audioBytes = new byte[audioInputStream.available()];
      int[] audioInfo = new int[audioBytes.length/2];
      audioInputStream.mark(audioInputStream.available());
      try {
        int numBytesRead = 0;
        int numFramesRead = 0;
        while ((numBytesRead = audioInputStream.read(audioBytes)) != -1)
        {
          numFramesRead = numBytesRead / bytesPerFrame;
          totalFramesRead += numFramesRead;
        }
        for(int i = 0; i < audioBytes.length>>1; i++)
        {
          ByteBuffer bb = ByteBuffer.allocate(2);
          bb.order(ByteOrder.LITTLE_ENDIAN);
          bb.put(audioBytes[i*2]);
          bb.put(audioBytes[i*2+1]);
          audioInfo[i] = bb.getShort(0);
          audioInfo[i] = audioInfo[i];
        }
         return audioInfo;
      } catch (Exception ex) {
        System.out.println("test");
      }
    } catch (Exception e) {
      System.out.println("test");
    }
    return null;
  }
  public int getSampleRate()
  {
    return (int)(audioInputStream.getFormat().getSampleRate());
  }
  public void write(int [] audioInfo)
  {
    byte[] audioBytes = new byte[audioInfo.length*2];
    for(int i = 0; i < audioBytes.length>>1; i++)
    {
      audioBytes[i*2] = (byte)((((audioInfo[i])) & 0xff));
      audioBytes[i*2+1] = (byte)(((((audioInfo[i]))>>8) & 0xff));
    }
    try{
      AudioSystem.write(new AudioInputStream(new ByteArrayInputStream(audioBytes), audioInputStream.getFormat(), audioInputStream.getFrameLength()), AudioFileFormat.Type.WAVE, new File("/Users/matthewsun/Desktop/Lofier/Output/test.wav"));
    } catch (Exception e) {
      System.out.println("Fuck you 3");
    }
  }
}
