package io.determann.shadow.tck.internal;

import io.determann.shadow.api.C;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.determann.shadow.api.query.Operations.*;
import static io.determann.shadow.api.query.Provider.requestOrEmpty;
import static io.determann.shadow.api.query.Provider.requestOrThrow;
import static io.determann.shadow.tck.internal.TckTest.withSource;
import static org.junit.jupiter.api.Assertions.*;

class AnnotationableTest
{
   @Test
   void emptyAnnotations()
   {
      withSource("NotAnnotated.java", "class NotAnnotated{}")
            .test(implementation ->
                  {
                     C.Declared cDeclared = requestOrThrow(implementation, GET_DECLARED, "NotAnnotated");
                     assertTrue(requestOrThrow(cDeclared, ANNOTATIONABLE_GET_ANNOTATION_USAGES).isEmpty());
                     assertTrue(requestOrThrow(cDeclared, ANNOTATIONABLE_GET_DIRECT_ANNOTATION_USAGES).isEmpty());
                  });
   }

   @Test
   void inheritance()
   {
      withSource("InheritedAnnotation.java", "@java.lang.annotation.Inherited @interface InheritedAnnotation{}")
            .withSource("StandaloneAnnotation.java", "@interface StandaloneAnnotation{}")
            .withSource("Parent.java", "@InheritedAnnotation @StandaloneAnnotation class Parent{}")
            .withSource("Child.java", "class Child extends Parent{}")
            .test(implementation ->
                  {
                     C.Declared cDeclared = requestOrThrow(implementation, GET_DECLARED, "Child");
                     C.Annotation standaloneAnnotation = requestOrThrow(implementation, GET_ANNOTATION, "StandaloneAnnotation");
                     C.Annotation inheritedAnnotation = requestOrThrow(implementation, GET_ANNOTATION, "InheritedAnnotation");

                     //direct
                     assertTrue(requestOrThrow(cDeclared, ANNOTATIONABLE_GET_DIRECT_ANNOTATION_USAGES).isEmpty());
                     assertFalse(requestOrThrow(cDeclared, ANNOTATIONABLE_IS_DIRECTLY_ANNOTATED_WITH, standaloneAnnotation));
                     assertFalse(requestOrThrow(cDeclared, ANNOTATIONABLE_IS_DIRECTLY_ANNOTATED_WITH, inheritedAnnotation));

                     assertTrue(requestOrEmpty(cDeclared, ANNOTATIONABLE_GET_DIRECT_USAGE_OF, standaloneAnnotation).isEmpty());
                     assertTrue(requestOrEmpty(cDeclared, ANNOTATIONABLE_GET_DIRECT_USAGE_OF, inheritedAnnotation).isEmpty());

                     //inherited
                     List<? extends C.AnnotationUsage> usages = requestOrThrow(cDeclared, ANNOTATIONABLE_GET_ANNOTATION_USAGES);
                     assertEquals(1, usages.size());
                     C.Annotation cAnnotation = requestOrThrow(usages.get(0), ANNOTATION_USAGE_GET_ANNOTATION);
                     assertEquals("InheritedAnnotation", requestOrThrow(cAnnotation, QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME));
                     assertFalse(requestOrThrow(cDeclared, ANNOTATIONABLE_IS_ANNOTATED_WITH, standaloneAnnotation));
                     assertTrue(requestOrThrow(cDeclared, ANNOTATIONABLE_IS_ANNOTATED_WITH, inheritedAnnotation));

                     assertTrue(requestOrEmpty(cDeclared, ANNOTATIONABLE_GET_USAGE_OF, standaloneAnnotation).isEmpty());
                     assertTrue(requestOrEmpty(cDeclared, ANNOTATIONABLE_GET_USAGE_OF, inheritedAnnotation).isPresent());
                  });
   }
}
