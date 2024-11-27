package io.determann.shadow.tck.internal.renderer;

import io.determann.shadow.api.shadow.structure.C_RecordComponent;
import io.determann.shadow.api.shadow.type.C_Record;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.Operations.GET_RECORD;
import static io.determann.shadow.api.Operations.RECORD_GET_RECORD_COMPONENT;
import static io.determann.shadow.api.Provider.requestOrThrow;
import static io.determann.shadow.api.renderer.Renderer.render;
import static io.determann.shadow.api.renderer.RenderingContext.DEFAULT;
import static io.determann.shadow.tck.internal.TckTest.withSource;
import static org.junit.jupiter.api.Assertions.assertEquals;

class RecordComponentRendererTest
{
   @Test
   void declaration()
   {
      withSource("RecordComponentExample.java", "public record RecordComponentExample(Long id){}")
            .test(implementation ->
                  {
                     C_Record record = requestOrThrow(implementation, GET_RECORD, "RecordComponentExample");
                     C_RecordComponent recordComponent = requestOrThrow(record, RECORD_GET_RECORD_COMPONENT, "id");
                     assertEquals("Long id", render(DEFAULT, recordComponent).declaration());
                  });
   }

   @Test
   void invocation()
   {
      withSource("RecordComponentExample.java", "public record RecordComponentExample(Long id){}")
            .test(implementation ->
                  {
                     C_Record record = requestOrThrow(implementation, GET_RECORD, "RecordComponentExample");
                     C_RecordComponent recordComponent = requestOrThrow(record, RECORD_GET_RECORD_COMPONENT, "id");
                     assertEquals("id()", render(DEFAULT, recordComponent).invocation());
                  });
   }
}