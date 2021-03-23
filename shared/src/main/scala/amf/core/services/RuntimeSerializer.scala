package amf.core.services

import amf.core.emitter.{RenderOptions, ShapeRenderOptions}
import amf.core.model.document.BaseUnit
import amf.core.remote.Platform

import scala.concurrent.Future

trait RuntimeSerializer {
  def dump(unit: BaseUnit,
           mediaType: String,
           vendor: String,
           options: RenderOptions,
           shapeOptions: ShapeRenderOptions): String

  def dump(unit: BaseUnit,
           mediaType: String,
           vendor: String,
           shapeOptions: ShapeRenderOptions): String = dump(unit, mediaType, vendor, RenderOptions(), shapeOptions)

  def dumpToFile(platform: Platform,
                 file: String,
                 unit: BaseUnit,
                 mediaType: String,
                 vendor: String,
                 options: RenderOptions,
                 shapeOptions: ShapeRenderOptions): Future[Unit]
}

object RuntimeSerializer {
  var serializer: Option[RuntimeSerializer] = None
  def register(runtimeSerializer: RuntimeSerializer): Unit = {
    serializer = Some(runtimeSerializer)
  }

  def dumpToFile(platform: Platform,
                 file: String,
                 unit: BaseUnit,
                 mediaType: String,
                 vendor: String,
                 options: RenderOptions,
                 shapeOptions: ShapeRenderOptions = ShapeRenderOptions()): Future[Unit] = {
    serializer match {
      case Some(runtimeSerializer) =>
        runtimeSerializer.dumpToFile(platform, file, unit, mediaType, vendor, options, shapeOptions)
      case None => throw new Exception("No registered runtime serializer")
    }
  }

  def apply(unit: BaseUnit,
            mediaType: String,
            vendor: String,
            options: RenderOptions = RenderOptions(),
            shapeOptions: ShapeRenderOptions = ShapeRenderOptions()): String = {
    serializer match {
      case Some(runtimeSerializer) => runtimeSerializer.dump(unit, mediaType, vendor, options, shapeOptions)
      case None                    => throw new Exception("No registered runtime serializer")
    }
  }

  // only used from JsonSchemaSerializer
  def apply(unit: BaseUnit,
            mediaType: String,
            vendor: String,
            shapeOptions: ShapeRenderOptions): String = {
    serializer match {
      case Some(runtimeSerializer) => runtimeSerializer.dump(unit, mediaType, vendor, shapeOptions)
      case None                    => throw new Exception("No registered runtime serializer")
    }
  }
}
