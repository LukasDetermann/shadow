package io.determann.shadow.api;

import io.determann.shadow.api.shadow.Annotation;
import io.determann.shadow.api.shadow.AnnotationUsage;

import java.util.List;
import java.util.Optional;

/**
 * anything that can be annotated
 */
public interface Annotationable
{
   /**
    * returns all annotations. Annotations on parentClasses are included when they are annotated with {@link java.lang.annotation.Inherited}
    */
   List<AnnotationUsage> getAnnotationUsages();

   default List<AnnotationUsage> getUsagesOf(Annotation annotation)
   {
      return getAnnotationUsages().stream()
                                  .filter(usage -> usage.getAnnotation().equals(annotation))
                                  .toList();
   }

   default Optional<AnnotationUsage> getUsageOf(Annotation annotation)
   {
      List<AnnotationUsage> usages = getUsagesOf(annotation);

      if (usages.isEmpty())
      {
         return Optional.empty();
      }
      if (usages.size() == 1)
      {
         return Optional.of(usages.get(0));
      }
      throw new IllegalArgumentException();
   }

   default AnnotationUsage getUsageOfOrThrow(Annotation annotation)
   {
      return getUsageOf(annotation).orElseThrow(IllegalArgumentException::new);
   }

   default boolean isAnnotatedWith(Annotation annotation)
   {
      return getAnnotationUsages().stream()
                                  .map(AnnotationUsage::getAnnotation)
                                  .anyMatch(annotation1 -> annotation1.equals(annotation));
   }

   /**
    * returns all direkt annotations
    *
    * @see #getAnnotationUsages()
    */
   List<AnnotationUsage> getDirectAnnotationUsages();

   default List<AnnotationUsage> getDirectUsagesOf(Annotation annotation)
   {
      return getDirectAnnotationUsages().stream()
                                        .filter(usage -> usage.getAnnotation().equals(annotation))
                                        .toList();
   }

   default Optional<AnnotationUsage> getDirectUsageOf(Annotation annotation)
   {
      return getDirectAnnotationUsages().stream()
                                        .filter(usage -> usage.getAnnotation().equals(annotation))
                                        .findAny();
   }

   default AnnotationUsage getDirectUsageOfOrThrow(Annotation annotation)
   {
      return getDirectUsageOf(annotation).orElseThrow();
   }

   default boolean isDirectlyAnnotatedWith(Annotation annotation)
   {
      return getDirectAnnotationUsages().stream()
                                        .map(AnnotationUsage::getAnnotation)
                                        .anyMatch(annotation1 -> annotation1.equals(annotation));
   }
}
