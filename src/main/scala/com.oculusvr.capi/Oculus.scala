/**
 * Created by klatsmj on 5/22/14.
 *
 * HMD - Head Mounted Display
 * FOV - Field of view
 */

package biz.scalamonkey.oculusvr
import com.oculusvr.capi._
import com.oculusvr.capi.OvrLibrary._
import com.sun.jna.Pointer
import java.nio.{ByteBuffer, FloatBuffer}

trait HmdType extends ovrHmdType
trait HmdCapBits extends ovrHmdCapBits
trait DistortionCaps extends ovrDistortionCaps
trait EyeType extends ovrEyeType
trait StatusBits extends ovrStatusBits
trait RenderAPIType extends ovrRenderAPIType


object Hmd extends OvrLibrary.ovrHmd

object Oculus extends OvrLibrary {

  type Hmd  = OvrLibrary.ovrHmd

  final val DefaultNeckToEyeVertical: Float = OVR_DEFAULT_NECK_TO_EYE_VERTICAL
  final val KeyNeckToEyeVertical:String = OVR_KEY_NECK_TO_EYE_VERTICAL
  final val KeyPlayerHeight: String = OVR_KEY_PLAYER_HEIGHT
  final val DefaultPlayerHeight: Float = OVR_DEFAULT_PLAYER_HEIGHT
  final val DefaultIPD: Float = OVR_DEFAULT_IPD
  final val KeyEyeHeight:String = OVR_KEY_EYE_HEIGHT
  final val KeyNeckToEyeHorizontal: String = OVR_KEY_NECK_TO_EYE_HORIZONTAL
  final val DefaultNeckToEyeHorizontal:Float = OVR_DEFAULT_NECK_TO_EYE_HORIZONTAL
  final val KeyUser: String = OVR_KEY_USER
  final val KeyName: String = OVR_KEY_NAME
  final val DefaultEyeHeight: Float = OVR_DEFAULT_EYE_HEIGHT
  final val DefaultGender: String = OVR_DEFAULT_GENDER
  final val KeyGender: String = OVR_KEY_GENDER
  final val KeyIPD: String = OVR_KEY_IPD

  /*
   IPD, which stands for interpupillary distance, is the distance between your pupils.
   IPD calibration is important for nausea-free gameplay with head mounted displays
   */
  def initialize: Byte = ovr_Initialize

  def shutdown() = ovr_Shutdown()

  def detect: Int = ovrHmd_Detect

  def create(index : Int) : Hmd = ovrHmd_Create(index)

  def destroy(hmd: Hmd) = ovrHmd_Destroy(hmd: Hmd)

  def createDebug(debugType: Int) = ovrHmd.createDebug(debugType)

  def getLastError(hmd: Hmd): String = ovrHmd_GetLastError(hmd)

  def startSensor(hmd: Hmd, supportedCaps: Int, requiredCaps: Int): Byte = ovrHmd_StartSensor(hmd, supportedCaps, requiredCaps )

  def stopSensor(hmd: Hmd) = ovrHmd_StopSensor(hmd)

  def resetSensor(hmd: Hmd) = ovrHmd_ResetSensor(hmd)

  def getetSensorState(hmd: Hmd, absTime: Double): SensorState.ByValue = ovrHmd_GetSensorState(hmd, absTime)

  def getSensorDesc(hmd: Hmd, descOut: SensorDesc): Byte = ovrHmd_GetSensorDesc(hmd, descOut)

  def getDesc(hmd: Hmd, desc: HmdDesc) = ovrHmd_GetDesc(hmd, desc)

  def getFovTextureSize(
                         hmd: Hmd,
                         eye: Int,
                         fov: FovPort.ByValue,
                         pixelsPerDisplayPixel: Float): Sizei.ByValue = {
    ovrHmd_GetFovTextureSize(hmd, eye, fov, pixelsPerDisplayPixel)
  }

  def configureRendering(
                          hmd: Hmd,
                          apiConfig: RenderAPIConfig,
                          hmdCaps: Int,
                          distortionCaps: Int,
                          eyeDescIn: EyeDesc,
                          eyeRenderDescOut: EyeRenderDesc): Byte = {
    ovrHmd_ConfigureRendering(hmd, apiConfig, hmdCaps, distortionCaps, eyeDescIn, eyeRenderDescOut )
  }

  def configureRendering(hmd: Pointer,
                         apiConfig: RenderAPIConfig,
                         hmdCaps: Int,
                         distortionCaps: Int,
                         eyeDescIn: EyeDesc,
                         eyeRenderDescOut: EyeRenderDesc): Byte = {
    ovrHmd_ConfigureRendering(hmd, apiConfig,hmdCaps, distortionCaps, eyeDescIn, eyesRenderDescout)
  }

  def beginFrame(hmd: Hmd, frameIndex: Int): FrameTiming.ByValue = ovrHmd_BeginFrame(hmd, frameIndex)

  def endFrame(hmd: Hmd) = ovrHmd_EndFrame

  def beginEyeRender(hmd: Hmd, eye: Int): Posef.ByValue = ovrHmd_BeginEyeRender(hmd , eye)

  def endEyeRender(hmd: Hmd, eye: Int, renderPose: Posef.ByValue, eyeTexture: Texture) = ovrHmd_EndEyeRender(hmd, eye)

  def getRenderDesc(hmd: Hmd, eyeDesc: EyeDesc.ByValue): EyeRenderDesc.ByValue = ovrHmd_GetRenderDesc( md, eyeDesc)

  def createDistortionMesh(
                            hmd: Hmd,
                            eyeDesc: EyeDesc.ByValue,
                            distortionCaps: Int,
                            uvScaleOffsetOut: Vector2f,
                            meshData: DistortionMesh): Byte = {
    ovrHmd_CreateDistortionMesh( hmd, eyeDesc, distortionCaps, uvScaleOffsetOut, meshData )
  }


  def createDistortionMesh(hmd: Pointer, eyeDesc: EyeDesc.ByValue, distortionCaps: Int, uvScaleOffsetOut: Vector2f, meshData: DistortionMesh): Byte = {
    ovrHmd_CreateDistortionMesh(hmd, eyeDesc, distortionCaps, uvScaleOffsetOut, meshData)
  }

  def destroyDistortionMesh(meshData: DistortionMesh) = {
    ovrHmd_DestroyDistortionMesh(meshData)
  }

  def getRenderScaleAndOffset(hmd: Hmd, eyeDesc: EyeDesc.ByValue, distortionCaps: Int, uvScaleOffsetOut: Vector2f) = {
    ovrHmd_GetRenderScaleAndOffset(hmd, eyeDesc,distortionCaps,uvScaleOffsetOut)
  }

  def getRenderScaleAndOffset(hmd: Pointer, eyeDesc: EyeDesc.ByValue, distortionCaps: Int, uvScaleOffsetOut: Vector2f) = {
    ovrHmd_GetRenderScaleAndOffset(hmd, eyeDesc,distortionCaps,uvScaleOffsetOut)
  }

  def getFrameTiming(hmd: Hmd, frameIndex: Int): FrameTiming.ByValue = ovrHmd_GetFrameTiming(hmd, frameIndex)

  def beginFrameTiming(hmd: Hmd, frameIndex: Int): FrameTiming.ByValue = ovrHmd_BeginFrameTiming(hmd, frameIndex)

  def endFrameTiming(hmd: Hmd) = ovrHmd_EndFrameTiming(hmd)

  def resetFrameTiming(hmd: Hmd, frameIndex: Int, vsync: Byte) = ovrHmd_ResetFrameTiming(hmd, frameIndex, vsync)

  def getEyePose(hmd: Hmd, eye: Int): Posef.ByValue = ovrHmd_GetEyePose(hmd , eye )

  def getEyeTimewarpMatrices(hmd: Hmd, eye: Int, renderPose: Posef.ByValue, twmOut: Matrix4f) = {
    ovrHmd_GetEyeTimewarpMatrices(hmd , eye , renderPose, twmOut )
  }

  def getEyeTimewarpMatrices(hmd: Pointer, eye: Int, renderPose: Posef.ByValue, twmOut: Matrix4f) = {
    ovrHmd_GetEyeTimewarpMatrices(hmd, eye, renderPose, twmOut)
  }
  def projection(fov: FovPort.ByValue, znear: Float, zfar: Float, rightHanded: Byte): Matrix4f.ByValue = {
    ovrMatrix4f_Projection(fov, znear, zfar, rightHanded)
  }

  def orthoSubProjection(
                          projection: Matrix4f.ByValue,
                          orthoScale: Vector2f.ByValue,
                          orthoDistance: Float,
                          eyeViewAdjustX: Float): Matrix4f.ByValue  = {
    ovrMatrix4f_OrthoSubProjection(projection, orthoScale, orthoDistance, eyeViewAdjustX)
  }

  def getTimeInSeconds: Double = ovr_GetTimeInSeconds

  def waitTillTime(absTime: Double): Double = ovr_WaitTillTime(absTime)

  def processLatencyTest(hmd: Hmd, rgbColorOut: ByteBuffer): Byte = ovrHmd_ProcessLatencyTest(hmd, rgbColorOut)

  def getLatencyTestResult(hmd: Hmd): String = ovrHmd_GetLatencyTestResult(hmd)

  def getMeasuredLatencyTest2(hmd: Hmd): Double = ovrHmd_GetMeasuredLatencyTest2(hmd)

  def getFloat(hmd: Hmd, propertyName: String, defaultVal: Float): Float = ovrHmd_GetFloat(hmd, propertyName, defaultVal)

  def setFloat(hmd: Hmd, propertyName: String, value: Float): Byte = ovrHmd_SetFloat(hmd, propertyName, value )

  def getFloatArray(
                     hmd: Hmd,
                     propertyName: String,
                     values: FloatBuffer,
                     arraySize: Int): Int = {
    ovrHmd_GetFloatArray(hmd, propertyName, values, arraySize )
  }

  def setFloatArray(
                     hmd: Hmd,
                     propertyName: String,
                     values: FloatBuffer,
                     arraySize: Int): Byte = {
    ovrHmd_SetFloatArray(hmd, propertyName, values, arraySize )
  }

  def getString(hmd: Hmd, propertyName: String, defaultVal: String): String =
    ovrHmd_GetString(hmd, propertyName, defaultVal)

 def getArraySize(hmd: Hmd, propertyName: String): Int = ovrHmd_GetArraySize(hmd, propertyName)

}

