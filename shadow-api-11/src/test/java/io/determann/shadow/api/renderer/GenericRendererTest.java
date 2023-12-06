package io.determann.shadow.api.renderer;

import io.determann.shadow.api.test.ProcessorTest;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.converter.Converter.convert;
import static io.determann.shadow.api.renderer.Renderer.render;
import static io.determann.shadow.api.renderer.RenderingContext.DEFAULT;
import static org.junit.jupiter.api.Assertions.assertEquals;

class GenericRendererTest
{
   @Test
   void declaration()
   {
      ProcessorTest.process(shadowApi ->
                                  assertEquals("@MyAnnotation T",
                                               render(DEFAULT, convert(shadowApi.getClassOrThrow("Annotated")
                                                                       .getGenericTypes()
                                                                       .get(0)).toGenericOrThrow())
                                                     .declaration()))
                   .withCodeToCompile("MyAnnotation.java",
                                      "@java.lang.annotation.Target(java.lang.annotation.ElementType.TYPE_USE) @interface MyAnnotation {} ")
                   .withCodeToCompile("Annotated.java", "class Annotated<@MyAnnotation T> {} ")
                   .compile();
   }

   @Test
   void type()
   {
      ProcessorTest.process(shadowApi ->
                                  assertEquals("T",
                                               render(DEFAULT, convert(shadowApi.getClassOrThrow("Annotated")
                                                                       .getGenericTypes()
                                                                       .get(0)).toGenericOrThrow())
                                                     .type()))
                   .withCodeToCompile("Annotated.java", "class Annotated<T> {} ")
                   .compile();
   }
}