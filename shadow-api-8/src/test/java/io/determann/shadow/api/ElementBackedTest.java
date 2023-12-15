package io.determann.shadow.api;

import io.determann.shadow.api.modifier.Modifier;
import io.determann.shadow.api.shadow.Class;
import io.determann.shadow.api.shadow.Declared;
import io.determann.shadow.api.shadow.Field;
import io.determann.shadow.api.test.ProcessorTest;
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
      ProcessorTest.process(shadowApi ->
                            {
                               Declared arrayList = shadowApi.getClassOrThrow("java.util.ArrayList");
                               assertEquals(new HashSet<>(Collections.singletonList(Modifier.PUBLIC)), arrayList.getModifiers());

                               Field serialVersionUID = arrayList.getFields().stream()
                                                                 .filter(field -> "serialVersionUID".equals(field.getName()))
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
      ProcessorTest.process(shadowApi ->
                            {
                               assertNull(shadowApi.getInterfaceOrThrow("java.util.Collection").getJavaDoc());

                               Class aClass = shadowApi.getClassOrThrow("JavaDocExample");
                               assertEquals(" Class level doc\n", aClass.getJavaDoc());
                               assertEquals(" Method level doc\n", aClass.getMethods("toString").get(0).getJavaDoc());
                               assertEquals(" Constructor level doc\n", aClass.getConstructors().get(0).getJavaDoc());
                            })
                   .withCodeToCompile("JavaDocExample.java",
                                      "/**\n" +
                                      "                            * Class level doc\n" +
                                      "                            */\n" +
                                      "                           public class JavaDocExample<T>\n" +
                                      "                           {\n" +
                                      "                              /**\n" +
                                      "                               * Field level doc\n" +
                                      "                               */\n" +
                                      "                              private Long id;\n" +
                                      "\n" +
                                      "                              /**\n" +
                                      "                               * Constructor level doc\n" +
                                      "                               */\n" +
                                      "                              public JavaDocExample() {}\n" +
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
