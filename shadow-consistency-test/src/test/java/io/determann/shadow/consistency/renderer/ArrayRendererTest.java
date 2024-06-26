package io.determann.shadow.consistency.renderer;

import io.determann.shadow.api.reflection.ReflectionAdapter;
import io.determann.shadow.api.renderer.Renderer;
import io.determann.shadow.api.renderer.RenderingContext;
import io.determann.shadow.api.shadow.type.Declared;
import io.determann.shadow.consistency.test.ConsistencyTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.reflection.Reflection.asArray;
import static io.determann.shadow.api.renderer.Renderer.render;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ArrayRendererTest
{
   @Test
   void type()
   {
      ConsistencyTest.compileTime(context -> context.asArray(context.getClassOrThrow("java.lang.String")))
                     .runtime(stringClassFunction -> asArray((Declared) ReflectionAdapter.generalize(stringClassFunction.apply("java.lang.String"))))
                     .test(aClass -> Assertions.assertEquals("String[]", Renderer.render(RenderingContext.DEFAULT, aClass).type()));

      ConsistencyTest.compileTime(context -> context.asArray(context.asArray(context.getClassOrThrow("java.lang.String"))))
                     .runtime(stringClassFunction -> asArray(asArray((Declared) ReflectionAdapter.generalize(stringClassFunction.apply("java.lang.String")))))
                     .test(aClass -> assertEquals("String[][]", render(RenderingContext.DEFAULT, aClass).type()));
   }

   @Test
   void initialisation()
   {
      ConsistencyTest.compileTime(context -> context.asArray(context.getClassOrThrow("java.lang.String")))
                     .runtime(stringClassFunction -> asArray((Declared) ReflectionAdapter.generalize(stringClassFunction.apply("java.lang.String"))))
                     .test(string ->
                           {
                              assertEquals("new String[3][1]", render(RenderingContext.DEFAULT, string).initialisation(3, 1));
                           });
   }
}