package amf.core.client.platform.model.domain

import amf.core.internal.convert.CoreClientConverters._
import amf.core.client.platform.model.StrField
import amf.core.client.scala.model.domain.templates.{AbstractDeclaration => InternalAbstractDeclaration}

import scala.scalajs.js.annotation.{JSExportAll, JSExportTopLevel}

@JSExportAll
class AbstractDeclaration(private[amf] val _internal: InternalAbstractDeclaration)
    extends DomainElement
    with Linkable
    with NamedDomainElement {

  override def name: StrField         = _internal.name
  def description: StrField           = _internal.description
  def dataNode: DataNode              = _internal.dataNode
  def variables: ClientList[StrField] = _internal.variables.asClient

  override def withName(name: String): this.type = {
    _internal.withName(name)
    this
  }

  def withDescription(description: String): this.type = {
    _internal.withDescription(description)
    this
  }

  def withDataNode(dataNode: DataNode): this.type = {
    _internal.withDataNode(dataNode._internal)
    this
  }

  def withVariables(variables: ClientList[String]): this.type = {
    _internal.withVariables(variables.asInternal)
    this
  }

  override def linkTarget: ClientOption[DomainElement] = throw new Exception("AbstractDeclaration is abstract")

  override def linkCopy(): AbstractDeclaration = throw new Exception("AbstractDeclaration is abstract")
}
