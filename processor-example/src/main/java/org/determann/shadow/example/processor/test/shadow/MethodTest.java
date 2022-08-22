package org.determann.shadow.example.processor.test.shadow;

import org.determann.shadow.api.shadow.Class;
import org.determann.shadow.api.shadow.Method;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.determann.shadow.example.processor.test.TestProcessor.SHADOW_API;
import static org.junit.jupiter.api.Assertions.*;

public class MethodTest extends ExecutableTest<Method>
{
   protected MethodTest()
   {
      super(() -> SHADOW_API.getClass("java.lang.String").getMethods("toString").get(0));
   }

   @Test
   void testisSubSignatureOf()
   {
      List<Method> methods = SHADOW_API.getClass("org.determann.shadow.example.processed.test.method.SubSignature").getMethods();
      Method first = methods.get(0);
      Method second = methods.get(1);
      Method third = methods.get(2);
      Method four = methods.get(3);
      Method five = methods.get(4);

      assertTrue(first.sameParameterTypes(second));
      assertTrue(second.sameParameterTypes(first));
      assertFalse(third.sameParameterTypes(four));
      assertFalse(four.sameParameterTypes(third));
      assertTrue(four.sameParameterTypes(five));
   }

   @Test
   void testOverrides()
   {
      assertTrue(SHADOW_API.getClass("org.determann.shadow.example.processed.test.method.MethodExample")
                           .getMethods("toString").get(0)
                           .overrides(SHADOW_API.getClass("java.lang.Object").getMethods("toString").get(0)));

      assertFalse(SHADOW_API.getClass("org.determann.shadow.example.processed.test.method.MethodExample")
                            .getMethods("toString").get(0)
                            .overrides(SHADOW_API.getClass("java.lang.Object").getMethods("clone").get(0)));
   }

   @Test
   @Override
   void testGetParameters()
   {
      assertEquals("[args]",
                   SHADOW_API.getClass("org.determann.shadow.example.processed.test.method.MethodExample")
                             .getMethods("varArgsMethod").get(0).getParameters().toString());
   }

   @Test
   @Override
   void testGetParameterTypes()
   {
      assertEquals(List.of(SHADOW_API.convert(SHADOW_API.getClass("java.lang.String")).asArray()),
                   SHADOW_API.getClass("org.determann.shadow.example.processed.test.method.MethodExample")
                             .getMethods("varArgsMethod").get(0).getParameterTypes());
   }

   @Test
   @Override
   void testGetReturnType()
   {
      assertEquals(SHADOW_API.getClass("java.lang.String"),
                   SHADOW_API.getClass("java.lang.String")
                             .getMethods("toString").get(0)
                             .getReturnType());

      assertNotEquals(SHADOW_API.getClass("java.lang.Long"),
                      SHADOW_API.getClass("java.lang.String")
                                .getMethods("toString").get(0)
                                .getReturnType());
   }

   @Test
   @Override
   void testGetThrows()
   {
      assertEquals(List.of(),
                   SHADOW_API.getClass("java.lang.Object")
                             .getMethods("toString").get(0)
                             .getThrows());

      assertEquals(List.of(SHADOW_API.getClass("java.lang.InterruptedException")),
                   SHADOW_API.getClass("java.lang.Object")
                             .getMethods("wait").get(0)
                             .getThrows());
   }

   @Test
   @Override
   void testIsVarArgs()
   {
      assertFalse(SHADOW_API.getClass("java.lang.Object")
                            .getMethods("toString").get(0)
                            .isVarArgs());
      assertTrue(SHADOW_API.getClass("org.determann.shadow.example.processed.test.method.MethodExample")
                           .getMethods("varArgsMethod").get(0)
                           .isVarArgs());
   }

   @Test
   @Override
   void testGetSurrounding()
   {
      Class aClass = SHADOW_API.getClass("org.determann.shadow.example.processed.test.method.MethodExample");
      assertEquals(aClass, aClass.getMethods("toString").get(0)
                                 .getSurrounding());
   }

   @Test
   @Override
   void testGetPackage()
   {
      assertEquals(SHADOW_API.getPackages("org.determann.shadow.example.processed.test.method").get(0),
                   SHADOW_API.getClass("org.determann.shadow.example.processed.test.method.MethodExample").getMethods("toString").get(0)
                             .getPackage());
   }

   @Test
   @Override
   void testGetReceiverType()
   {
      Class aClass = SHADOW_API.getClass("org.determann.shadow.example.processed.test.method.MethodExample");

      assertTrue(aClass.getMethods("toString").get(0).getReceiverType().isEmpty());
      assertEquals(aClass, aClass.getMethods("receiver").get(0).getReceiverType().orElseThrow());
   }
}
