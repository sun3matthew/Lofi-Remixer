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
      File fileIn = new File(name);// /Users/matthewsun/Desktop/Lofier/Input/"+name+".wav
      audioInputStream = AudioSystem.getAudioInputStream(fileIn);
    }catch(Exception e)
    {
      System.out.println("Ahh shit, here we go again " + name);
    }
  }
  public int[] read()
  {
    int totalFramesRead = 0;
    //File fileIn = new File("/Users/matthewsun/Desktop/Lofier/"+name+".aiff");
    // somePathName is a pre-existing string whose value was
    // based on a user selection.
    try {
      //audioInputStream = AudioSystem.getAudioInputStream(fileIn);
      System.out.println(audioInputStream.getFormat());
      int bytesPerFrame = audioInputStream.getFormat().getFrameSize();
        if (bytesPerFrame == AudioSystem.NOT_SPECIFIED) {
        // some audio formats may have unspecified frame size
        // in that case we may read any amount of bytes
        bytesPerFrame = 1;
      }
      // Set an arbitrary buffer size of 1024 frames.
      int numBytes = 1024 * bytesPerFrame;
      //byte[] audioBytes = new byte[numBytes];
      byte[] audioBytes = new byte[audioInputStream.available()];
      int[] audioInfo = new int[audioBytes.length/2];
      audioInputStream.mark(audioInputStream.available());
      try {
        int numBytesRead = 0;
        int numFramesRead = 0;
        // Try to read numBytes bytes from the file.
        //int counter = 0;
        //int counter = 0;
        while ((numBytesRead = audioInputStream.read(audioBytes)) != -1)
        {
          // Calculate the number of frames actually read.
          numFramesRead = numBytesRead / bytesPerFrame;
          totalFramesRead += numFramesRead;
          //System.out.println(counter);
          //counter++;

        }
        //audioBytes[0] = 0x7b;
        //audioBytes[1] = 0x51;
        for(int i = 0; i < audioBytes.length>>1; i++)
        {
          ByteBuffer bb = ByteBuffer.allocate(2);
          bb.order(ByteOrder.LITTLE_ENDIAN);
          bb.put(audioBytes[i*2]);
          bb.put(audioBytes[i*2+1]);
          audioInfo[i] = bb.getShort(0);
          audioInfo[i] = audioInfo[i];
        }
          //audioInfo[i] = ((audioBytes[i*2+1]+128)<<8) + (audioBytes[i*2]+128);
        //System.out.println(audioBytes[123432>>1]);
        //System.out.println(audioBytes[123432]);
        //System.out.println(audioInfo[123432]);
         return audioInfo;
      } catch (Exception ex) {
        System.out.println("test");
      }
    } catch (Exception e) {
      System.out.println("test");
    }
    return null;
    /*
    try{
      AudioSystem.write(audioInputStream, "aiff", new File("test.aiff"));
    } catch (Exception e) {
      System.out.println("Fuck you 3");
    }
    */
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
      //(audioInfo[i] > 45056)
        //System.out.println(audioInfo[i]);
      audioBytes[i*2] = (byte)((((audioInfo[i])) & 0xff));
      audioBytes[i*2+1] = (byte)(((((audioInfo[i]))>>8) & 0xff));
    }
    //System.out.println(audioBytes[123432>>1]);
    //System.out.println(audioBytes[123432]);
    //System.out.println(audioInfo[123432]);
    //for(int i = 0; i < audioBytes.length; i++)
    //{
    //  System.out.println(audioBytes[i]);
    //}
    //for(int i = 0; i < soundInfo.length; i++)
    //  audioBytes[i] = soundInfo[i];
    //audioInputStream.reset();
    try{
      AudioSystem.write(new AudioInputStream(new ByteArrayInputStream(audioBytes), audioInputStream.getFormat(), audioInputStream.getFrameLength()), AudioFileFormat.Type.WAVE, new File("/Users/matthewsun/Desktop/Lofier/Output/test.wav"));
    } catch (Exception e) {
      System.out.println("Fuck you 3");
    }
  }
}
