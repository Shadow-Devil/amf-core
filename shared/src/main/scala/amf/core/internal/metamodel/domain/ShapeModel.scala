package amf.core.internal.metamodel.domain

import amf.core.internal.metamodel.Field
import amf.core.internal.metamodel.Type.{Array, Bool, Iri, SortedArray, Str}
import amf.core.internal.metamodel.domain.common.DescribedElementModel
import amf.core.internal.metamodel.domain.extensions.{PropertyShapeModel, ShapeExtensionModel}
import amf.core.internal.metamodel.domain.templates.KeyField
import amf.core.client.scala.vocabulary.Namespace.{Core, Federation, Shacl, Shapes}
import amf.core.client.scala.vocabulary.{Namespace, ValueType}
import amf.core.internal.metamodel.domain.federation.{HasShapeFederationMetadataModel, ShapeFederationMetadataModel}

/** Base class for all shapes. Shapes are Domain Entities that define constraints over parts of a data graph. They can
  * be used to define and enforce schemas for the data graph information through SHACL. Shapes can be recursive and
  * inherit from other shapes.
  */
trait ShapeModel
    extends DomainElementModel
    with LinkableElementModel
    with KeyField
    with DescribedElementModel
    with HasShapeFederationMetadataModel {

  val Name: Field =
    Field(Str, Shacl + "name", ModelDoc(ExternalModelVocabularies.Shacl, "name", "Name for a data shape"))

  val DisplayName: Field =
    Field(Str, Core + "name", ModelDoc(ModelVocabularies.Core, "displayName", "Human readable name for the term"))

  val Default: Field = Field(
    DataNodeModel,
    Shacl + "defaultValue",
    ModelDoc(ExternalModelVocabularies.Shacl, "defaultValue", "Default value parsed for a data shape property")
  )

  // TODO: change namespace
  val DefaultValueString: Field = Field(
    Str,
    Shacl + "defaultValueStr",
    ModelDoc(
      ExternalModelVocabularies.Shacl,
      "defaultValueString",
      "Textual representation of the parsed default value for the shape property"
    )
  )

  val Values: Field = Field(
    SortedArray(DataNodeModel),
    Shacl + "in",
    ModelDoc(ExternalModelVocabularies.Shacl, "in", "Enumeration of possible values for a data shape property")
  )

  val Closure: Field = Field(
    Array(Iri),
    Shapes + "closure",
    ModelDoc(
      ModelVocabularies.Shapes,
      "inheritanceClosure",
      "Transitive closure of data shapes this particular shape inherits structure from"
    )
  )

  /** Inheritance relationship between shapes. Introduces the idea that the constraints defined by this shape are a
    * specialization of the constraints of the base shapes. Graphs validating this shape should also validate all the
    * constraints for the base shapes
    */
  val Inherits: Field = Field(
    Array(ShapeModel),
    Shapes + "inherits",
    ModelDoc(ModelVocabularies.Shapes, "inherits", "Relationship of inheritance between data shapes")
  )

  /** Indicates if a Shape is an extension of another shape or a standalone shape. Some API specs like GraphQL decouple
    * the schema definition from the schema extension. Shape to-be-extended might not be present in the current schema
    * (i.e. might be declared on another API). This is a similar concept to RAML Extensions at the Shape level, and
    * without the explicit reference to the extended file.
    */
  val IsExtension: Field = Field(
    Bool,
    Shapes + "isExtension",
    ModelDoc(
      ModelVocabularies.Shapes,
      "isExtension",
      "Indicates if a Shape is an extension of another shape or a standalone shape"
    )
  )

  // Logical constraints:

  val Or: Field = Field(
    Array(ShapeModel),
    Shacl + "or",
    ModelDoc(ExternalModelVocabularies.Shacl, "or", "Logical or composition of data shapes")
  )

  val And: Field = Field(
    Array(ShapeModel),
    Shacl + "and",
    ModelDoc(ExternalModelVocabularies.Shacl, "and", "Logical and composition of data shapes")
  )

  val Xone: Field = Field(
    Array(ShapeModel),
    Shacl + "xone",
    ModelDoc(ExternalModelVocabularies.Shacl, "exclusiveOr", "Logical exclusive or composition of data shapes")
  )

  val Not: Field = Field(
    ShapeModel,
    Shacl + "not",
    ModelDoc(ExternalModelVocabularies.Shacl, "not", "Logical not composition of data shapes")
  )

  val If: Field = Field(
    ShapeModel,
    Shacl + "if",
    ModelDoc(ExternalModelVocabularies.Shacl, "if", "Condition for applying composition of data shapes")
  )

  val Then: Field = Field(
    ShapeModel,
    Shacl + "then",
    ModelDoc(ExternalModelVocabularies.Shacl, "then", "Composition of data shape when if data shape is valid")
  )

  val Else: Field = Field(
    ShapeModel,
    Shacl + "else",
    ModelDoc(ExternalModelVocabularies.Shacl, "else", "Composition of data shape when if data shape is invalid")
  )

  val ReadOnly: Field =
    Field(Bool, Shapes + "readOnly", ModelDoc(ModelVocabularies.Shapes, "readOnly", "Read only property constraint"))

  val WriteOnly: Field =
    Field(Bool, Shapes + "writeOnly", ModelDoc(ModelVocabularies.Shapes, "writeOnly", "Write only property constraint"))

  val Deprecated: Field = Field(
    Bool,
    Shapes + "deprecated",
    ModelDoc(ModelVocabularies.Shapes, "deprecated", "Deprecated annotation for a property constraint")
  )

  val SerializationSchema: Field = Field(
    ShapeModel,
    Shapes + "serializationSchema",
    ModelDoc(ModelVocabularies.Shapes, "serializationSchema", "Serialization schema for a shape")
  )

  override val key: Field = Name

  // RAML user-defined facets: definitions and values
  lazy val CustomShapePropertyDefinitions: Field = Field(
    Array(PropertyShapeModel),
    Shapes + "customShapePropertyDefinitions",
    ModelDoc(
      ModelVocabularies.Shapes,
      "customShapePropertyDefinitions",
      "Custom constraint definitions added over a data shape"
    )
  )
  lazy val CustomShapeProperties: Field = Field(
    Array(ShapeExtensionModel),
    Shapes + "customShapeProperties",
    ModelDoc(ModelVocabularies.Shapes, "customShapeProperties", "Custom constraint values for a data shape")
  )
  //

  val IsStub: Field =
    Field(
      Bool,
      Federation + "isStub",
      ModelDoc(
        ModelVocabularies.Federation,
        "isStub",
        "Indicates if an element is a stub from an external component from another component of the federated graph"
      )
    )

}

object ShapeModel extends ShapeModel {

  override val fields: List[Field] = LinkableElementModel.fields ++ List(
    Name,
    DisplayName,
    Description,
    Default,
    Values,
    Inherits,
    DefaultValueString,
    Not,
    And,
    Or,
    Xone,
    Closure,
    If,
    Then,
    Else,
    ReadOnly,
    WriteOnly,
    SerializationSchema,
    Deprecated,
    IsExtension,
    FederationMetadata,
    IsStub
  )

  override val `type`: List[ValueType] = List(Shacl + "Shape", Shapes + "Shape") ++ DomainElementModel.`type`

  override def modelInstance = throw new Exception("Shape is abstract and it cannot be instantiated by default")

  override val doc: ModelDoc = ModelDoc(
    ModelVocabularies.Shapes,
    "Shape",
    "Base class for all shapes. Shapes are Domain Entities that define constraints over parts of a data graph.\nThey can be used to define and enforce schemas for the data graph information through SHACL.\nShapes can be recursive and inherit from other shapes.",
    superClasses = Seq((Namespace.Shacl + "Shape").iri())
  )
}
