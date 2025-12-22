package io.determann.shadow.annotation_processing.shadow.structure;

import io.determann.shadow.api.annotation_processing.Ap;
import io.determann.shadow.api.annotation_processing.test.ProcessorTest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ResultTest
{
   @Test
   void getType()
   {
      ProcessorTest.process(context ->
                            {
                               Ap.Class integer = context.getClassOrThrow("java.lang.Integer");
                               Ap.Class returnExample = context.getClassOrThrow("ReturnExample");
                               Ap.Method method = returnExample.getMethods().get(0);
                               Ap.Result cReturn = method.getResult();

                               assertEquals(integer, cReturn.getType());
                            })
                   .withCodeToCompile("ReturnExample.java", """
                                                            abstract class ReturnExample {
                                                               public abstract Integer method();
                                                            }
                                                            """)
                   .compile();
   }

   @Test
   void getAnnotationUsages()
   {
      ProcessorTest.process(context ->
                            {
                               Ap.Class returnExample = context.getClassOrThrow("ReturnExample");
                               Ap.Method method = returnExample.getMethods().get(0);
                               Ap.Result cReturn = method.getResult();

                               List<Ap.AnnotationUsage> usages = cReturn.getAnnotationUsages();
                               assertEquals(1, usages.size());
                               Ap.AnnotationValue annotationValue = usages.get(0).getValueOrThrow("value");
                               assertEquals(2, annotationValue.getValue());
                            })
                   .withCodeToCompile("MyAnnotation.java",
                                      "@java.lang.annotation.Target(java.lang.annotation.ElementType.TYPE_USE) @interface MyAnnotation{int value();}")
                   .withCodeToCompile("ReturnExample.java", """
                                                            abstract class ReturnExample {
                                                               public abstract @MyAnnotation(2) Integer method();
                                                            }
                                                            """)
                   .compile();
   }
}