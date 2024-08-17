package io.determann.shadow.consistency.renderer;

import io.determann.shadow.api.reflection.ReflectionAdapter;
import io.determann.shadow.api.shadow.type.Record;
import io.determann.shadow.consistency.test.ConsistencyTest;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.renderer.Renderer.render;
import static io.determann.shadow.api.renderer.RenderingContext.DEFAULT;
import static org.junit.jupiter.api.Assertions.assertEquals;

class RecordRendererTest
{
   @Test
   void declaration()
   {
      ConsistencyTest.<Record>compileTime(context -> context.getRecordOrThrow("InterpolateGenericsExample"))
                     .runtime(stringClassFunction -> ReflectionAdapter.generalize(stringClassFunction.apply("InterpolateGenericsExample")))
                     .withCode("InterpolateGenericsExample.java",
                               "public record InterpolateGenericsExample<A extends Comparable<B>, B extends Comparable<A>> () {}")
                     .test(aClass ->
                           {
                              assertEquals("public record InterpolateGenericsExample<A extends Comparable<B>, B extends Comparable<A>>() {}\n",
                                           render(DEFAULT, aClass).declaration());

                              assertEquals(
                                    "public record InterpolateGenericsExample<A extends Comparable<B>, B extends Comparable<A>>() {\ntest\n}\n",
                                    render(DEFAULT, aClass).declaration("test"));
                           });

      ConsistencyTest.<Record>compileTime(context -> context.getRecordOrThrow("Parameters"))
                     .runtime(stringClassFunction -> ReflectionAdapter.generalize(stringClassFunction.apply("Parameters")))
                     .withCode("Parameters.java", "@MyAnnotation\n record Parameters(Long id, String name) implements java.io.Serializable {}")
                     .withCode("MyAnnotation.java",
                               "@java.lang.annotation.Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME) @interface MyAnnotation {} ")
                     .test(aClass -> assertEquals("@MyAnnotation\nrecord Parameters(Long id, String name) implements java.io.Serializable {}\n",
                                                  render(DEFAULT, aClass).declaration()));
   }

   @Test
   void type()
   {
      ConsistencyTest.<Record>compileTime(context -> context.getRecordOrThrow("InterpolateGenericsExample"))
                     .runtime(stringClassFunction -> ReflectionAdapter.generalize(stringClassFunction.apply("InterpolateGenericsExample")))
                     .withCode("InterpolateGenericsExample.java",
                               "public record InterpolateGenericsExample<A extends Comparable<B>, B extends Comparable<A>> () {}")
                     .test(aClass -> assertEquals("InterpolateGenericsExample<A extends Comparable<B>, B extends Comparable<A>>",
                                                  render(DEFAULT, aClass).type()),
                           aClass -> assertEquals("InterpolateGenericsExample",
                                                  render(DEFAULT, aClass).type()));
   }
}