package io.determann.shadow.internal.dsl;

import io.determann.shadow.api.dsl.Renderable;
import io.determann.shadow.api.dsl.annotation_usage.AnnotationUsageRenderable;
import io.determann.shadow.api.dsl.receiver.ReceiverAnnotateStep;
import io.determann.shadow.api.renderer.RenderingContext;

import java.util.ArrayList;
import java.util.List;

import static io.determann.shadow.api.renderer.RenderingContextOptions.RECEIVER_TYPE;
import static io.determann.shadow.internal.dsl.DslSupport.addArrayRenderer;
import static io.determann.shadow.internal.dsl.DslSupport.renderElement;

public class ReceiverDsl
      implements ReceiverAnnotateStep
{
   private final List<Renderable> annotations = new ArrayList<>();

   public ReceiverDsl()
   {
   }

   private ReceiverDsl(ReceiverDsl other)
   {
      this.annotations.addAll(other.annotations);
   }

   @Override
   public ReceiverAnnotateStep annotate(String... annotation)
   {
      return addArrayRenderer(new ReceiverDsl(this),
                              annotation,
                              (renderingContext, string) -> '@' + string,
                              receiverDsl -> receiverDsl.annotations::add);
   }

   @Override
   public ReceiverAnnotateStep annotate(List<? extends AnnotationUsageRenderable> annotation)
   {
      return addArrayRenderer(new ReceiverDsl(this),
                              annotation,
                              (renderingContext, renderable) -> renderable.renderDeclaration(renderingContext),
                              receiverDsl -> receiverDsl.annotations::add);
   }

   @Override
   public String renderDeclaration(RenderingContext renderingContext)
   {
      String receiverType = renderingContext.getOption(RECEIVER_TYPE);
      if (receiverType == null)
      {
         throw new IllegalStateException(
               "A Receiver is dependent on its context. it can only be rendered as part of a instance method or inner class constructor");
      }

      StringBuilder sb = new StringBuilder();

      renderElement(sb, annotations, " ", renderingContext, ", ");

      sb.append(receiverType);
      sb.append(' ');
      sb.append(receiverType);
      sb.append(".this");
      return sb.toString();
   }
}
