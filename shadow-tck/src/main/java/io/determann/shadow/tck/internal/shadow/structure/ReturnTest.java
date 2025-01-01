package io.determann.shadow.tck.internal.shadow.structure;

import io.determann.shadow.api.shadow.C_AnnotationUsage;
import io.determann.shadow.api.shadow.C_AnnotationValue;
import io.determann.shadow.api.shadow.structure.C_Method;
import io.determann.shadow.api.shadow.structure.C_Return;
import io.determann.shadow.api.shadow.type.C_Class;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.determann.shadow.api.Operations.*;
import static io.determann.shadow.api.Provider.requestOrThrow;
import static io.determann.shadow.tck.internal.TckTest.withSource;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ReturnTest
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
                     C_Class integer = requestOrThrow(implementation, GET_CLASS, "java.lang.Integer");
                     C_Class returnExample = requestOrThrow(implementation, GET_CLASS, "ReturnExample");
                     C_Method method = requestOrThrow(returnExample, DECLARED_GET_METHODS).get(0);
                     C_Return cReturn = requestOrThrow(method, EXECUTABLE_GET_RETURN);

                     assertEquals(integer, requestOrThrow(cReturn, RETURN_GET_TYPE));
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
                     C_Class returnExample = requestOrThrow(implementation, GET_CLASS, "ReturnExample");
                     C_Method method = requestOrThrow(returnExample, DECLARED_GET_METHODS).get(0);
                     C_Return cReturn = requestOrThrow(method, EXECUTABLE_GET_RETURN);

                     List<? extends C_AnnotationUsage> usages = requestOrThrow(cReturn, ANNOTATIONABLE_GET_ANNOTATION_USAGES);
                     assertEquals(1, usages.size());
                     C_AnnotationValue annotationValue = requestOrThrow(usages.get(0), ANNOTATION_USAGE_GET_VALUE, "value");
                     assertEquals(2, requestOrThrow(annotationValue, ANNOTATION_VALUE_GET_VALUE));
                  });
   }
}