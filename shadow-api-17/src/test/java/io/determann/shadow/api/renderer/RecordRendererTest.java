package io.determann.shadow.api.renderer;

import io.determann.shadow.api.test.ProcessorTest;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.ShadowApi.render;
import static org.junit.jupiter.api.Assertions.assertEquals;

class RecordRendererTest
{
   @Test
   void declaration()
   {
      ProcessorTest.process(shadowApi ->
                            {
                               assertEquals("public record InterpolateGenericsExample<A extends Comparable<B>, B extends Comparable<A>>() {}\n",
                                            render(shadowApi.getRecordOrThrow("InterpolateGenericsExample")).declaration());

                               assertEquals(
                                     "public record InterpolateGenericsExample<A extends Comparable<B>, B extends Comparable<A>>() {\ntest\n}\n",
                                     render(shadowApi.getRecordOrThrow("InterpolateGenericsExample")).declaration("test"));

                               assertEquals("@MyAnnotation\nrecord Parameters(Long id, String name) implements java.io.Serializable {}\n",
                                            render(shadowApi.getRecordOrThrow("InterpolateGenericsExample.Parameters")).declaration());
                            })
                   .withCodeToCompile("InterpolateGenericsExample.java", """
                         public record InterpolateGenericsExample<A extends Comparable<B>, B extends Comparable<A>> () {
                            record IndependentGeneric<C>() {}
                            record DependentGeneric<D extends E, E>() {}
                            @MyAnnotation
                            record Parameters(Long id, String name) implements java.io.Serializable {}
                         }
                         """)
                   .withCodeToCompile("MyAnnotation.java", "@interface MyAnnotation {} ")
                   .compile();
   }

   @Test
   void type()
   {
      ProcessorTest.process(shadowApi ->
                                  assertEquals("InterpolateGenericsExample<A extends Comparable<B>, B extends Comparable<A>>",
                                               render(shadowApi.getRecordOrThrow("InterpolateGenericsExample")).type()))
                   .withCodeToCompile("InterpolateGenericsExample.java", """
                         public record InterpolateGenericsExample<A extends Comparable<B>, B extends Comparable<A>> () {
                            record IndependentGeneric<C> () {}
                            record DependentGeneric<D extends E, E> () {}
                         }
                         """)
                   .compile();
   }
}