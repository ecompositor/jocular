package com.oculusvr.capi;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;
/**
 * This file was autogenerated by <a href="http://jnaerator.googlecode.com/">JNAerator</a>,<br>
 * a tool written by <a href="http://ochafik.com/">Olivier Chafik</a> that <a href="http://code.google.com/p/jnaerator/wiki/CreditsAndLicense">uses a few opensource projects.</a>.<br>
 * For help, please visit <a href="http://nativelibs4java.googlecode.com/">NativeLibs4Java</a> , <a href="http://rococoa.dev.java.net/">Rococoa</a>, or <a href="http://jna.dev.java.net/">JNA</a>.
 */
public class TrackingState extends Structure {
  public PoseStatef HeadPose;
  public Posef CameraPose;
  public Posef LeveledCameraPose;
  public SensorData RawSensorData;
  public int StatusFlags;
  public TrackingState() {
    super();
  }
  protected List<? > getFieldOrder() {
    return Arrays.asList("HeadPose", "CameraPose", "LeveledCameraPose", "RawSensorData", "StatusFlags");
  }
  public TrackingState(PoseStatef HeadPose, Posef CameraPose, Posef LeveledCameraPose, SensorData RawSensorData, int StatusFlags) {
    super();
    this.HeadPose = HeadPose;
    this.CameraPose = CameraPose;
    this.LeveledCameraPose = LeveledCameraPose;
    this.RawSensorData = RawSensorData;
    this.StatusFlags = StatusFlags;
  }
  public TrackingState(Pointer peer) {
    super(peer);
  }
  public static class ByReference extends TrackingState implements Structure.ByReference {
    
  };
  public static class ByValue extends TrackingState implements Structure.ByValue {
    
  };
}
