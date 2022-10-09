package org.determann.shadow.api;

import org.determann.shadow.api.modifier.Modifier;
import org.determann.shadow.api.shadow.Class;
import org.determann.shadow.api.shadow.Declared;
import org.determann.shadow.api.shadow.Field;
import org.determann.shadow.api.test.CompilationTest;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ElementBackedTest
{
   @Test
   void getModifiersTest()
   {
      CompilationTest.process(shadowApi ->
                              {
                                 Declared arrayList = shadowApi.getClass("java.util.ArrayList");
                                 assertEquals(new HashSet<>(Collections.singletonList(Modifier.PUBLIC)), arrayList.getModifiers());

                                 Field serialVersionUID = arrayList.getFields().stream()
                                                                   .filter(field -> field.getSimpleName().equals("serialVersionUID"))
                                                                   .findAny()
                                                                   .orElseThrow(IllegalStateException::new);
                                 assertEquals(new HashSet<>(Arrays.asList(Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)),
                                              serialVersionUID.getModifiers());
                              })
                     .compile();
   }

   @Test
   void getJavaDocTest()
   {
      CompilationTest.process(shadowApi ->
                              {
                                 assertNull(shadowApi.getInterface("java.util.Collection").getJavaDoc());

                                 Class aClass = shadowApi.getClass("JavaDocExample");
                                 assertEquals(" Class level doc\n", aClass.getJavaDoc());
                                 assertEquals(" Method level doc\n", aClass.getMethods("toString").get(0).getJavaDoc());
                                 assertEquals(" Constructor level doc\n", aClass.getConstructors().get(0).getJavaDoc());
                                 assertNull(shadowApi.convert(aClass.getGenerics().get(0)).toOptionalGeneric().get().getJavaDoc());
                                 assertNull(aClass.getConstructors().get(0).getParameters().get(0).getJavaDoc());
                              })
                     .withCodeToCompile("JavaDocExample.java",
                                        "/**\n" +
                                        "                            * Class level doc\n" +
                                        "                            */\n" +
                                        "                           public class JavaDocExample</** a */T>\n" +
                                        "                           {\n" +
                                        "                              /**\n" +
                                        "                               * Field level doc\n" +
                                        "                               */\n" +
                                        "                              private Long id;\n" +
                                        "\n" +
                                        "                              /**\n" +
                                        "                               * Constructor level doc\n" +
                                        "                               */\n" +
                                        "                              public JavaDocExample(/** */ Long id)\n" +
                                        "                              {\n" +
                                        "                              }\n" +
                                        "\n" +
                                        "                              /**\n" +
                                        "                               * Method level doc\n" +
                                        "                               */\n" +
                                        "                              @Override\n" +
                                        "                              public String toString()\n" +
                                        "                              {\n" +
                                        "                                 return \"JavaDocExample{}\";\n" +
                                        "                              }\n" +
                                        "                           }")
                     .compile();
   }
}
