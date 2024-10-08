package io.determann.shadow.consistency.renderer;

import io.determann.shadow.api.reflection.R_Adapter;
import io.determann.shadow.api.shadow.type.C_Interface;
import io.determann.shadow.consistency.test.ConsistencyTest;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.renderer.Renderer.render;
import static io.determann.shadow.api.renderer.RenderingContext.DEFAULT;
import static org.junit.jupiter.api.Assertions.assertEquals;

class InterfaceRendererTest
{
   @Test
   void declaration()
   {
      ConsistencyTest.<C_Interface>compileTime(context -> context.getInterfaceOrThrow("InterpolateGenericsExample"))
                     .runtime(stringClassFunction -> R_Adapter.generalize(stringClassFunction.apply("InterpolateGenericsExample")))
                     .withCode("InterpolateGenericsExample.java", """
                           public interface InterpolateGenericsExample<A extends Comparable<B>, B extends Comparable<A>> extends java.io.Serializable {
                              interface IndependentGeneric<C> {}
                              interface DependentGeneric<D extends E, E> {}
                           }
                           """)
                     .test(aClass -> assertEquals(
                           "public interface InterpolateGenericsExample<A extends Comparable<B>, B extends Comparable<A>> extends java.io.Serializable {}\n",
                           render(DEFAULT, aClass).declaration()));

      ConsistencyTest.<C_Interface>compileTime(context -> context.getInterfaceOrThrow("java.util.function.Function"))
                     .runtime(stringClassFunction -> R_Adapter.generalize(stringClassFunction.apply("java.util.function.Function")))
                     .test(aClass -> assertEquals("""
                                                  @FunctionalInterface
                                                  public interface Function<T, R> {
                                                  test
                                                  }
                                                  """,
                                                  render(DEFAULT, aClass).declaration("test")));
   }

   @Test
   void type()
   {
      ConsistencyTest.<C_Interface>compileTime(context -> context.getInterfaceOrThrow("InterpolateGenericsExample"))
                     .runtime(stringClassFunction -> R_Adapter.generalize(stringClassFunction.apply("InterpolateGenericsExample")))
                     .withCode("InterpolateGenericsExample.java", """
                         public interface InterpolateGenericsExample<A extends Comparable<B>, B extends Comparable<A>> {
                            interface IndependentGeneric<C> {}
                            interface DependentGeneric<D extends E, E> {}
                         }
                         """)
                     .test(aClass -> assertEquals("InterpolateGenericsExample<A extends Comparable<B>, B extends Comparable<A>>",
                                                  render(DEFAULT, aClass).type()),
                           aClass -> assertEquals("InterpolateGenericsExample",
                                                  render(DEFAULT, aClass).type()));
   }
}