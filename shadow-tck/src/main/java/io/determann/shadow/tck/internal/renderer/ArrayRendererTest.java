package io.determann.shadow.tck.internal.renderer;

import io.determann.shadow.api.renderer.RenderingContext;
import io.determann.shadow.api.shadow.type.C_Array;
import io.determann.shadow.api.shadow.type.C_Declared;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.Operations.ARRAY_AS_ARRAY;
import static io.determann.shadow.api.Operations.DECLARED_AS_ARRAY;
import static io.determann.shadow.api.Provider.requestOrThrow;
import static io.determann.shadow.api.renderer.Renderer.render;
import static io.determann.shadow.tck.internal.RenderingTestBuilder.renderingTest;

class ArrayRendererTest
{
   @Test
   void type()
   {
      renderingTest(C_Declared.class)
            .withToRender("java.lang.String")
            .withRender(cDeclared ->
                        {
                           C_Array array = requestOrThrow(cDeclared, DECLARED_AS_ARRAY);
                           return render(RenderingContext.DEFAULT, array).type();
                        })
            .withExpected("String[]")
            .test();
   }

   @Test
   void nestedType()
   {
      renderingTest(C_Declared.class)
            .withToRender("java.lang.String")
            .withRender(cDeclared ->
                        {
                           C_Array array = requestOrThrow(cDeclared, DECLARED_AS_ARRAY);
                           C_Array array1 = requestOrThrow(array, ARRAY_AS_ARRAY);
                           return render(RenderingContext.DEFAULT, array1).type();
                        })
            .withExpected("String[][]")
            .test();
   }

   @Test
   void initialisation()
   {
      renderingTest(C_Declared.class)
            .withToRender("java.lang.String")
            .withRender(cDeclared ->
                        {
                           C_Array array = requestOrThrow(cDeclared, DECLARED_AS_ARRAY);
                           return render(RenderingContext.DEFAULT, array).initialisation(3, 1);
                        })
            .withExpected("new String[3][1]")
            .test();
   }
}