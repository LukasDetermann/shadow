package io.determann.shadow.api.lang_model.shadow;

import io.determann.shadow.api.shadow.Annotationable;
import io.determann.shadow.api.shadow.type.Annotation;

import java.util.List;
import java.util.Optional;

import static io.determann.shadow.api.Operations.ANNOTATION_USAGE_GET_ANNOTATION;
import static io.determann.shadow.api.Provider.requestOrThrow;

/**
 * anything that can be annotated
 */
public interface AnnotationableLangModel extends Annotationable
{
   /**
    * returns all annotations. Annotations on parentClasses are included when they are annotated with {@link java.lang.annotation.Inherited}
    */
   List<AnnotationUsageLangModel> getAnnotationUsages();

   default List<AnnotationUsageLangModel> getUsagesOf(Annotation annotation)
   {
      return getAnnotationUsages().stream()
                                  .filter(usage -> requestOrThrow(usage, ANNOTATION_USAGE_GET_ANNOTATION).equals(annotation))
                                  .toList();
   }

   default Optional<AnnotationUsageLangModel> getUsageOf(Annotation annotation)
   {
      List<AnnotationUsageLangModel> usages = getUsagesOf(annotation);

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

   default AnnotationUsageLangModel getUsageOfOrThrow(Annotation annotation)
   {
      return getUsageOf(annotation).orElseThrow(IllegalArgumentException::new);
   }

   default boolean isAnnotatedWith(Annotation annotation)
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
   List<AnnotationUsageLangModel> getDirectAnnotationUsages();

   default List<AnnotationUsageLangModel> getDirectUsagesOf(Annotation annotation)
   {
      return getDirectAnnotationUsages().stream()
                                        .filter(usage -> requestOrThrow(usage, ANNOTATION_USAGE_GET_ANNOTATION).equals(annotation))
                                        .toList();
   }

   default Optional<AnnotationUsageLangModel> getDirectUsageOf(Annotation annotation)
   {
      return getDirectAnnotationUsages().stream()
                                        .filter(usage -> requestOrThrow(usage, ANNOTATION_USAGE_GET_ANNOTATION).equals(annotation))
                                        .findAny();
   }

   default AnnotationUsageLangModel getDirectUsageOfOrThrow(Annotation annotation)
   {
      return getDirectUsageOf(annotation).orElseThrow();
   }

   default boolean isDirectlyAnnotatedWith(Annotation annotation)
   {
      return getDirectAnnotationUsages().stream()
                                        .map(usage -> requestOrThrow(usage, ANNOTATION_USAGE_GET_ANNOTATION))
                                        .anyMatch(annotation1 -> annotation1.equals(annotation));
   }
}
