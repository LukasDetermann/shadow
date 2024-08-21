package io.determann.shadow.consistency;

import io.determann.shadow.api.annotation_processing.test.ProcessorTest;
import io.determann.shadow.api.lang_model.shadow.structure.LM_Field;
import io.determann.shadow.api.lang_model.shadow.type.LM_Class;
import io.determann.shadow.api.lang_model.shadow.type.LM_Declared;
import io.determann.shadow.api.shadow.modifier.C_Modifier;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ElementBackedTest
{
   @Test
   void getModifiersTest()
   {
      ProcessorTest.process(context ->
                            {
                               LM_Declared arrayList = context.getClassOrThrow("java.util.ArrayList");
                               assertEquals(Set.of(C_Modifier.PUBLIC), arrayList.getModifiers());

                               LM_Field serialVersionUID = arrayList.getFields().stream()
                                                                    .filter(field -> field.getName().equals("serialVersionUID"))
                                                                    .findAny()
                                                                    .orElseThrow();
                               assertEquals(Set.of(C_Modifier.PRIVATE, C_Modifier.STATIC, C_Modifier.FINAL), serialVersionUID.getModifiers());
                            })
                   .compile();
   }

   @Test
   void getModuleTest()
   {
      ProcessorTest.process(context -> assertEquals("java.base",
                                                      context.getInterfaceOrThrow("java.util.Collection").getModule().getQualifiedName()))
                   .compile();
   }

   @Test
   void getJavaDocTest()
   {
      ProcessorTest.process(context ->
                            {
                               assertNull(context.getInterfaceOrThrow("java.util.Collection").getJavaDoc());

                               LM_Class aClass = context.getClassOrThrow("JavaDocExample");
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
