package io.determann.shadow.tck.internal.renderer;

import io.determann.shadow.api.shadow.type.C_Record;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.Operations.GET_RECORD;
import static io.determann.shadow.api.Provider.requestOrThrow;
import static io.determann.shadow.api.renderer.Renderer.render;
import static io.determann.shadow.api.renderer.RenderingContext.DEFAULT;
import static io.determann.shadow.tck.internal.TckTest.withSource;
import static org.junit.jupiter.api.Assertions.assertEquals;

class RecordRendererTest
{
   @Test
   void emptyDeclaration()
   {
      String expected = "public record InterpolateGenericsExample<A extends Comparable<B>, B extends Comparable<A>>() {}\n";

      withSource("InterpolateGenericsExample.java",
                 "public record InterpolateGenericsExample<A extends Comparable<B>, B extends Comparable<A>> () {}")
            .test(implementation ->
                  {
                     C_Record record = requestOrThrow(implementation, GET_RECORD, "InterpolateGenericsExample");
                     assertEquals(expected, render(record).declaration(DEFAULT));
                  });
   }

   @Test
   void declaration()
   {
      String expected = "public record InterpolateGenericsExample<A extends Comparable<B>, B extends Comparable<A>>() {\ntest\n}\n";

      withSource("InterpolateGenericsExample.java",
                 "public record InterpolateGenericsExample<A extends Comparable<B>, B extends Comparable<A>> () {}")
            .test(implementation ->
                  {
                     C_Record record = requestOrThrow(implementation, GET_RECORD, "InterpolateGenericsExample");
                     assertEquals(expected, render(record).declaration(DEFAULT,"test"));
                  });
   }

   @Test
   void parameterDeclaration()
   {
      String expected = "@MyAnnotation\nrecord Parameters(Long id, String name) implements java.io.Serializable {}\n";

      withSource("Parameters.java", "@MyAnnotation\n record Parameters(Long id, String name) implements java.io.Serializable {}")
            .withSource("MyAnnotation.java",
                        "@java.lang.annotation.Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME) @interface MyAnnotation {} ")
            .test(implementation ->
                  {
                     C_Record record = requestOrThrow(implementation, GET_RECORD, "Parameters");
                     assertEquals(expected, render(record).declaration(DEFAULT));
                  });
   }

   @Test
   void type()
   {
      String expected = "InterpolateGenericsExample<A extends Comparable<B>, B extends Comparable<A>>";

      withSource("InterpolateGenericsExample.java",
                 "public record InterpolateGenericsExample<A extends Comparable<B>, B extends Comparable<A>> () {}")
            .test(implementation ->
                  {
                     C_Record record = requestOrThrow(implementation, GET_RECORD, "InterpolateGenericsExample");
                     assertEquals(expected, render(record).type(DEFAULT));
                  });
   }
}