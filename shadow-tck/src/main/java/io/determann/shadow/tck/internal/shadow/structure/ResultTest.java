package io.determann.shadow.tck.internal.shadow.structure;

import io.determann.shadow.api.C;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.determann.shadow.api.query.Operations.*;
import static io.determann.shadow.api.query.Provider.requestOrThrow;
import static io.determann.shadow.tck.internal.TckTest.withSource;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ResultTest
{
   @Test
   void getType()
   {
      withSource("ReturnExample.java", """
            abstract class ReturnExample {
               public abstract Integer method();
            }
            """)
            .test(implementation ->
                  {
                     C.Class integer = requestOrThrow(implementation, GET_CLASS, "java.lang.Integer");
                     C.Class returnExample = requestOrThrow(implementation, GET_CLASS, "ReturnExample");
                     C.Method method = requestOrThrow(returnExample, DECLARED_GET_METHODS).get(0);
                     C.Result cReturn = requestOrThrow(method, METHOD_GET_RESULT);

                     assertEquals(integer, requestOrThrow(cReturn, RESULT_GET_TYPE));
                  });
   }

   @Test
   void getAnnotationUsages()
   {
      withSource("MyAnnotation.java",
                 "@java.lang.annotation.Target(java.lang.annotation.ElementType.TYPE_USE) @interface MyAnnotation{int value();}")
            .withSource("ReturnExample.java", """
                  abstract class ReturnExample {
                     public abstract @MyAnnotation(2) Integer method();
                  }
                  """)
            .test(implementation ->
                  {
                     C.Class returnExample = requestOrThrow(implementation, GET_CLASS, "ReturnExample");
                     C.Method method = requestOrThrow(returnExample, DECLARED_GET_METHODS).get(0);
                     C.Result cReturn = requestOrThrow(method, METHOD_GET_RESULT);

                     List<? extends C.AnnotationUsage> usages = requestOrThrow(cReturn, ANNOTATIONABLE_GET_ANNOTATION_USAGES);
                     assertEquals(1, usages.size());
                     C.AnnotationValue annotationValue = requestOrThrow(usages.get(0), ANNOTATION_USAGE_GET_VALUE, "value");
                     assertEquals(2, requestOrThrow(annotationValue, ANNOTATION_VALUE_GET_VALUE));
                  });
   }
}