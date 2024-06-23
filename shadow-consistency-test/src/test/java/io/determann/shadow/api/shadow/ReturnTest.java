package io.determann.shadow.api.shadow;

import io.determann.shadow.api.annotation_processing.test.ProcessorTest;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.lang_model.LangModelQueries.query;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ReturnTest
{
   @Test
   void getType()
   {
      ProcessorTest.process(context ->
                            {
                               Return aReturn = query(query(context.getClassOrThrow("ReturnExample")).getMethods().get(0)).getReturn();

                               assertEquals(context.getClassOrThrow("java.lang.Integer"), query(aReturn).getType());
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
                               Return aReturn = query(query(context.getClassOrThrow("ReturnExample")).getMethods().get(0)).getReturn();

                               assertEquals(1, aReturn.getAnnotationUsages().size());
                               assertEquals(2, query(aReturn.getAnnotationUsages().get(0)).getValueOrThrow("value").asInteger());
                            })
                   .withCodeToCompile("MyAnnotation.java", "@java.lang.annotation.Target(java.lang.annotation.ElementType.TYPE_USE) @interface MyAnnotation{int value();}")
                   .withCodeToCompile("ReturnExample.java", """
                         abstract class ReturnExample {
                            public abstract @MyAnnotation(2) Integer method();
                         }
                         """)
                   .compile();
   }
}