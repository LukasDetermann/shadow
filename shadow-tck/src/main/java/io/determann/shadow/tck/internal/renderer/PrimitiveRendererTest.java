package io.determann.shadow.tck.internal.renderer;

import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.Operations.*;
import static io.determann.shadow.api.Provider.requestOrThrow;
import static io.determann.shadow.api.renderer.Renderer.render;
import static io.determann.shadow.api.renderer.RenderingContext.DEFAULT;
import static io.determann.shadow.tck.internal.TckTest.test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PrimitiveRendererTest
{
   @Test
   void booleanType()
   {
      test(implementation -> assertEquals("boolean", render(DEFAULT, requestOrThrow(implementation, GET_BOOLEAN)).type()));
   }

   @Test
   void byteType()
   {
      test(implementation -> assertEquals("byte", render(DEFAULT, requestOrThrow(implementation, GET_BYTE)).type()));
   }

   @Test
   void shortType()
   {
      test(implementation -> assertEquals("short", render(DEFAULT, requestOrThrow(implementation, GET_SHORT)).type()));
   }

   @Test
   void intType()
   {
      test(implementation -> assertEquals("int", render(DEFAULT, requestOrThrow(implementation, GET_INT)).type()));
   }

   @Test
   void longType()
   {
      test(implementation -> assertEquals("long", render(DEFAULT, requestOrThrow(implementation, GET_LONG)).type()));
   }

   @Test
   void charType()
   {
      test(implementation -> assertEquals("char", render(DEFAULT, requestOrThrow(implementation, GET_CHAR)).type()));
   }

   @Test
   void floatType()
   {
      test(implementation -> assertEquals("float", render(DEFAULT, requestOrThrow(implementation, GET_FLOAT)).type()));
   }

   @Test
   void doubleType()
   {
      test(implementation -> assertEquals("double", render(DEFAULT, requestOrThrow(implementation, GET_DOUBLE)).type()));
   }
}