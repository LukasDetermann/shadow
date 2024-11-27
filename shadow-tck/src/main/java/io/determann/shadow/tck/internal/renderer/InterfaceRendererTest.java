package io.determann.shadow.tck.internal.renderer;

import io.determann.shadow.api.shadow.type.C_Interface;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.Operations.GET_INTERFACE;
import static io.determann.shadow.api.Provider.requestOrThrow;
import static io.determann.shadow.api.renderer.Renderer.render;
import static io.determann.shadow.api.renderer.RenderingContext.DEFAULT;
import static io.determann.shadow.tck.internal.TckTest.test;
import static io.determann.shadow.tck.internal.TckTest.withSource;
import static org.junit.jupiter.api.Assertions.assertEquals;

class InterfaceRendererTest
{
   @Test
   void emptyDeclaration()
   {
      test(implementation ->
           {
              C_Interface cInterface = requestOrThrow(implementation, GET_INTERFACE, "java.util.function.Function");
              assertEquals("@FunctionalInterface\npublic interface Function<T, R> {}\n", render(DEFAULT, cInterface).declaration());
           });
   }

   @Test
   void declaration()
   {
      test(implementation ->
           {
              C_Interface cInterface = requestOrThrow(implementation, GET_INTERFACE, "java.util.function.Function");
              assertEquals("""
                                 @FunctionalInterface
                                 public interface Function<T, R> {
                                 test
                                 }
                                 """, render(DEFAULT, cInterface).declaration("test"));
           });
   }

   @Test
   void declarationWithDependentGenerics()
   {
      //@formatter:off
      String expected = "public interface InterpolateGenericsExample<A extends Comparable<B>, B extends Comparable<A>> extends java.io.Serializable {}\n";
      //@formatter:on
      withSource("InterpolateGenericsExample.java", """
            public interface InterpolateGenericsExample<A extends Comparable<B>, B extends Comparable<A>> extends java.io.Serializable {
               interface IndependentGeneric<C> {}
               interface DependentGeneric<D extends E, E> {}
            }
            """)
            .test(implementation ->
                  {
                     C_Interface cInterface = requestOrThrow(implementation, GET_INTERFACE, "InterpolateGenericsExample");
                     assertEquals(expected, render(DEFAULT, cInterface).declaration());
                  });
   }

   @Test
   void type()
   {
      String expected = "InterpolateGenericsExample<A extends Comparable<B>, B extends Comparable<A>>";

      withSource("InterpolateGenericsExample.java", """
            public interface InterpolateGenericsExample<A extends Comparable<B>, B extends Comparable<A>> {
               interface IndependentGeneric<C> {}
               interface DependentGeneric<D extends E, E> {}
            }
            """)
            .test(implementation ->
                  {
                     C_Interface cInterface = requestOrThrow(implementation, GET_INTERFACE, "InterpolateGenericsExample");
                     assertEquals(expected, render(DEFAULT, cInterface).type());
                  });
   }
}