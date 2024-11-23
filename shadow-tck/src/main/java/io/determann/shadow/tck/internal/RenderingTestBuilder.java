package io.determann.shadow.tck.internal;

import io.determann.shadow.tck.Source;

import java.util.*;
import java.util.function.Function;

import static io.determann.shadow.api.Operations.GET_DECLARED;
import static io.determann.shadow.api.Provider.requestOrThrow;
import static io.determann.shadow.tck.Tck.TCK;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RenderingTestBuilder<T>
{
   private Collection<Source> sources = Collections.emptyList();
   private String toRender;
   private final Class<T> toRenderType;
   private Function<T, String> renderer;
   private String expected;

   public static <T> RenderingTestBuilder<T> renderingTest(Class<T> toRenderType)
   {
      return new RenderingTestBuilder<>(toRenderType);
   }

   public RenderingTestBuilder(Class<T> toRenderType)
   {
      this.toRenderType = toRenderType;
   }

   private RenderingTestBuilder(Collection<Source> sources,
                                String toRender,
                                Class<T> toRenderType,
                                Function<T, String> renderer,
                                String expected)
   {
      this.sources = sources;
      this.toRender = toRender;
      this.toRenderType = toRenderType;
      this.renderer = renderer;
      this.expected = expected;
   }

   public RenderingTestBuilder<T> withSources(Collection<Source> sources)
   {
      return new RenderingTestBuilder<>(sources, toRender, toRenderType, renderer, expected);
   }

   public RenderingTestBuilder<T> withSource(String name, String content)
   {
      List<Source> copy = new ArrayList<>(sources);
      copy.add(new Source(name, content));
      return new RenderingTestBuilder<>(copy, toRender, toRenderType, renderer, expected);
   }

   public RenderingTestBuilder<T> withToRender(String toRender)
   {
      return new RenderingTestBuilder<>(sources, toRender, toRenderType, renderer, expected);
   }

   public RenderingTestBuilder<T> withRender(Function<T, String> renderer)
   {
      return new RenderingTestBuilder<>(sources, toRender, toRenderType, renderer, expected);
   }

   public RenderingTestBuilder<T> withExpected(String expected)
   {
      return new RenderingTestBuilder<>(sources, toRender, toRenderType, renderer, expected);
   }

   public void test()
   {
      Objects.requireNonNull(sources);
      Objects.requireNonNull(toRender);
      Objects.requireNonNull(toRenderType);
      Objects.requireNonNull(renderer);
      Objects.requireNonNull(expected);

      TCK.test(sources, implementation ->
      {
         String actual = renderer.apply(toRenderType.cast(requestOrThrow(implementation,
                                                                         GET_DECLARED,
                                                                         toRender)));
         assertEquals(expected, actual);
      });
   }
}
