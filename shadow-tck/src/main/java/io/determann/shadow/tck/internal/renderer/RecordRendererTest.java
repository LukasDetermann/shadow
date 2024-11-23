package io.determann.shadow.tck.internal.renderer;

import io.determann.shadow.api.shadow.type.C_Record;
import io.determann.shadow.tck.internal.RenderingTestBuilder;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.renderer.Renderer.render;
import static io.determann.shadow.api.renderer.RenderingContext.DEFAULT;
import static io.determann.shadow.tck.internal.RenderingTestBuilder.renderingTest;

class RecordRendererTest
{
   @Test
   void declaration()
   {
      RenderingTestBuilder<C_Record> recordTest = renderingTest(C_Record.class)
            .withSource("InterpolateGenericsExample.java",
                        "public record InterpolateGenericsExample<A extends Comparable<B>, B extends Comparable<A>> () {}")
            .withToRender("InterpolateGenericsExample");

      recordTest.withRender(cRecord -> render(DEFAULT, cRecord).declaration())
                .withExpected("public record InterpolateGenericsExample<A extends Comparable<B>, B extends Comparable<A>>() {}\n")
                .test();

      recordTest.withRender(cRecord -> render(DEFAULT, cRecord).declaration("test"))
                .withExpected("public record InterpolateGenericsExample<A extends Comparable<B>, B extends Comparable<A>>() {\ntest\n}\n")
                .test();

      renderingTest(C_Record.class)
            .withSource("Parameters.java", "@MyAnnotation\n record Parameters(Long id, String name) implements java.io.Serializable {}")
            .withSource("MyAnnotation.java",
                        "@java.lang.annotation.Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME) @interface MyAnnotation {} ")
            .withToRender("Parameters")
            .withRender(cRecord -> render(DEFAULT, cRecord).declaration())
            .withExpected("@MyAnnotation\nrecord Parameters(Long id, String name) implements java.io.Serializable {}\n")
            .test();
   }

   @Test
   void type()
   {
      renderingTest(C_Record.class)
            .withSource("InterpolateGenericsExample.java",
                        "public record InterpolateGenericsExample<A extends Comparable<B>, B extends Comparable<A>> () {}")
            .withToRender("InterpolateGenericsExample")
            .withRender(cRecord -> render(DEFAULT, cRecord).type())
            .withExpected("InterpolateGenericsExample<A extends Comparable<B>, B extends Comparable<A>>")
            .test();
   }
}