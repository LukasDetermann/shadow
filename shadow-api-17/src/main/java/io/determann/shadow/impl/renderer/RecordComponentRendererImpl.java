package io.determann.shadow.impl.renderer;

import io.determann.shadow.api.renderer.RecordComponentRenderer;
import io.determann.shadow.api.shadow.RecordComponent;
import io.determann.shadow.impl.annotation_processing.ShadowApiImpl;

import java.util.stream.Collectors;

public class RecordComponentRendererImpl implements RecordComponentRenderer
{
   private final Context context;
   private final RecordComponent recordComponent;

   public RecordComponentRendererImpl(RecordComponent recordComponent)
   {
      this.context = ((ShadowApiImpl) recordComponent.getApi()).getRenderingContext();
      this.recordComponent = recordComponent;
   }

   public static String declaration(Context context, RecordComponent recordComponent)
   {
      StringBuilder sb = new StringBuilder();

      if (!recordComponent.getDirectAnnotationUsages().isEmpty())
      {
         sb.append(recordComponent.getDirectAnnotationUsages()
                                  .stream()
                                  .map(usage -> AnnotationUsageRendererImpl.usage(context, usage) + " ")
                                  .collect(Collectors.joining()));
      }
      sb.append(ShadowRendererImpl.type(context, recordComponent.getType()))
        .append(' ')
        .append(recordComponent.getSimpleName());

      return sb.toString();
   }

   public static String invocation(Context context, RecordComponent recordComponent)
   {
      return recordComponent.getGetter().getSimpleName() + "()";
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
