import java.io.*;
import java.util.*;

public class Lofier
{
  final static int largestIntVal = 32767;
  int sampleRate;
  private int start;
  private ArrayList<AudioPlay> play = new ArrayList<AudioPlay>();
  private int[] finalSound;
  public static void main(String [] args)
  {
    Lofier run = new Lofier();
    if(args.length > 0)
    {
      run.runShit(args[0]);
    }else
    {
      run.runShit("Date");
    }
  }
  public void runShit(String name)
  {
    LofiDrumLibary libary = new LofiDrumLibary();
    MusicReadWrite trackOne = new MusicReadWrite("/Users/matthewsun/Desktop/Lofier/Input/"+name+".wav");
    MusicReadWrite crack = new MusicReadWrite("/Users/matthewsun/Desktop/Lofier/DrumSamples/ohShit_2.wav");
    MusicReadWrite rainThing = new MusicReadWrite("/Users/matthewsun/Desktop/Lofier/DrumSamples/rain.wav");
    //MusicReadWrite drum = new MusicReadWrite("/Users/matthewsun/Desktop/Lofier/Input/"+Dr+".wav");
    sampleRate = trackOne.getSampleRate();
    int[] soundInfo = cloneArray(trackOne.read());
    int[] crackly = crack.read();
    int[] rain = rainThing.read();
    //int[] trackTwo = myHeartsAStereo(cloneArray(drum.read()));
    ArrayList<Integer> indexBeat = new ArrayList<Integer>();
    int bpm = findBpm(soundInfo)/2;
    start+=(60.0/(double)(bpm))*sampleRate*9;
    finalSound = new int[soundInfo.length];
    //soundInfo = killHighHz(soundInfo);
    play(soundInfo, 0.5);
    play(rain, 1);
    play(crackly, 1);
    int previous = 0;
    //int bpm = findBpm(soundInfo)/2;// (int)((double)((60.0*sampleRate))/(double)(findBpm(soundInfo)))
    System.out.println(sampleRate);
    //System.out.println(bpm);
    System.out.println(start);
    LofiDrumPattern drumPattern = libary.getPattern(1);
    int[][] drumNames = getTracks(drumPattern.getNames());
    int[] beatDistance = drumPattern.getBeat(bpm, sampleRate);
    //int loopVin = 0;
    int counter = 1;
    int drumIndexCounter = 0;
    for(int i = 0; i < soundInfo.length; i++)
    {
      if(i > start)
      {
        counter++;
      }
      if(counter == beatDistance[drumIndexCounter])
      {
        play(drumNames[drumIndexCounter], 0.1);
        drumIndexCounter++;
      }
      if(drumIndexCounter == beatDistance.length)
      {
        drumIndexCounter = 0;
        counter = 0;
      }
      update(i);
    }
    //reduceBitDepth(finalSound);
    //trackOne.write(slowTheFuckDown(finalSound));
    trackOne.write(flutterYourWings(killHighHz((reduceBitDepth(finalSound)))));//flutterYourWings(killHighHz(slowTheFuckDown(reduceBitDepth(finalSound))))
  }
  private int[] slowTheFuckDown(int [] inputSong)
  {
    int [] returnThing = new int[inputSong.length];
    int slowDown = 2;
    int counter = 0;
    for(int i = 0; i < (inputSong.length>>1)-slowDown; i++)
    {
      if(i%slowDown == 0)
      {
        for(int loop = 0; loop < slowDown; loop++)
        {
          returnThing[counter+(loop*2)] = inputSong[i];
          returnThing[counter+1+(loop*2)] = inputSong[i+1];
        }
        counter+=slowDown;
      }
    }
    return returnThing;
  }
  private int[] flutterYourWings(int [] inputSong)
  {
    ArrayList<Integer> thing = new ArrayList<Integer>();
    int onePeight = (int)(1.8*sampleRate);
    int flutterLength = (int)(2.5*sampleRate);
    boolean flutter = false;
    int flutterCounter = 0;
    int moduloCounter = 0;
    boolean phase = false;
    //int counter = 0;
    for(int i = 0; i < inputSong.length>>1; i++)
    {
      if(i%onePeight == 0)
      {
        onePeight = (int)(randomWithRange(1, 2.1)*sampleRate);
        flutterCounter = 0;
        flutter = true;
        moduloCounter = 100;
        phase = true;
      }
      if(flutter)
      {
        flutterCounter++;
        int temp = flutterLength/80;//200-60*2
        if(moduloCounter == 60)//60
          phase = false;
        if(flutterCounter%temp == 0)
        {
          if(phase)
            moduloCounter--;
          else
            moduloCounter++;
        }
        if(flutterCounter%moduloCounter == 0)
        {
          for(int loop = 0; loop < 2; loop++)
          {
            thing.add(inputSong[i*2]);
            thing.add(inputSong[(i*2)+1]);
          }
        }else
        {
          thing.add(inputSong[i*2]);
          thing.add(inputSong[(i*2)+1]);
        }
      }else
      {
        thing.add(inputSong[i*2]);
        thing.add(inputSong[(i*2)+1]);
      }
      if(flutterCounter >= flutterLength)
        flutter = false;
    }
    int [] returnThing = new int[thing.size()];
    for(int i = 0; i < thing.size(); i++)
      returnThing[i] = thing.get(i);
    return returnThing;
  }
  private int[] killHighHz(int [] inputSong)
  {
    int [] returnThing = new int[inputSong.length];
    double threshold = 0.1;
    for(int i = 0; i < inputSong.length; i++)
    {
      if(inputSong[i] > (threshold*largestIntVal))
      {
        //System.out.println(i);
        returnThing[i] = (int)(((threshold*largestIntVal))+(((inputSong[i]-(threshold*largestIntVal))/3)*5));
      }else
        returnThing[i] = inputSong[i];
    }
    return returnThing;
  }
  private int[] reduceBitDepth(int [] inputSong)
  {
    int [] returnThing = new int[inputSong.length];
    double range = ((double)(900))/((double)(largestIntVal));
    for(int i = 0; i < inputSong.length; i++)
    {
      int temp = (int)(range*inputSong[i]);
      returnThing[i] = (int)((1.0/range)*temp);
    }
    return returnThing;
  }
  private int findBpm(int[] song)
  {
    int [] songR = new int[song.length/2];
    int [] songL = new int[song.length/2];
    boolean[] beatTracker = new boolean[song.length/2];
    for(int i = 0; i < beatTracker.length; i++)
      beatTracker[i] = false;
    for(int i = 0; i < song.length>>1; i++)
    {
      songR[i] = song[i*2];
      songL[i] = song[(i*2)+1];
    }
    long average = 0;
    int counter = 0;
    for(int i = 0; i < songR.length; i++)
    {
      counter++;
      average += ((songR[i]*songR[i]) + (songL[i]*songL[i]));
    }
    average = average/counter;
    start = -1;
    for(int i = 0; i < songR.length; i++)
    {
      if(((songR[i]*songR[i]) + (songL[i]*songL[i])) > (average/2))
      {
        if(start == -1)
          start = i+1000;
        beatTracker[i] = true;
      }
    }
    int bestScore = -32768;
    int bpm = 0;
    for(int testBpm = 40; testBpm <= 180; testBpm++)
    {
      int currentScore = 0;
      int lookfor = (int)((double)((60.0*sampleRate))/(double)(testBpm));
      for(int i = start; i < beatTracker.length-lookfor; i+=lookfor)
      {
        if(beatTracker[i])
          currentScore+=10;
        else
          currentScore-=9;
      }
      System.out.println(testBpm + "\t" + currentScore);
      if(currentScore > bestScore)
      {
        bestScore = currentScore;
        bpm = testBpm;
      }
    }
    System.out.println("BPM   " + bpm);
    return bpm;
  }
  private int[][] getTracks(String [] names)
  {
    int[][] drumTracks = new int[names.length][];
    for(int i = 0; i < names.length; i++)
      drumTracks[i] = new MusicReadWrite("/Users/matthewsun/Desktop/Lofier/DrumSamples/EasyAccess/"+names[i]+".wav").read();
    return drumTracks;
  }
  private void play(int[] sound, double volume)
  {
    play.add(new AudioPlay(cloneArray(sound), volume));
  }
  private void update(int on)
  {
    int temp = 0;
    for(int i = 0; i < play.size(); i++)
    {
      if(play.get(i).isDone())
      {
        play.remove(i);
        i--;
      }else
      {
        temp+=play.get(i).update();
      }
    }
    finalSound[on] = temp/2;//play.size()
  }
  private int[] myHeartsAStereo(int[] mono)
  {
    int [] newArray = new int[mono.length*2];
    for(int i = 0; i < mono.length; i++)
      newArray[i*2] = newArray[(i*2)+1] = mono[i];
    return newArray;
  }
  private int[] cloneArray(int[] toClone)
  {
    int[] returnArray = new int[toClone.length];
    for(int i = 0; i < toClone.length; i++)
      returnArray[i] = toClone[i];
    return returnArray;
  }
  private double randomWithRange(double min, double max)
  {
     double range = (max - min) + 1.0;
     return (Math.random() * range) + min;
  }
}
