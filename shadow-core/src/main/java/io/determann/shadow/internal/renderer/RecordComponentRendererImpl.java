package io.determann.shadow.internal.renderer;

import io.determann.shadow.api.renderer.RecordComponentRenderer;
import io.determann.shadow.api.renderer.RenderingContext;
import io.determann.shadow.api.shadow.AnnotationUsage;
import io.determann.shadow.api.shadow.Operations;
import io.determann.shadow.api.shadow.structure.RecordComponent;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static io.determann.shadow.api.shadow.Operations.*;
import static io.determann.shadow.api.shadow.Provider.requestOrEmpty;
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

      //noinspection OptionalContainsCollection
      Optional<List<? extends AnnotationUsage>> annotationUsages = requestOrEmpty(recordComponent, Operations.ANNOTATIONABLE_GET_DIRECT_ANNOTATION_USAGES);
      if (!annotationUsages.map(List::isEmpty).orElse(true))
      {
         sb.append(annotationUsages.get()
                                  .stream()
                                  .map(usage -> AnnotationUsageRendererImpl.usage(context, usage) + " ")
                                  .collect(Collectors.joining()));
      }
      sb.append(ShadowRendererImpl.type(context, requestOrThrow(recordComponent, RECORD_COMPONENT_GET_TYPE)))
        .append(' ')
        .append(requestOrThrow(recordComponent, NAMEABLE_GET_NAME));

      return sb.toString();
   }

   public static String invocation(RenderingContextWrapper context, RecordComponent recordComponent)
   {
      return requestOrThrow(requestOrThrow(recordComponent, RECORD_COMPONENT_GET_GETTER), NAMEABLE_GET_NAME) + "()";
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
