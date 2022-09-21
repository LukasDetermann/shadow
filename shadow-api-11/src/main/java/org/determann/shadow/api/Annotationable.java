package org.determann.shadow.api;

import org.determann.shadow.api.shadow.AnnotationUsage;
import org.jetbrains.annotations.UnmodifiableView;

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
   default @UnmodifiableView List<AnnotationUsage> getAnnotationUsages()
   {
      return getApi()
            .getShadowFactory()
            .annotationUsage(getApi().getJdkApiContext().elements().getAllAnnotationMirrors(getElement()));
   }

   /**
    * returns all direkt annotations
    *
    * @see #getAnnotationUsages()
    */
   default @UnmodifiableView List<AnnotationUsage> getDirectAnnotationUsages()
   {
      return getApi().getShadowFactory().annotationUsage(getElement().getAnnotationMirrors());
   }
}
