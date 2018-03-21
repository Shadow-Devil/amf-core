package amf.client.model

import scala.scalajs.js.annotation.JSExportAll

@JSExportAll
trait DoubleField extends BaseAnyValField {

  override type ValueType = Double

  /** Return double value or `0.0` if value is null or undefined. */
  override def value(): Double = option() match {
    case Some(v) => v
    case _       => 0.0
  }
}
