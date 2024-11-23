package io.determann.shadow.tck.internal.renderer;

import io.determann.shadow.api.shadow.type.C_Interface;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.renderer.Renderer.render;
import static io.determann.shadow.api.renderer.RenderingContext.DEFAULT;
import static io.determann.shadow.tck.internal.RenderingTestBuilder.renderingTest;

class InterfaceRendererTest
{
   @Test
   void declaration()
   {
      renderingTest(C_Interface.class)
            .withSource("InterpolateGenericsExample.java", """
                  public interface InterpolateGenericsExample<A extends Comparable<B>, B extends Comparable<A>> extends java.io.Serializable {
                     interface IndependentGeneric<C> {}
                     interface DependentGeneric<D extends E, E> {}
                  }
                  """)
            .withToRender("InterpolateGenericsExample")
            .withRender(cInterface -> render(DEFAULT, cInterface).declaration())
            .withExpected(
                  "public interface InterpolateGenericsExample<A extends Comparable<B>, B extends Comparable<A>> extends java.io.Serializable {}\n")
            .test();

      renderingTest(C_Interface.class)
            .withToRender("java.util.function.Function")
            .withRender(cInterface -> render(DEFAULT, cInterface).declaration("test"))
            .withExpected("""
                                @FunctionalInterface
                                public interface Function<T, R> {
                                test
                                }
                                """)
            .test();
   }

   @Test
   void type()
   {
      renderingTest(C_Interface.class)
            .withSource("InterpolateGenericsExample.java", """
                  public interface InterpolateGenericsExample<A extends Comparable<B>, B extends Comparable<A>> {
                     interface IndependentGeneric<C> {}
                     interface DependentGeneric<D extends E, E> {}
                  }
                  """)
            .withToRender("InterpolateGenericsExample")
            .withRender(cInterface -> render(DEFAULT, cInterface).type())
            .withExpected("InterpolateGenericsExample<A extends Comparable<B>, B extends Comparable<A>>")
            .test();
   }
}