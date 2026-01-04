package io.determann.shadow.annotation_processing;

import io.determann.shadow.api.annotation_processing.Ap;
import io.determann.shadow.api.annotation_processing.test.ProcessorTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DocumentedTest
{
   @Test
   void getJavaDoc()
   {
      ProcessorTest.process(context ->
                            {
                               assertTrue(context.getInterfaceOrThrow("java.util.Collection").getJavaDoc().isEmpty());

                               Ap.Class aClass = context.getClassOrThrow("JavaDocExample");
                               assertEquals(" Class level doc\n", aClass.getJavaDoc().orElseThrow());
                               assertEquals(" Method level doc\n", aClass.getMethods("toString").get(0).getJavaDoc().orElseThrow());
                               assertEquals(" Constructor level doc\n", aClass.getConstructors().get(0).getJavaDoc().orElseThrow());
                            })
                   .withCodeToCompile("JavaDocExample.java", """
                         /**
                          * Class level doc
                          */
                         public class JavaDocExample<T>
                         {
                            /**
                             * Field level doc
                             */
                            private Long id;

                            /**
                             * Constructor level doc
                             */
                            public JavaDocExample() {}

                            /**
                             * Method level doc
                             */
                            @Override
                            public String toString()
                            {
                               return "JavaDocExample{}";
                            }
                         }
                         """)
                   .compile();
   }
}
