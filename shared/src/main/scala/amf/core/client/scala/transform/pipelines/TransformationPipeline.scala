package amf.core.client.scala.transform.pipelines

import amf.core.client.scala.errorhandling.AMFErrorHandler
import amf.core.client.scala.config.{
  AMFEvent,
  AMFEventListener,
  FinishedTransformationEvent,
  FinishedTransformationStepEvent,
  StartingTransformationEvent
}
import amf.core.client.scala.model.document.BaseUnit
import amf.core.client.scala.transform.stages.TransformationStep
import amf.core.internal.benchmark.ExecutionLog

trait TransformationPipeline {
  val name: String
  def steps: Seq[TransformationStep]
}

// transformation pipelines can only run internally within amf.
private[amf] case class TransformationPipelineRunner(errorHandler: AMFErrorHandler,
                                                     listeners: Seq[AMFEventListener] = Nil) {

  private def notifyEvent(e: AMFEvent): Unit = listeners.foreach(_.notifyEvent(e))

  def run(model: BaseUnit, pipeline: TransformationPipeline): BaseUnit = {
    ExecutionLog.log(s"${this.getClass.getName}#resolve: resolving ${model.location().getOrElse("")}")
    notifyEvent(StartingTransformationEvent(pipeline))
    var m     = model
    val steps = pipeline.steps
    steps.zipWithIndex foreach {
      case (s, index) =>
        m = step(m, s, errorHandler)
        notifyEvent(FinishedTransformationStepEvent(s, index))
    }
    // TODO: should be unit metadata
    m.resolved = true
    notifyEvent(FinishedTransformationEvent(m))
    ExecutionLog.log(s"${this.getClass.getName}#resolve: resolved model ${m.location().getOrElse("")}")
    m
  }

  private def step(unit: BaseUnit, step: TransformationStep, errorHandler: AMFErrorHandler): BaseUnit = {
    ExecutionLog.log(s"ResolutionPipeline#step: applying resolution stage ${step.getClass.getName}")
    val resolved = step.transform(unit, errorHandler)
    ExecutionLog.log(s"ResolutionPipeline#step: finished applying stage ${step.getClass.getName}")
    resolved
  }
}
