package amf.core.client.platform.config

import amf.core.client.scala.config
import amf.core.internal.convert.CoreClientConverters._
import amf.core.client.scala.config.{ShapeRenderOptions => InternalShapeRenderOptions}
import scala.scalajs.js.annotation.{JSExportAll, JSExportTopLevel}

@JSExportAll
@JSExportTopLevel("ShapeRenderOptions")
case class ShapeRenderOptions(private[amf] val _internal: config.ShapeRenderOptions) {

  @JSExportTopLevel("ShapeRenderOptions")
  def this() = this(InternalShapeRenderOptions())

  def isWithDocumentation: Boolean     = _internal.isWithDocumentation
  def isWithCompactedEmission: Boolean = _internal.isWithCompactedEmission
  def schemaVersion: JSONSchemaVersion = _internal.schemaVersion

  /** Remove documentation info as examples, descriptions, display names, etc. */
  def withoutDocumentation: ShapeRenderOptions = _internal.withoutDocumentation

  /** Render shape without extracting common types to definitions. */
  def withoutCompactedEmission: ShapeRenderOptions = _internal.withoutCompactedEmission

  def withSchemaVersion(version: JSONSchemaVersion): ShapeRenderOptions = _internal.withSchemaVersion(version)
}

@JSExportAll
@JSExportTopLevel("JSONSchemaVersions")
object JSONSchemaVersions {
  val UNSPECIFIED: JSONSchemaVersion   = Unspecified
  val DRAFT_04: JSONSchemaVersion      = JsonSchemaDraft4
  val DRAFT_07: JSONSchemaVersion      = JsonSchemaDraft7
  val DRAFT_2019_09: JSONSchemaVersion = JsonSchemaDraft201909
}

sealed trait JSONSchemaVersion
object Unspecified           extends JSONSchemaVersion
object JsonSchemaDraft4      extends JSONSchemaVersion
object JsonSchemaDraft7      extends JSONSchemaVersion
object JsonSchemaDraft201909 extends JSONSchemaVersion