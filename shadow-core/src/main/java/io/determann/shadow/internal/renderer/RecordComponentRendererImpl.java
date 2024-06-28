package io.determann.shadow.internal.renderer;

import io.determann.shadow.api.renderer.RecordComponentRenderer;
import io.determann.shadow.api.renderer.RenderingContext;
import io.determann.shadow.api.shadow.structure.RecordComponent;

import java.util.stream.Collectors;

import static io.determann.shadow.api.shadow.Operations.*;
import static io.determann.shadow.api.shadow.Provider.requestOrThrow;

public class RecordComponentRendererImpl implements RecordComponentRenderer
{
   private final RenderingContextWrapper context;
   private final RecordComponent recordComponent;

   public RecordComponentRendererImpl(RenderingContext renderingContext, RecordComponent recordComponent)
   {
      this.context = new RenderingContextWrapper(renderingContext);
      this.recordComponent = recordComponent;
   }

   public static String declaration(RenderingContextWrapper context, RecordComponent recordComponent)
   {
      StringBuilder sb = new StringBuilder();

      if (!recordComponent.getDirectAnnotationUsages().isEmpty())
      {
         sb.append(recordComponent.getDirectAnnotationUsages()
                                  .stream()
                                  .map(usage -> AnnotationUsageRendererImpl.usage(context, usage) + " ")
                                  .collect(Collectors.joining()));
      }
      sb.append(ShadowRendererImpl.type(context, requestOrThrow(recordComponent, RECORD_COMPONENT_GET_TYPE)))
        .append(' ')
        .append(requestOrThrow(recordComponent, NAMEABLE_NAME));

      return sb.toString();
   }

   public static String invocation(RenderingContextWrapper context, RecordComponent recordComponent)
   {
      return requestOrThrow(requestOrThrow(recordComponent, RECORD_COMPONENT_GET_GETTER), NAMEABLE_NAME) + "()";
   }

   @Override
   public String declaration()
   {
      return declaration(context, recordComponent);
   }

   @Override
   public String invocation()
   {
      return invocation(context, recordComponent);
   }
}
