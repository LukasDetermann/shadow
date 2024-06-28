package io.determann.shadow.consistency;

import io.determann.shadow.api.annotation_processing.test.ProcessorTest;
import io.determann.shadow.api.shadow.Documented;
import io.determann.shadow.api.shadow.ModuleEnclosed;
import io.determann.shadow.api.shadow.Nameable;
import io.determann.shadow.api.shadow.QualifiedNameable;
import io.determann.shadow.api.shadow.modifier.Modifier;
import io.determann.shadow.api.shadow.structure.Field;
import io.determann.shadow.api.shadow.type.Class;
import io.determann.shadow.api.shadow.type.Declared;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static io.determann.shadow.api.lang_model.LangModelQueries.query;
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

                               Field serialVersionUID = query(arrayList).getFields().stream()
                                                                 .filter(field -> query((Nameable) field).getName().equals("serialVersionUID"))
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
                                                      query((QualifiedNameable) query((ModuleEnclosed) shadowApi.getInterfaceOrThrow("java.util.Collection")).getModule()).getQualifiedName()))
                   .compile();
   }

   @Test
   void getJavaDocTest()
   {
      ProcessorTest.process(shadowApi ->
                            {
                               assertNull(query((Documented) shadowApi.getInterfaceOrThrow("java.util.Collection")).getJavaDoc());

                               Class aClass = shadowApi.getClassOrThrow("JavaDocExample");
                               assertEquals(" Class level doc\n", query((Documented) aClass).getJavaDoc());
                               assertEquals(" Method level doc\n", query((Documented) query(aClass).getMethods("toString").get(0)).getJavaDoc());
                               assertEquals(" Constructor level doc\n", query((Documented) query(aClass).getConstructors().get(0)).getJavaDoc());
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
