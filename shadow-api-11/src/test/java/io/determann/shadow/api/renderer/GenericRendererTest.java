package io.determann.shadow.api.renderer;

import io.determann.shadow.api.test.ProcessorTest;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.ShadowApi.convert;
import static io.determann.shadow.api.ShadowApi.render;
import static org.junit.jupiter.api.Assertions.assertEquals;

class GenericRendererTest
{
   @Test
   void declaration()
   {
      ProcessorTest.process(shadowApi ->
                                  assertEquals("@MyAnnotation T",
                                               render(convert(shadowApi.getClassOrThrow("Annotated")
                                                                       .getGenerics()
                                                                       .get(0)).toGenericOrThrow())
                                                     .declaration()))
                   .withCodeToCompile("MyAnnotation.java", "@java.lang.annotation.Target(java.lang.annotation.ElementType.TYPE_USE) @interface MyAnnotation {} ")
                   .withCodeToCompile("Annotated.java", "class Annotated<@MyAnnotation T> {} ")
                   .compile();
   }

   @Test
   void type()
   {
      ProcessorTest.process(shadowApi ->
                                  assertEquals("T",
                                               render(convert(shadowApi.getClassOrThrow("Annotated")
                                                                       .getGenerics()
                                                                       .get(0)).toGenericOrThrow())
                                                     .type()))
                   .withCodeToCompile("Annotated.java", "class Annotated<T> {} ")
                   .compile();
   }
}