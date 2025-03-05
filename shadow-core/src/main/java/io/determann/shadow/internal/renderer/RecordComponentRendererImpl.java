package io.determann.shadow.internal.renderer;

import io.determann.shadow.api.Operations;
import io.determann.shadow.api.renderer.RecordComponentRenderer;
import io.determann.shadow.api.renderer.RenderingContext;
import io.determann.shadow.api.shadow.C_AnnotationUsage;
import io.determann.shadow.api.shadow.structure.C_RecordComponent;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static io.determann.shadow.api.Operations.*;
import static io.determann.shadow.api.Provider.requestOrEmpty;
import static io.determann.shadow.api.Provider.requestOrThrow;

public class RecordComponentRendererImpl implements RecordComponentRenderer
{
   private final C_RecordComponent recordComponent;

   public RecordComponentRendererImpl(C_RecordComponent recordComponent)
   {
      this.recordComponent = recordComponent;
   }

   public static String declaration(RenderingContextWrapper context, C_RecordComponent recordComponent)
   {
      StringBuilder sb = new StringBuilder();

      //noinspection OptionalContainsCollection
      Optional<List<? extends C_AnnotationUsage>> annotationUsages = requestOrEmpty(recordComponent, Operations.ANNOTATIONABLE_GET_DIRECT_ANNOTATION_USAGES);
      if (!annotationUsages.map(List::isEmpty).orElse(true))
      {
         sb.append(annotationUsages.get()
                                  .stream()
                                  .map(usage -> AnnotationUsageRendererImpl.usage(context, usage) + " ")
                                  .collect(Collectors.joining()));
      }
      sb.append(TypeRendererImpl.type(context, requestOrThrow(recordComponent, RECORD_COMPONENT_GET_TYPE)))
        .append(' ')
        .append(requestOrThrow(recordComponent, NAMEABLE_GET_NAME));

      return sb.toString();
   }

   public static String invocation(RenderingContextWrapper context, C_RecordComponent recordComponent)
   {
      return requestOrThrow(requestOrThrow(recordComponent, RECORD_COMPONENT_GET_GETTER), NAMEABLE_GET_NAME) + "()";
   }

   @Override
   public String declaration(RenderingContext renderingContext)
   {
      return declaration(new RenderingContextWrapper(renderingContext), recordComponent);
   }

   @Override
   public String invocation(RenderingContext renderingContext)
   {
      return invocation(new RenderingContextWrapper(renderingContext), recordComponent);
   }
}
