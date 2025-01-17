package amf.core.internal.validation

import amf.core.client.common.validation._
import SeverityLevels.{INFO, VIOLATION, WARNING}
import amf.core.internal.validation.core.ValidationSpecification
import amf.core.internal.validation.core.ValidationSpecification.CORE_VALIDATION
import amf.core.client.scala.vocabulary.Namespace
import amf.core.client.scala.vocabulary.Namespace.AmfCore

// noinspection TypeAnnotation
object CoreValidations extends Validations {
  override val specification: String = CORE_VALIDATION
  override val namespace: Namespace  = AmfCore

  val CycleReferenceError = validation(
      "cycle-reference",
      "Cycle in references"
  )

  val InvalidCrossSpec = validation(
      "invalid-cross-spec",
      "Cross spec file usage is not allowed"
  )

  val UnresolvedReference = validation(
      "unresolved-reference",
      "Unresolved reference"
  )

  val UnresolvedReferenceWarning = validation(
      "unresolved-reference-warning",
      "Unresolved reference"
  )

  val UriSyntaxError = validation(
      "uri-syntax-error",
      "invalid uri syntax"
  )

  val InvalidFragmentRef = validation(
      "invalid-fragment-ref",
      "References with # in RAML are not allowed"
  )

  val DeclarationNotFound = validation(
      "declaration-not-found",
      "Declaration not found"
  )

  val SyamlError = validation(
      "syaml-error",
      "Syaml error"
  )

  val SyamlWarning = validation(
      "syaml-warning",
      "Syaml warning"
  )

  val ExpectedModule = validation(
      "expected-module",
      "Expected Module"
  )

  val InvalidInclude = validation(
      "invalid-include",
      "Invalid !include value"
  )

  val UnableToParseNode = validation(
      "parse-node-fail",
      "JsonLD @types failed to parse in node"
  )

  val UnableToConvertToScalar = validation(
      "unable-to-convert-scalar",
      "Unable to convert scalar"
  )

  val UnableToParseRdfDocument = validation(
      "parse-rdf-document-fail",
      "Unable to parse rdf document"
  )

  val NodeNotFound = validation(
      "node-not-found",
      "Builder for model not found"
  )

  val NotLinkable = validation(
      "not-linkable",
      "Only linkable elements can be linked"
  )

  val UnableToParseDocument = validation(
      "parse-document-fail",
      "Unable to parse document"
  )

  val InvalidRootStructure = validation(
      "invalid-root-structure",
      "Unable to parse map at given structure"
  )

  val UnableToParseDomainElement = validation(
      "parse-domain-element-fail",
      "Parsed element for @id is not a domain element"
  )

  val MissingIdInNode = validation(
      "missing-id-in-node",
      "Missing @id in json-ld node"
  )

  val MissingTypeInNode = validation(
      "missing-type-in-node",
      "Missing @type in json-ld node"
  )

  // Used in transformation
  val RecursiveShapeSpecification = validation(
      "recursive-shape",
      "Recursive shape",
      Some("Recursive type"),
      Some("Recursive schema")
  )

  // Used in transformation
  val TransformationValidation = validation(
      "transformation-validation",
      "Default transformation validation"
  )

  val UnhandledDomainElement = validation(
      "unhandled-element",
      "Unhandled domain element for given spec"
  )

  val ExceededMaxYamlReferences = validation(
      "max-yaml-references",
      "Exceeded maximum yaml references threshold"
  )

  override val levels: Map[String, Map[ProfileName, String]] = Map(
      SyamlWarning.id               -> all(WARNING),
      UnresolvedReferenceWarning.id -> all(WARNING),
      RecursiveShapeSpecification.id -> Map(
          Raml10Profile -> VIOLATION,
          Raml08Profile -> VIOLATION,
          Oas20Profile  -> VIOLATION,
          Oas30Profile  -> VIOLATION,
          AmfProfile    -> INFO
      )
  )

  override val validations: List[ValidationSpecification] = List(
      CycleReferenceError,
      NotLinkable,
      UnresolvedReference,
      UnresolvedReferenceWarning,
      SyamlError,
      SyamlWarning,
      NodeNotFound,
      UnableToParseDocument,
      UnableToParseNode,
      ExpectedModule,
      MissingIdInNode,
      MissingTypeInNode,
      UriSyntaxError,
      UnableToParseRdfDocument,
      DeclarationNotFound,
      InvalidInclude,
      InvalidCrossSpec,
      InvalidFragmentRef,
      RecursiveShapeSpecification,
      TransformationValidation,
      UnhandledDomainElement,
      ExceededMaxYamlReferences
  )
}
