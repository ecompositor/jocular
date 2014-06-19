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
public class Texture extends Structure {
  /** C type : ovrTextureHeader */
  public TextureHeader Header;
  /** C type : uintptr_t[8] */
  public int TextureId;
  public int Padding[] = new int[7];

  public Texture() {
    super();
  }

  @Override
  protected List<?> getFieldOrder() {
    return Arrays.asList("Header", "TextureId", "Padding");
  }

  /**
   * @param Header
   *          C type : ovrTextureHeader<br>
   * @param PlatformData
   *          C type : uintptr_t[8]
   */
  public Texture(TextureHeader Header, int TextureId) {
    super();
    this.Header = Header;
    this.TextureId = TextureId;
  }

  public Texture(Pointer peer) {
    super(peer);
  }

  public static class ByReference extends Texture implements Structure.ByReference {

  };

  public static class ByValue extends Texture implements Structure.ByValue {

  };
}
