package io.determann.shadow.consistency.renderer;

import io.determann.shadow.api.reflection.ReflectionAdapter;
import io.determann.shadow.consistency.test.ConsistencyTest;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.renderer.Renderer.render;
import static io.determann.shadow.api.renderer.RenderingContext.DEFAULT;
import static io.determann.shadow.api.shadow.Operations.CLASS_GET_GENERICS;
import static io.determann.shadow.api.shadow.Provider.requestOrThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

class GenericRendererTest
{
   @Test
   void declaration()
   {
      ConsistencyTest.compileTime(context -> context.getClassOrThrow("Annotated"))
                     .runtime(stringClassFunction -> ReflectionAdapter.generalize(stringClassFunction.apply("Annotated")))
                     .withCode("MyAnnotation.java",
                               "@java.lang.annotation.Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME) @java.lang.annotation.Target(java.lang.annotation.ElementType.TYPE_USE) @interface MyAnnotation {} ")
                     .withCode("Annotated.java", "class Annotated<@MyAnnotation T> {} ")
                     .test(aClass -> assertEquals("@MyAnnotation T",
                                                  render(DEFAULT, requestOrThrow(aClass, CLASS_GET_GENERICS).get(0)).declaration()));
   }

   @Test
   void type()
   {
      ConsistencyTest.compileTime(context -> context.getClassOrThrow("Annotated"))
                     .runtime(stringClassFunction -> ReflectionAdapter.generalize(stringClassFunction.apply("Annotated")))
                     .withCode("Annotated.java", "class Annotated<T> {} ")
                     .test(aClass -> assertEquals("T",
                                                  render(DEFAULT, requestOrThrow(aClass, CLASS_GET_GENERICS).get(0)).type()));

   }
}