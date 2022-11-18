package org.determann.shadow.api;

import org.determann.shadow.api.modifier.Modifier;
import org.determann.shadow.api.shadow.Class;
import org.determann.shadow.api.shadow.Declared;
import org.determann.shadow.api.shadow.Field;
import org.determann.shadow.api.test.CompilationTest;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.determann.shadow.api.ShadowApi.convert;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ElementBackedTest
{
   @Test
   void getModifiersTest()
   {
      CompilationTest.process(shadowApi ->
                              {
                                 Declared arrayList = shadowApi.getClassOrThrow("java.util.ArrayList");
                                 assertEquals(Set.of(Modifier.PUBLIC), arrayList.getModifiers());

                                 Field serialVersionUID = arrayList.getFields().stream()
                                                                   .filter(field -> field.getSimpleName().equals("serialVersionUID"))
                                                                   .findAny()
                                                                   .orElseThrow();
                                 assertEquals(Set.of(Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL), serialVersionUID.getModifiers());
                              })
                     .compile();
   }

   @Test
   void getModuleTest()
   {
      CompilationTest.process(shadowApi -> assertEquals("java.base",
                                                        shadowApi.getInterfaceOrThrow("java.util.Collection").getModule().getQualifiedName()))
                     .compile();
   }

   @Test
   void getJavaDocTest()
   {
      CompilationTest.process(shadowApi ->
                              {
                                 assertNull(shadowApi.getInterfaceOrThrow("java.util.Collection").getJavaDoc());

                                 Class aClass = shadowApi.getClassOrThrow("JavaDocExample");
                                 assertEquals(" Class level doc\n", aClass.getJavaDoc());
                                 assertEquals(" Method level doc\n", aClass.getMethods("toString").get(0).getJavaDoc());
                                 assertEquals(" Constructor level doc\n", aClass.getConstructors().get(0).getJavaDoc());
                                 assertNull(convert(aClass.getGenerics().get(0)).toGeneric().get().getJavaDoc());
                                 assertNull(aClass.getConstructors().get(0).getParameters().get(0).getJavaDoc());
                              })
                     .withCodeToCompile("JavaDocExample.java", """
                           /**
                            * Class level doc
                            */
                           public class JavaDocExample</** a */T>
                           {
                              /**
                               * Field level doc
                               */
                              private Long id;

                              /**
                               * Constructor level doc
                               */
                              public JavaDocExample(/** */ Long id)
                              {
                              }

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
