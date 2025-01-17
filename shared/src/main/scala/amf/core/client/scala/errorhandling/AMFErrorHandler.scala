package amf.core.client.scala.errorhandling

import amf.core.client.common.validation.SeverityLevels.{VIOLATION, WARNING}
import amf.core.client.scala.model.domain.AmfObject
import amf.core.client.scala.validation.AMFValidationResult
import amf.core.internal.annotations.{LexicalInformation, SourceLocation => AmfSourceLocation}
import amf.core.internal.parser.domain.Annotations
import amf.core.internal.utils.AmfStrings
import amf.core.internal.validation.core.ValidationSpecification
import org.mulesoft.common.client.lexical.{PositionRange, SourceLocation}

import scala.collection.mutable

trait AMFErrorHandler {

  private val results: mutable.LinkedHashSet[AMFValidationResult] = mutable.LinkedHashSet()

  /** Get all [[AMFValidationResult]] reported */
  def getResults: List[AMFValidationResult] = results.toList

  /** Report an [[AMFValidationResult]] */
  def report(result: AMFValidationResult): Unit = synchronized {
    if (!results.contains(result)) {
      results += result
    }
  }

  def guiKey(message: String, location: Option[String], lexical: Option[LexicalInformation]): String = {
    message ++ location.getOrElse("") ++ lexical.map(_.value).getOrElse("")
  }

  def reportConstraint(
      id: String,
      node: String,
      property: Option[String],
      message: String,
      lexical: Option[LexicalInformation],
      level: String,
      location: Option[String]
  ): Unit =
    report(AMFValidationResult(message, level, node, property, id, lexical, location, this))

  def reportConstraint(
      id: String,
      node: AmfObject,
      property: Option[String],
      message: String,
      lexical: Option[LexicalInformation],
      level: String,
      location: Option[String]
  ): Unit =
    report(AMFValidationResult(message, level, node, property, id, lexical, location, this))

  def reportConstraint(
      specification: ValidationSpecification,
      node: String,
      message: String,
      pos: SourceLocation,
      level: String
  ): Unit =
    reportConstraint(specification.id, node, None, message, lexical(pos), level, pos.sourceName.option)

  /** Report constraint failure of severity violation. */
  def violation(
      specification: ValidationSpecification,
      node: String,
      property: Option[String],
      message: String,
      lexical: Option[LexicalInformation],
      location: Option[String]
  ): Unit = {
    reportConstraint(specification.id, node, property, message, lexical, VIOLATION, location)
  }

  def violation(
      specification: ValidationSpecification,
      node: AmfObject,
      property: Option[String],
      message: String,
      lexical: Option[LexicalInformation],
      location: Option[String]
  ): Unit = {
    reportConstraint(specification.id, node, property, message, lexical, VIOLATION, location)
  }

  def violation(
      specification: ValidationSpecification,
      node: String,
      message: String,
      annotations: Annotations
  ): Unit = {
    violation(
        specification,
        node,
        None,
        message,
        annotations.find(classOf[LexicalInformation]),
        annotations.find(classOf[AmfSourceLocation]).map(_.location)
    )
  }

  def violation(
      specification: ValidationSpecification,
      node: AmfObject,
      message: String,
      annotations: Annotations
  ): Unit = {
    violation(
        specification,
        node,
        None,
        message,
        annotations.find(classOf[LexicalInformation]),
        annotations.find(classOf[AmfSourceLocation]).map(_.location)
    )
  }

  /** Report constraint failure of severity violation for the given amf object. */
  def violation(
      specification: ValidationSpecification,
      element: AmfObject,
      target: Option[String],
      message: String
  ): Unit =
    violation(specification, element.id, target, message, element.position(), element.location())

  /** Report constraint failure of severity violation with location file. */
  def violation(specification: ValidationSpecification, node: String, message: String, location: String): Unit = {
    violation(specification, node, None, message, None, location.option)
  }

  /** Report constraint failure of severity violation with location file. */
  def violation(specification: ValidationSpecification, node: AmfObject, message: String, location: String): Unit = {
    reportConstraint(specification.id, node, None, message, None, VIOLATION, location.option)
  }

  def violation(spec: ValidationSpecification, n: String, prop: Option[String], msg: String, l: SourceLocation): Unit =
    violation(spec, n, prop, msg, lexical(l), l.sourceName.option)

  def violation(
      spec: ValidationSpecification,
      n: AmfObject,
      prop: Option[String],
      msg: String,
      l: SourceLocation
  ): Unit =
    violation(spec, n, prop, msg, lexical(l), l.sourceName.option)

  def violation(specification: ValidationSpecification, node: String, message: String, loc: SourceLocation): Unit =
    violation(specification, node, None, message, loc)

  def violation(specification: ValidationSpecification, node: AmfObject, message: String, loc: SourceLocation): Unit =
    violation(specification, node, None, message, loc)

  def violation(specification: ValidationSpecification, node: String, message: String): Unit =
    violation(specification, node, None, message, None, None)

  def violation(specification: ValidationSpecification, node: AmfObject, message: String): Unit =
    violation(specification, node, None, message, None, None)

  /** Report constraint failure of severity warning. */
  def warning(
      specification: ValidationSpecification,
      node: String,
      property: Option[String],
      message: String,
      lexical: Option[LexicalInformation],
      location: Option[String]
  ): Unit =
    reportConstraint(specification.id, node, property, message, lexical, WARNING, location)

  def warning(
      specification: ValidationSpecification,
      node: AmfObject,
      property: Option[String],
      message: String,
      lexical: Option[LexicalInformation],
      location: Option[String]
  ): Unit =
    reportConstraint(specification.id, node, property, message, lexical, WARNING, location)

  /** Report constraint failure of severity violation for the given amf object. */
  def warning(spec: ValidationSpecification, element: AmfObject, target: Option[String], message: String): Unit =
    warning(spec, element, target, message, element.position(), element.location())

  /** Report constraint failure of severity warning. */
  def warning(
      specification: ValidationSpecification,
      node: String,
      property: Option[String],
      message: String,
      location: SourceLocation
  ): Unit =
    warning(specification, node, property, message, lexical(location), location.sourceName.option)

  def warning(
      specification: ValidationSpecification,
      node: AmfObject,
      property: Option[String],
      message: String,
      location: SourceLocation
  ): Unit =
    warning(specification, node, property, message, lexical(location), location.sourceName.option)

  def warning(specification: ValidationSpecification, node: String, message: String, location: SourceLocation): Unit =
    warning(specification, node, None, message, location)

  def warning(
      specification: ValidationSpecification,
      node: AmfObject,
      message: String,
      location: SourceLocation
  ): Unit =
    warning(specification, node, None, message, location)

  /** Report constraint failure of severity warning. */
  def warning(specification: ValidationSpecification, node: String, message: String, annotations: Annotations): Unit =
    warning(
        specification,
        node,
        None,
        message,
        annotations.find(classOf[LexicalInformation]),
        annotations.find(classOf[AmfSourceLocation]).map(_.location)
    )

  private def lexical(loc: SourceLocation): Option[LexicalInformation] = {
    loc.range match {
      case PositionRange.ZERO => None
      case range              => Some(LexicalInformation(range))
    }
  }

}
