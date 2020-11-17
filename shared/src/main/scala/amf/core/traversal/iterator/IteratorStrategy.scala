package amf.core.traversal.iterator

import amf.core.model.domain.AmfElement

trait IteratorStrategy {
  def iterator(elements: List[AmfElement], visited: VisitedCollector): AmfIterator
}

object AmfElementStrategy extends IteratorStrategy {
  override def iterator(elements: List[AmfElement], visited: VisitedCollector): AmfIterator =
    AmfElementIterator(elements, visited)
}

object DomainElementStrategy extends IteratorStrategy {
  override def iterator(elements: List[AmfElement], visited: VisitedCollector): AmfIterator =
    DomainElementIterator(elements, visited)
}
