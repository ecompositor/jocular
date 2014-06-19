package com.oculusvr.capi;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import javax.annotation.Nonnull;

import com.google.common.base.Preconditions;
import com.sun.jna.Pointer;
import com.sun.jna.PointerType;

public class Hmd extends PointerType {
  boolean inFrame = false;
  boolean configuredRendering = false;
  int currentEye = -1;

  @Nonnull 
  static private FovPort.ByValue byValue(@Nonnull FovPort fov) {
    if (fov instanceof FovPort.ByValue) {
      return (FovPort.ByValue) fov;
    }
    return new FovPort.ByValue(fov);
  }

  @Nonnull 
  static private Posef.ByValue byValue(@Nonnull Posef pose) {
    if (pose instanceof Posef.ByValue) {
      return (Posef.ByValue) pose;
    }
    return new Posef.ByValue(pose);
  }

  @Nonnull 
  static private OvrMatrix4f.ByValue byValue(@Nonnull OvrMatrix4f m) {
    if (m instanceof OvrMatrix4f.ByValue) {
      return (OvrMatrix4f.ByValue) m;
    }
    return new OvrMatrix4f.ByValue(m);
  }

  @Nonnull 
  static private OvrVector2f.ByValue byValue(@Nonnull OvrVector2f v) {
    if (v instanceof OvrVector2f.ByValue) {
      return (OvrVector2f.ByValue) v;
    }
    return new OvrVector2f.ByValue(v);
  }

  public static void initialize() {
    if (0 == OvrLibrary.INSTANCE.ovr_Initialize()) {
      throw new IllegalStateException("Unable to initialize Oculus SDK");
    }
  }

  public static void shutdown() {
    OvrLibrary.INSTANCE.ovr_Shutdown();
  }

  public static Hmd create(int index) {
    return OvrLibrary.INSTANCE.ovrHmd_Create(index);
  }

  @Nonnull 
  public static Hmd createDebug(int type) {
    return OvrLibrary.INSTANCE.ovrHmd_CreateDebug(type);
  }

  public void destroy() {
    OvrLibrary.INSTANCE.ovrHmd_Destroy(this);
  }

  public String getLastError() {
    return OvrLibrary.INSTANCE.ovrHmd_GetLastError(this);
  }

  public int getEnabledCaps() {
    return OvrLibrary.INSTANCE.ovrHmd_GetEnabledCaps(this);
  }

  public void setEnabledCaps(int hmdCaps) {
    OvrLibrary.INSTANCE.ovrHmd_SetEnabledCaps(this, hmdCaps);
  }

  public byte startSensor(int supportedSensorCaps, int requiredSensorCaps) {
    return OvrLibrary.INSTANCE.ovrHmd_StartSensor(this, supportedSensorCaps, requiredSensorCaps);
  }

  public void stopSensor() {
    OvrLibrary.INSTANCE.ovrHmd_StopSensor(this);
  }

  public void resetSensor() {
    OvrLibrary.INSTANCE.ovrHmd_ResetSensor(this);
  }

  public SensorState getSensorState(double absTime) {
    return OvrLibrary.INSTANCE.ovrHmd_GetSensorState(this, absTime);
  }

  public SensorDesc getSensorDesc() {
    SensorDesc descOut = new SensorDesc();
    if (0 == OvrLibrary.INSTANCE.ovrHmd_GetSensorDesc(this, descOut)) {
      throw new IllegalStateException("Unable to fetch sensor description");
    }
    return descOut;
  }

  public HmdDesc getDesc() {
    HmdDesc desc = new HmdDesc();
    OvrLibrary.INSTANCE.ovrHmd_GetDesc(this, desc);
    return desc;
  }

  public OvrSizei getFovTextureSize(int eye, FovPort fov, float pixelsPerDisplayPixel) {
    return OvrLibrary.INSTANCE.ovrHmd_GetFovTextureSize(this, eye, byValue(fov), pixelsPerDisplayPixel);
  }

  public EyeRenderDesc[] configureRendering(RenderAPIConfig apiConfig, int distortionCaps, FovPort eyeFovIn[]) {
    Pointer first = eyeFovIn[0].getPointer();
    int size = eyeFovIn[0].size();
    Pointer secondCalc = first.getPointer(size);
    Pointer secondActual = eyeFovIn[1].getPointer().getPointer(0);
    Preconditions.checkArgument(secondCalc.equals(secondActual), 
        "eyeFovIn must be a contiguous array in memory.  Use new FovPort().toArray(2) to create it");
    EyeRenderDesc eyeRenderDescs[] = (EyeRenderDesc[]) new EyeRenderDesc().toArray(2);
    if (0 == OvrLibrary.INSTANCE.ovrHmd_ConfigureRendering(this, apiConfig, distortionCaps, eyeFovIn, eyeRenderDescs)) {
      throw new IllegalStateException("Unable to configure rendering");
    }
    configuredRendering = true;
    return eyeRenderDescs;
  }

  public FrameTiming beginFrame(int frameIndex) {
    Preconditions.checkState(configuredRendering, "rendering must be configured before beginEyeRender is called");
    Preconditions.checkArgument(!inFrame, "a frame is already being rendered");
    inFrame = true;
    return OvrLibrary.INSTANCE.ovrHmd_BeginFrame(this, frameIndex);
  }

  public void endFrame() {
    Preconditions.checkArgument(inFrame, "beginFrame must be called before endFrame");
    inFrame = false;
    OvrLibrary.INSTANCE.ovrHmd_EndFrame(this);
  }

  public Posef beginEyeRender(int eye) {
    Preconditions.checkState(configuredRendering, "rendering must be configured before beginEyeRender is called");
    Preconditions.checkState(-1 == currentEye, "an eye is already rendering");
    Preconditions.checkState(inFrame, "beginFrame must be called before beginEyeRender");
    this.currentEye = eye;
    return OvrLibrary.INSTANCE.ovrHmd_BeginEyeRender(this, eye);
  }

  public void endEyeRender(int eye, @Nonnull Posef renderPose, @Nonnull Texture eyeTexture) {
    Preconditions.checkArgument(-1 != currentEye, "endEyeRender must be called after beginEyeRender");
    Preconditions.checkArgument(eye == currentEye, "endEyeRender must be called with the same eye argument as beginEyeRender");
    Preconditions.checkNotNull(renderPose);
    Preconditions.checkNotNull(eyeTexture);
    currentEye = -1;
    OvrLibrary.INSTANCE.ovrHmd_EndEyeRender(this, eye, byValue(renderPose), eyeTexture);
  }

  @Nonnull
  public EyeRenderDesc getRenderDesc(int eyeType, @Nonnull FovPort fov) {
    Preconditions.checkNotNull(fov);
    return OvrLibrary.INSTANCE.ovrHmd_GetRenderDesc(this, eyeType, byValue(fov));
  }

  @Nonnull
  public DistortionMesh createDistortionMesh(int eyeType, @Nonnull FovPort fov, int distortionCaps) {
    Preconditions.checkNotNull(fov);
    DistortionMesh meshData = new DistortionMesh();
    if (0 == OvrLibrary.INSTANCE.ovrHmd_CreateDistortionMesh(this, eyeType, byValue(fov), distortionCaps, meshData)) {
      throw new IllegalStateException("Unable to create distortion mesh");
    }
    return meshData;
  }

  @Nonnull
  public FrameTiming getFrameTiming(int frameIndex) {
    return OvrLibrary.INSTANCE.ovrHmd_GetFrameTiming(this, frameIndex);
  }

  @Nonnull
  public FrameTiming beginFrameTiming(int frameIndex) {
    return OvrLibrary.INSTANCE.ovrHmd_BeginFrameTiming(this, frameIndex);
  }

  public void endFrameTiming() {
    OvrLibrary.INSTANCE.ovrHmd_EndFrameTiming(this);
  }

  public void resetFrameTiming(int frameIndex) {
    OvrLibrary.INSTANCE.ovrHmd_ResetFrameTiming(this, frameIndex);
  }

  @Nonnull
  public Posef getEyePose(int eye) {
    return OvrLibrary.INSTANCE.ovrHmd_GetEyePose(this, eye);
  }

  public void getEyeTimewarpMatrices(int eye, Posef renderPose, OvrMatrix4f twmOut[]) {
    OvrLibrary.INSTANCE.ovrHmd_GetEyeTimewarpMatrices(this, eye, byValue(renderPose), twmOut);
  }

  public byte processLatencyTest(ByteBuffer rgbColorOut) {
    return OvrLibrary.INSTANCE.ovrHmd_ProcessLatencyTest(this, rgbColorOut);
  }

  public Pointer getLatencyTestResult() {
    return OvrLibrary.INSTANCE.ovrHmd_GetLatencyTestResult(this);
  }

  public double getMeasuredLatencyTest2() {
    return OvrLibrary.INSTANCE.ovrHmd_GetMeasuredLatencyTest2(this);
  }

  public float getFloat(@Nonnull String propertyName, float defaultVal) {
    return OvrLibrary.INSTANCE.ovrHmd_GetFloat(this, propertyName, defaultVal);
  }

  public byte setFloat(@Nonnull String propertyName, float value) {
    return OvrLibrary.INSTANCE.ovrHmd_SetFloat(this, propertyName, value);
  }

  public float[] getFloatArray(@Nonnull String propertyName) {
    int arraySize = getArraySize(propertyName);
    if (0 == arraySize) {
      return null;
    }
    return getFloatArray(propertyName, arraySize);
  }

  public float[] getFloatArray(@Nonnull String propertyName, int arraySize) {
    FloatBuffer buffer = FloatBuffer.allocate(arraySize);
    int result = OvrLibrary.INSTANCE.ovrHmd_GetFloatArray(this, propertyName, buffer, arraySize);
    if (0 == result) {
      return null;
    }
    return buffer.array();
  }

  public byte setFloatArray(@Nonnull String propertyName, @Nonnull float[] values) {
    return setFloatArray(propertyName, FloatBuffer.wrap(values), values.length);
  }

  public int getFloatArray(@Nonnull String propertyName, @Nonnull FloatBuffer values, int arraySize) {
    return OvrLibrary.INSTANCE.ovrHmd_GetFloatArray(this, propertyName, values, arraySize);
  }

  public byte setFloatArray(@Nonnull String propertyName, @Nonnull FloatBuffer values, int arraySize) {
    return OvrLibrary.INSTANCE.ovrHmd_SetFloatArray(this, propertyName, values, arraySize);
  }

  public String getString(@Nonnull String propertyName, String defaultVal) {
    return OvrLibrary.INSTANCE.ovrHmd_GetString(this, propertyName, defaultVal);
  }

  public int getArraySize(@Nonnull String propertyName) {
    return OvrLibrary.INSTANCE.ovrHmd_GetArraySize(this, propertyName);
  }

  public static OvrMatrix4f getPerspectiveProjection(@Nonnull FovPort fov, float znear, float zfar, boolean rightHanded) {
    Preconditions.checkArgument(znear < zfar, "znear must be less than zfar");
    return OvrLibrary.INSTANCE.ovrMatrix4f_Projection(byValue(fov), znear, zfar, (byte) (rightHanded ? 1 : 0));
  }

  public static OvrMatrix4f getOrthographicProjection(@Nonnull OvrMatrix4f projection, @Nonnull OvrVector2f orthoScale,
      float orthoDistance, float eyeViewAdjustX) {
    return OvrLibrary.INSTANCE.ovrMatrix4f_OrthoSubProjection(byValue(projection), byValue(orthoScale), orthoDistance,
        eyeViewAdjustX);
  }

  public static double getTimeInSeconds() {
    return OvrLibrary.INSTANCE.ovr_GetTimeInSeconds();
  }

  public static void waitTillTime(double absTime) {
    OvrLibrary.INSTANCE.ovr_WaitTillTime(absTime);
  }

}
