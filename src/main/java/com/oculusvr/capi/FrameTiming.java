package com.oculusvr.capi;

import java.util.Arrays;
import java.util.List;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;

/**
 * <i>native declaration : /usr/include/stdint.h</i><br>
 * This file was autogenerated by <a
 * href="http://jnaerator.googlecode.com/">JNAerator</a>,<br>
 * a tool written by <a href="http://ochafik.com/">Olivier Chafik</a> that <a
 * href="http://code.google.com/p/jnaerator/wiki/CreditsAndLicense">uses a few
 * opensource projects.</a>.<br>
 * For help, please visit <a
 * href="http://nativelibs4java.googlecode.com/">NativeLibs4Java</a> , <a
 * href="http://rococoa.dev.java.net/">Rococoa</a>, or <a
 * href="http://jna.dev.java.net/">JNA</a>.
 */
public class FrameTiming extends Structure {
  public float DeltaSeconds;
  public double ThisFrameSeconds;
  public double TimewarpPointSeconds;
  public double NextFrameSeconds;
  public double ScanoutMidpointSeconds;
  /** C type : double[2] */
  public double[] EyeScanoutSeconds = new double[2];

  public FrameTiming() {
    super();
  }

  @Override
  protected List<?> getFieldOrder() {
    return Arrays.asList("DeltaSeconds", "ThisFrameSeconds", "TimewarpPointSeconds", "NextFrameSeconds",
        "ScanoutMidpointSeconds", "EyeScanoutSeconds");
  }

  /**
   * @param EyeScanoutSeconds
   *          C type : double[2]
   */
  public FrameTiming(float DeltaSeconds, double ThisFrameSeconds, double TimewarpPointSeconds, double NextFrameSeconds,
      double ScanoutMidpointSeconds, double EyeScanoutSeconds[]) {
    super();
    this.DeltaSeconds = DeltaSeconds;
    this.ThisFrameSeconds = ThisFrameSeconds;
    this.TimewarpPointSeconds = TimewarpPointSeconds;
    this.NextFrameSeconds = NextFrameSeconds;
    this.ScanoutMidpointSeconds = ScanoutMidpointSeconds;
    if ((EyeScanoutSeconds.length != this.EyeScanoutSeconds.length))
      throw new IllegalArgumentException("Wrong array size !");
    this.EyeScanoutSeconds = EyeScanoutSeconds;
  }

  public FrameTiming(Pointer peer) {
    super(peer);
  }

  public static class ByReference extends FrameTiming implements Structure.ByReference {

  };

  public static class ByValue extends FrameTiming implements Structure.ByValue {

  };
}
