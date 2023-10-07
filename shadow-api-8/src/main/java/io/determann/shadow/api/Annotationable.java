package io.determann.shadow.api;

import io.determann.shadow.api.metadata.JdkApi;
import io.determann.shadow.api.modifier.Modifiable;
import io.determann.shadow.api.modifier.Modifier;
import io.determann.shadow.api.shadow.Annotation;
import io.determann.shadow.api.shadow.AnnotationUsage;

import javax.lang.model.element.Element;
import javax.tools.Diagnostic;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static io.determann.shadow.api.modifier.Modifier.*;
import static java.util.stream.Collectors.*;

/**
 * anything that can be annotated
 */
public interface Annotationable<ELEMENT extends Element> extends Modifiable,
                                                                 ApiHolder
{
   @JdkApi
   ELEMENT getElement();

   @Override
   default Set<Modifier> getModifiers()
   {
      return getElement().getModifiers()
                         .stream()
                         .map(Annotationable::mapModifier)
                         .collect(collectingAndThen(toSet(), Collections::unmodifiableSet));
   }

   public static Modifier mapModifier(javax.lang.model.element.Modifier modifier)
   {
      switch (modifier)
      {
         case PUBLIC:
            return PUBLIC;
         case PROTECTED:
            return PROTECTED;
         case PRIVATE:
            return PRIVATE;
         case ABSTRACT:
            return ABSTRACT;
         case STATIC:
            return STATIC;
         case FINAL:
            return FINAL;
         case STRICTFP:
            return STRICTFP;
         case DEFAULT:
            return DEFAULT;
         case TRANSIENT:
            return TRANSIENT;
         case VOLATILE:
            return VOLATILE;
         case SYNCHRONIZED:
            return SYNCHRONIZED;
         case NATIVE:
            return NATIVE;
         default:
            throw new IllegalArgumentException();
      }
   }

   default String getSimpleName()
   {
      return getElement().getSimpleName().toString();
   }

   /**
    * returns the javaDoc or null if none is present
    */
   default String getJavaDoc()
   {
      return getApi().getJdkApiContext().getProcessingEnv().getElementUtils().getDocComment(getElement());
   }

   default void logError(String msg)
   {
      getApi().getJdkApiContext().getProcessingEnv().getMessager().printMessage(Diagnostic.Kind.ERROR, msg, getElement());
   }

   default void logInfo(String msg)
   {
      getApi().getJdkApiContext().getProcessingEnv().getMessager().printMessage(Diagnostic.Kind.NOTE, msg, getElement());
   }

   default void logWarning(String msg)
   {
      getApi().getJdkApiContext().getProcessingEnv().getMessager().printMessage(Diagnostic.Kind.MANDATORY_WARNING, msg, getElement());
   }

   /**
    * returns all annotations. Annotations on parentClasses are included when they are annotated with {@link java.lang.annotation.Inherited}
    */
   default List<AnnotationUsage> getAnnotationUsages()
   {
      return getApi()
            .getShadowFactory()
            .annotationUsages(getApi().getJdkApiContext().getProcessingEnv().getElementUtils().getAllAnnotationMirrors(getElement()));
   }

   default List<AnnotationUsage> getUsagesOf(Annotation annotation)
   {
      return getAnnotationUsages().stream()
                                  .filter(usage -> usage.getAnnotation().equals(annotation))
                                  .collect(collectingAndThen(toList(), Collections::unmodifiableList));
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
   default List<AnnotationUsage> getDirectAnnotationUsages()
   {
      return getApi().getShadowFactory().annotationUsages(getElement().getAnnotationMirrors());
   }

   default List<AnnotationUsage> getDirectUsagesOf(Annotation annotation)
   {
      return getDirectAnnotationUsages().stream()
                                        .filter(usage -> usage.getAnnotation().equals(annotation))
                                        .collect(collectingAndThen(toList(), Collections::unmodifiableList));
   }

   default Optional<AnnotationUsage> getDirectUsageOf(Annotation annotation)
   {
      return getDirectAnnotationUsages().stream()
                                        .filter(usage -> usage.getAnnotation().equals(annotation))
                                        .findAny();
   }

   default AnnotationUsage getDirectUsageOfOrThrow(Annotation annotation)
   {
      return getDirectUsageOf(annotation).orElseThrow(IllegalArgumentException::new);
   }

   default boolean isDirectlyAnnotatedWith(Annotation annotation)
   {
      return getDirectAnnotationUsages().stream()
                                        .map(AnnotationUsage::getAnnotation)
                                        .anyMatch(annotation1 -> annotation1.equals(annotation));
   }
}
