package io.determann.shadow.api.lang_model.shadow;

import io.determann.shadow.api.shadow.C_Annotationable;
import io.determann.shadow.api.shadow.type.C_Annotation;

import java.util.List;
import java.util.Optional;

import static io.determann.shadow.api.Operations.ANNOTATION_USAGE_GET_ANNOTATION;
import static io.determann.shadow.api.Provider.requestOrThrow;

/**
 * anything that can be annotated
 */
public interface LM_Annotationable extends C_Annotationable
{
   /**
    * returns all annotations. Annotations on parentClasses are included when they are annotated with {@link java.lang.annotation.Inherited}
    */
   List<LM_AnnotationUsage> getAnnotationUsages();

   default List<LM_AnnotationUsage> getUsagesOf(C_Annotation annotation)
   {
      return getAnnotationUsages().stream()
                                  .filter(usage -> requestOrThrow(usage, ANNOTATION_USAGE_GET_ANNOTATION).equals(annotation))
                                  .toList();
   }

   default Optional<LM_AnnotationUsage> getUsageOf(C_Annotation annotation)
   {
      List<LM_AnnotationUsage> usages = getUsagesOf(annotation);

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

   default LM_AnnotationUsage getUsageOfOrThrow(C_Annotation annotation)
   {
      return getUsageOf(annotation).orElseThrow(IllegalArgumentException::new);
   }

   default boolean isAnnotatedWith(C_Annotation annotation)
   {
      return getAnnotationUsages().stream()
                                  .map(usage -> requestOrThrow(usage, ANNOTATION_USAGE_GET_ANNOTATION))
                                  .anyMatch(annotation1 -> annotation1.equals(annotation));
   }

   /**
    * returns all direkt annotations
    *
    * @see #getAnnotationUsages()
    */
   List<LM_AnnotationUsage> getDirectAnnotationUsages();

   default List<LM_AnnotationUsage> getDirectUsagesOf(C_Annotation annotation)
   {
      return getDirectAnnotationUsages().stream()
                                        .filter(usage -> requestOrThrow(usage, ANNOTATION_USAGE_GET_ANNOTATION).equals(annotation))
                                        .toList();
   }

   default Optional<LM_AnnotationUsage> getDirectUsageOf(C_Annotation annotation)
   {
      return getDirectAnnotationUsages().stream()
                                        .filter(usage -> requestOrThrow(usage, ANNOTATION_USAGE_GET_ANNOTATION).equals(annotation))
                                        .findAny();
   }

   default LM_AnnotationUsage getDirectUsageOfOrThrow(C_Annotation annotation)
   {
      return getDirectUsageOf(annotation).orElseThrow();
   }

   default boolean isDirectlyAnnotatedWith(C_Annotation annotation)
   {
      return getDirectAnnotationUsages().stream()
                                        .map(usage -> requestOrThrow(usage, ANNOTATION_USAGE_GET_ANNOTATION))
                                        .anyMatch(annotation1 -> annotation1.equals(annotation));
   }
}
