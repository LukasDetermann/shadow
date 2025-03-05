package io.determann.shadow.internal.renderer;

import io.determann.shadow.api.Operations;
import io.determann.shadow.api.renderer.ReceiverRenderer;
import io.determann.shadow.api.renderer.RenderingContext;
import io.determann.shadow.api.shadow.C_AnnotationUsage;
import io.determann.shadow.api.shadow.structure.C_Receiver;
import io.determann.shadow.api.shadow.type.C_Type;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static io.determann.shadow.api.Provider.requestOrEmpty;
import static io.determann.shadow.api.Provider.requestOrThrow;

public class ReceiverRendererImpl implements ReceiverRenderer
{
   private final C_Receiver receiver;

   public ReceiverRendererImpl(C_Receiver receiver)
   {
      this.receiver = receiver;
   }

   public static String declaration(RenderingContextWrapper context, C_Receiver receiver)
   {
      StringBuilder sb = new StringBuilder();

      //noinspection OptionalContainsCollection
      Optional<List<? extends C_AnnotationUsage>> annotationUsages = requestOrEmpty(receiver, Operations.ANNOTATIONABLE_GET_DIRECT_ANNOTATION_USAGES);
      if (!annotationUsages.map(List::isEmpty).orElse(true))
      {
         sb.append(annotationUsages.get()
                                   .stream()
                                   .map(usage -> AnnotationUsageRendererImpl.usage(context, usage) + " ")
                                   .collect(Collectors.joining()));
      }

      C_Type type = requestOrThrow(receiver, Operations.RECEIVER_GET_TYPE);
      String renderedType = TypeRendererImpl.type(context, type);
      sb.append(renderedType);
      sb.append(" ");
      sb.append(renderedType);
      sb.append(".this");

      return sb.toString();
   }

   @Override
   public String declaration(RenderingContext renderingContext)
   {
      return declaration(new RenderingContextWrapper(renderingContext), receiver);
   }
}
