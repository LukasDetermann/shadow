package io.determann.shadow.consistency;

import io.determann.shadow.api.annotation_processing.test.ProcessorTest;
import io.determann.shadow.api.lang_model.shadow.structure.FieldLangModel;
import io.determann.shadow.api.lang_model.shadow.type.ClassLangModel;
import io.determann.shadow.api.lang_model.shadow.type.DeclaredLangModel;
import io.determann.shadow.api.shadow.modifier.Modifier;
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
                               DeclaredLangModel arrayList = context.getClassOrThrow("java.util.ArrayList");
                               assertEquals(Set.of(Modifier.PUBLIC), arrayList.getModifiers());

                               FieldLangModel serialVersionUID = arrayList.getFields().stream()
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

                               ClassLangModel aClass = context.getClassOrThrow("JavaDocExample");
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
