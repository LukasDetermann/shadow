package io.determann.shadow.tck.internal.renderer;

import io.determann.shadow.api.shadow.structure.C_RecordComponent;
import io.determann.shadow.api.shadow.type.C_Record;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.Operations.RECORD_GET_RECORD_COMPONENT;
import static io.determann.shadow.api.Provider.requestOrThrow;
import static io.determann.shadow.api.renderer.Renderer.render;
import static io.determann.shadow.api.renderer.RenderingContext.DEFAULT;
import static io.determann.shadow.tck.internal.RenderingTestBuilder.renderingTest;

class RecordComponentRendererTest
{
   @Test
   void declaration()
   {
      renderingTest(C_Record.class)
            .withSource("RecordComponentExample.java", "public record RecordComponentExample(Long id){}")
            .withToRender("RecordComponentExample")
            .withRender(cRecord ->
                        {
                           C_RecordComponent recordComponent = requestOrThrow(cRecord, RECORD_GET_RECORD_COMPONENT, "id");
                           return render(DEFAULT, recordComponent).declaration();
                        })
            .withExpected("Long id")
            .test();
   }

   @Test
   void invocation()
   {
      renderingTest(C_Record.class)
            .withSource("RecordComponentExample.java", "public record RecordComponentExample(Long id){}")
            .withToRender("RecordComponentExample")
            .withRender(cRecord ->
                        {
                           C_RecordComponent recordComponent = requestOrThrow(cRecord, RECORD_GET_RECORD_COMPONENT, "id");
                           return render(DEFAULT, recordComponent).invocation();
                        })
            .withExpected("id()")
            .test();
   }
}