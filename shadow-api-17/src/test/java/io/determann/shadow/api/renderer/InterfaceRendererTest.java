package io.determann.shadow.api.renderer;

import io.determann.shadow.api.test.ProcessorTest;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.ShadowApi.render;
import static org.junit.jupiter.api.Assertions.assertEquals;

class InterfaceRendererTest
{
   @Test
   void declaration()
   {
      ProcessorTest.process(shadowApi ->
                            {
                               assertEquals(
                                     "public interface InterpolateGenericsExample<A extends Comparable<B>, B extends Comparable<A>> extends java.io.Serializable {}\n",
                                     render(shadowApi.getInterfaceOrThrow("InterpolateGenericsExample")).declaration());

                               assertEquals("""
                                                  @FunctionalInterface
                                                  public interface Function<T, R> {
                                                  test
                                                  }
                                                  """,
                                            render(shadowApi.getInterfaceOrThrow("java.util.function.Function")).declaration("test"));
                            })
                   .withCodeToCompile("InterpolateGenericsExample.java", """
                         public interface InterpolateGenericsExample<A extends Comparable<B>, B extends Comparable<A>> extends java.io.Serializable {
                            interface IndependentGeneric<C> {}
                            interface DependentGeneric<D extends E, E> {}
                         }
                         """)
                   .compile();
   }

   @Test
   void type()
   {
      ProcessorTest.process(shadowApi ->
                                  assertEquals("InterpolateGenericsExample<A extends Comparable<B>, B extends Comparable<A>>",
                                               render(shadowApi.getInterfaceOrThrow("InterpolateGenericsExample")).type()))
                   .withCodeToCompile("InterpolateGenericsExample.java", """
                         public interface InterpolateGenericsExample<A extends Comparable<B>, B extends Comparable<A>> {
                            interface IndependentGeneric<C> {}
                            interface DependentGeneric<D extends E, E> {}
                         }
                         """)
                   .compile();
   }
}