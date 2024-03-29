package io.determann.shadow.api;

import io.determann.shadow.api.annotation_processing.test.ProcessorTest;
import io.determann.shadow.api.modifier.Modifier;
import io.determann.shadow.api.shadow.Class;
import io.determann.shadow.api.shadow.Declared;
import io.determann.shadow.api.shadow.Field;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ElementBackedTest
{
   @Test
   void getModifiersTest()
   {
      ProcessorTest.process(shadowApi ->
                            {
                               Declared arrayList = shadowApi.getClassOrThrow("java.util.ArrayList");
                               assertEquals(Set.of(Modifier.PUBLIC), arrayList.getModifiers());

                               Field serialVersionUID = arrayList.getFields().stream()
                                                                 .filter(field -> field.getName().equals("serialVersionUID"))
                                                                 .findAny()
                                                                 .orElseThrow();
                               assertEquals(Set.of(Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL), serialVersionUID.getModifiers());
                            })
                   .compile();
   }

   @Test
   void getModuleTest()
   {
      ProcessorTest.process(shadowApi -> assertEquals("java.base",
                                                      shadowApi.getInterfaceOrThrow("java.util.Collection").getModule().getQualifiedName()))
                   .compile();
   }

   @Test
   void getJavaDocTest()
   {
      ProcessorTest.process(shadowApi ->
                            {
                               assertNull(shadowApi.getInterfaceOrThrow("java.util.Collection").getJavaDoc());

                               Class aClass = shadowApi.getClassOrThrow("JavaDocExample");
                               assertEquals(" Class level doc\n", aClass.getJavaDoc());
                               assertEquals(" Method level doc\n", aClass.getMethods("toString").get(0).getJavaDoc());
                               assertEquals(" Constructor level doc\n", aClass.getConstructors().get(0).getJavaDoc());
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
