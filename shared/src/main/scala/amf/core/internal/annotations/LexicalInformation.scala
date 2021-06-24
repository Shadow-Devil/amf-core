package amf.core.internal.annotations

import amf.core.client.common.position.{Position, Range}
import amf.core.client.scala.model.domain._
import org.yaml.model.YNode.MutRef
import org.yaml.model.YPart

case class LexicalInformation(range: Range) extends SerializableAnnotation with PerpetualAnnotation {
  override val name: String = "lexical"

  override val value: String = range.toString
}

object LexicalInformation extends AnnotationGraphLoader {
  override def unparse(annotatedValue: String, objects: Map[String, AmfElement]): Option[Annotation] =
    Some(LexicalInformation.apply(annotatedValue))

  def apply(range: String): LexicalInformation = new LexicalInformation(Range.apply(range))
  def apply(lineFrom: Int, columnFrom: Int, lineTo: Int, columnTo: Int) =
    new LexicalInformation(Range((lineFrom, columnFrom), (lineTo, columnTo)))
  def apply(startPosition: Position, endPosition: Position) = new LexicalInformation(Range(startPosition, endPosition))

  def apply(ast: YPart): LexicalInformation = {
    val range = ast match {
      case m: MutRef =>
        m.target.map(_.range).getOrElse(m.range)
      case _ => ast.range
    }
    new LexicalInformation(Range.apply(range))
  }
}

class HostLexicalInformation(override val range: Range) extends LexicalInformation(range) {
  override val name = "host-lexical"
}

object HostLexicalInformation extends AnnotationGraphLoader {
  override def unparse(annotatedValue: String, objects: Map[String, AmfElement]): Option[Annotation] =
    Some(HostLexicalInformation.apply(Range(annotatedValue)))

  def apply(range: Range): HostLexicalInformation = new HostLexicalInformation(range)
}

class BasePathLexicalInformation(override val range: Range) extends LexicalInformation(range) {
  override val name = "base-path-lexical"
}

object BasePathLexicalInformation extends AnnotationGraphLoader {
  override def unparse(annotatedValue: String, objects: Map[String, AmfElement]): Option[Annotation] =
    Some(BasePathLexicalInformation(Range(annotatedValue)))

  def apply(range: Range): BasePathLexicalInformation = new BasePathLexicalInformation(range)
}