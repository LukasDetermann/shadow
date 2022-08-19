package org.determann.shadow.example.processor.test;

import org.determann.shadow.api.modifier.Modifier;
import org.determann.shadow.api.shadow.Class;
import org.determann.shadow.api.shadow.Declared;
import org.determann.shadow.api.shadow.Field;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.determann.shadow.example.processor.test.TestProcessor.SHADOW_API;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ElementBackedTest
{
   @Test
   void getModifiersTest()
   {
      Declared arrayList = SHADOW_API.getClass("java.util.ArrayList");
      assertEquals(Set.of(Modifier.PUBLIC), arrayList.getModifiers());

      Field serialVersionUID = arrayList.getFields().stream()
                                        .filter(field -> field.getSimpleName().equals("serialVersionUID"))
                                        .findAny()
                                        .orElseThrow();
      assertEquals(Set.of(Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL), serialVersionUID.getModifiers());
   }

   @Test
   void getModuleTest()
   {
      assertEquals("java.base", SHADOW_API.getInterface("java.util.Collection").getModule().getQualifiedName());
   }

   @Test
   void getJavaDocTest()
   {
      assertNull(SHADOW_API.getInterface("java.util.Collection").getJavaDoc());

      Class aClass = SHADOW_API.getClass("org.determann.shadow.example.processed.test.elementbacked.JavaDocExample");
      assertEquals(" Class level doc\n", aClass.getJavaDoc());
      assertEquals(" Method level doc\n", aClass.getMethods("toString").get(0).getJavaDoc());
      assertEquals(" Constructor level doc\n", aClass.getConstructors().get(0).getJavaDoc());
      assertNull(SHADOW_API.convert(aClass.getGenerics().get(0)).toGeneric().get().getJavaDoc());
      assertNull(aClass.getConstructors().get(0).getParameters().get(0).getJavaDoc());
   }
}
