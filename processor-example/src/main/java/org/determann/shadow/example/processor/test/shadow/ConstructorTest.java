package org.determann.shadow.example.processor.test.shadow;

import org.determann.shadow.api.shadow.Class;
import org.determann.shadow.api.shadow.Constructor;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.determann.shadow.example.processor.test.TestProcessor.SHADOW_API;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ConstructorTest extends ExecutableTest<Constructor>
{
   protected ConstructorTest()
   {
      super(() -> SHADOW_API.getClass("org.determann.shadow.example.processed.test.constructor.DefaultConstructorExample")
                            .getConstructors()
                            .get(0));
   }

   @Test
   void testGetParameters()
   {
      assertEquals(0,
                   SHADOW_API.getClass("org.determann.shadow.example.processed.test.constructor.DefaultConstructorExample")
                             .getConstructors()
                             .get(0)
                             .getParameters()
                             .size());

      List<Constructor> constructors = SHADOW_API.getClass("org.determann.shadow.example.processed.test.constructor.ConstructorExample")
                                                 .getConstructors();
      assertEquals(3, constructors.size());
      assertEquals(SHADOW_API.getClass("java.lang.Long"),
                   constructors.get(0)
                               .getParameters()
                               .get(0)
                               .getType());
   }

   @Test
   @Override
   void testGetReturnType()
   {
      assertEquals(SHADOW_API.getConstants().getVoid(),
                   SHADOW_API.getClass("org.determann.shadow.example.processed.test.constructor.ConstructorExample")
                             .getConstructors()
                             .get(0)
                             .getReturnType());
   }

   @Test
   @Override
   void testGetParameterTypes()
   {
      assertEquals(List.of(SHADOW_API.getClass("java.lang.Long")),
                   SHADOW_API.getClass("org.determann.shadow.example.processed.test.constructor.ConstructorExample")
                             .getConstructors()
                             .get(0)
                             .getParameterTypes());
   }

   @Test
   @Override
   void testGetThrows()
   {
      assertEquals(List.of(),
                   SHADOW_API.getClass("org.determann.shadow.example.processed.test.constructor.ConstructorExample")
                             .getConstructors()
                             .get(0)
                             .getThrows());

      assertEquals(List.of(SHADOW_API.getClass("java.io.IOException")),
                   SHADOW_API.getClass("org.determann.shadow.example.processed.test.constructor.ConstructorExample")
                             .getConstructors()
                             .get(1)
                             .getThrows());
   }

   @Test
   @Override
   void testIsVarArgs()
   {
      assertTrue(SHADOW_API.getClass("org.determann.shadow.example.processed.test.constructor.ConstructorExample")
                           .getConstructors()
                           .get(2)
                           .isVarArgs());
   }

   @Test
   @Override
   void testGetSurrounding()
   {
      Class aClass = SHADOW_API.getClass("org.determann.shadow.example.processed.test.constructor.ConstructorExample");
      assertEquals(aClass, aClass.getConstructors().get(0).getSurrounding());
   }

   @Test
   @Override
   void testGetPackage()
   {
      assertEquals(SHADOW_API.getPackages("org.determann.shadow.example.processed.test.constructor").get(0),
                   SHADOW_API.getClass("org.determann.shadow.example.processed.test.constructor.ConstructorExample")
                             .getPackage());
   }

   @Test
   void testGetReceiverType()
   {
      assertEquals(Optional.empty(),
                   SHADOW_API.getClass("org.determann.shadow.example.processed.test.constructor.DefaultConstructorExample")
                             .getConstructors()
                             .get(0)
                             .getReceiverType());

      assertEquals(SHADOW_API.getClass("org.determann.shadow.example.processed.test.constructor.ReceiverExample"),
                   SHADOW_API.getClass("org.determann.shadow.example.processed.test.constructor.ReceiverExample.Inner")
                             .getConstructors()
                             .get(0)
                             .getReceiverType()
                             .orElseThrow());
   }
}
