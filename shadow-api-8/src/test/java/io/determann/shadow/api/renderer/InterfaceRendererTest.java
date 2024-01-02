package io.determann.shadow.api.renderer;

import io.determann.shadow.api.reflection.ReflectionAdapter;
import io.determann.shadow.consistency.ConsistencyTest;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.renderer.Renderer.render;
import static io.determann.shadow.api.renderer.RenderingContext.DEFAULT;
import static org.junit.jupiter.api.Assertions.assertEquals;

class InterfaceRendererTest
{
   @Test
   void declaration()
   {
      ConsistencyTest.compileTime(context -> context.getInterfaceOrThrow("InterpolateGenericsExample"))
                     .runtime(stringClassFunction -> ReflectionAdapter.getShadow(stringClassFunction.apply("InterpolateGenericsExample")))
                     .withCode("InterpolateGenericsExample.java",
                               "public interface InterpolateGenericsExample<A extends Comparable<B>, B extends Comparable<A>> extends java.io.Serializable {\n" +
                               "   interface IndependentGeneric<C> {}\n" +
                               "   interface DependentGeneric<D extends E, E> {}\n" +
                               "}\n")
                     .test(aClass -> assertEquals(
                           "public interface InterpolateGenericsExample<A extends Comparable<B>, B extends Comparable<A>> extends java.io.Serializable {}\n",
                           render(DEFAULT, aClass).declaration()));

      ConsistencyTest.compileTime(context -> context.getInterfaceOrThrow("java.util.function.Function"))
                     .runtime(stringClassFunction -> ReflectionAdapter.getShadow(stringClassFunction.apply("java.util.function.Function")))
                     .test(aClass -> assertEquals("@FunctionalInterface\n" +
                                                  "public interface Function<T, R> {\n" +
                                                  "test\n" +
                                                  "}\n",
                                                  render(DEFAULT, aClass).declaration("test")));
   }

   @Test
   void type()
   {
      ConsistencyTest.compileTime(context -> context.getInterfaceOrThrow("InterpolateGenericsExample"))
                     .runtime(stringClassFunction -> ReflectionAdapter.getShadow(stringClassFunction.apply("InterpolateGenericsExample")))
                     .withCode("InterpolateGenericsExample.java",
                               "public interface InterpolateGenericsExample<A extends Comparable<B>, B extends Comparable<A>> {\n" +
                               "   interface IndependentGeneric<C> {}\n" +
                               "   interface DependentGeneric<D extends E, E> {}\n" +
                               "}\n")
                     .test(aClass -> assertEquals("InterpolateGenericsExample<A extends Comparable<B>, B extends Comparable<A>>",
                                                  render(DEFAULT, aClass).type()),
                           aClass -> assertEquals("InterpolateGenericsExample",
                                                  render(DEFAULT, aClass).type()));
   }
}