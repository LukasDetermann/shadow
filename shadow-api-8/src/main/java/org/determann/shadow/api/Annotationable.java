package org.determann.shadow.api;

import org.determann.shadow.api.shadow.AnnotationUsage;

import javax.lang.model.element.Element;
import java.util.List;

/**
 * anything that can be annotated
 */
public interface Annotationable<ELEMENT extends Element> extends ElementBacked<ELEMENT>
{
   /**
    * returns all annotations. Annotations on parentClasses are included when they are annotated with {@link java.lang.annotation.Inherited}
    */
   default List<AnnotationUsage> getAnnotations()
   {
      return getApi()
            .getShadowFactory()
            .annotationUsage(getApi().getJdkApiContext().elements().getAllAnnotationMirrors(getElement()));
   }

   /**
    * returns all direkt annotations
    *
    * @see #getAnnotations()
    */
   default List<AnnotationUsage> getDirectAnnotations()
   {
      return getApi().getShadowFactory().annotationUsage(getElement().getAnnotationMirrors());
   }
}
