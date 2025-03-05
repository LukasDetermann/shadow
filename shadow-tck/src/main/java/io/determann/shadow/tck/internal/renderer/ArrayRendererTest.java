package io.determann.shadow.tck.internal.renderer;

import io.determann.shadow.api.shadow.type.C_Array;
import io.determann.shadow.api.shadow.type.C_Class;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.Operations.*;
import static io.determann.shadow.api.Provider.requestOrThrow;
import static io.determann.shadow.api.renderer.Renderer.render;
import static io.determann.shadow.api.renderer.RenderingContext.DEFAULT;
import static io.determann.shadow.tck.internal.TckTest.test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ArrayRendererTest
{
   @Test
   void type()
   {
      test(implementation ->
           {
              C_Class cClass = requestOrThrow(implementation, GET_CLASS, "java.lang.String");
              C_Array array = requestOrThrow(cClass, DECLARED_AS_ARRAY);
              String actual = render(array).type(DEFAULT);

              assertEquals("String[]", actual);
           });
   }

   @Test
   void nestedType()
   {
      test(implementation ->
           {
              C_Class cClass = requestOrThrow(implementation, GET_CLASS, "java.lang.String");
              C_Array array = requestOrThrow(cClass, DECLARED_AS_ARRAY);
              C_Array array1 = requestOrThrow(array, ARRAY_AS_ARRAY);
              String actual = render(array1).type(DEFAULT);

              assertEquals("String[][]", actual);
           });
   }

   @Test
   void initialisation()
   {
      test(implementation ->
           {
              C_Class cClass = requestOrThrow(implementation, GET_CLASS, "java.lang.String");
              C_Array array = requestOrThrow(cClass, DECLARED_AS_ARRAY);
              String actual = render(array).initialisation(DEFAULT,3, 1);

              assertEquals("new String[3][1]", actual);
           });
   }
}