package io.determann.shadow.api.renderer;

import io.determann.shadow.api.test.ProcessorTest;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.renderer.Renderer.render;
import static io.determann.shadow.api.renderer.RenderingContext.DEFAULT;
import static org.junit.jupiter.api.Assertions.assertEquals;

class RecordComponentRendererTest
{
   @Test
   void declaration()
   {
      ProcessorTest.process(shadowApi ->
                                  assertEquals("Long id",
                                               render(DEFAULT, shadowApi.getRecordOrThrow("RecordComponentExample")
                                                               .getRecordComponentOrThrow("id")).declaration()))
                   .withCodeToCompile("RecordComponentExample.java", "public record RecordComponentExample(Long id){}")
                   .compile();
   }

   @Test
   void invocation()
   {
      ProcessorTest.process(shadowApi ->
                                  assertEquals("id()",
                                               render(DEFAULT, shadowApi.getRecordOrThrow("RecordComponentExample")
                                                               .getRecordComponentOrThrow("id")).invocation()))
                   .withCodeToCompile("RecordComponentExample.java", "public record RecordComponentExample(Long id){}")
                   .compile();
   }
}