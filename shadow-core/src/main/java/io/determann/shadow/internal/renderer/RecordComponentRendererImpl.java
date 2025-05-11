package io.determann.shadow.internal.renderer;

import io.determann.shadow.api.renderer.RecordComponentRenderer;
import io.determann.shadow.api.renderer.RenderingContext;
import io.determann.shadow.api.shadow.structure.C_RecordComponent;

import static io.determann.shadow.api.Operations.*;
import static io.determann.shadow.api.Provider.requestOrThrow;
import static io.determann.shadow.internal.renderer.RenderingContextWrapper.wrap;

public class RecordComponentRendererImpl implements RecordComponentRenderer
{
   private final C_RecordComponent recordComponent;

   public RecordComponentRendererImpl(C_RecordComponent recordComponent)
   {
      this.recordComponent = recordComponent;
   }

   public static String declaration(RenderingContextWrapper context, C_RecordComponent recordComponent)
   {
      return RenderingSupport.annotations(context, recordComponent, '\n') +
             TypeRendererImpl.type(context, requestOrThrow(recordComponent, RECORD_COMPONENT_GET_TYPE)) +
             ' ' +
             requestOrThrow(recordComponent, NAMEABLE_GET_NAME);
   }

   public static String invocation(RenderingContextWrapper context, C_RecordComponent recordComponent)
   {
      return requestOrThrow(requestOrThrow(recordComponent, RECORD_COMPONENT_GET_GETTER), NAMEABLE_GET_NAME) + "()";
   }

   @Override
   public String declaration(RenderingContext renderingContext)
   {
      return declaration(wrap(renderingContext), recordComponent);
   }

   @Override
   public String invocation(RenderingContext renderingContext)
   {
      return invocation(wrap(renderingContext), recordComponent);
   }
}
