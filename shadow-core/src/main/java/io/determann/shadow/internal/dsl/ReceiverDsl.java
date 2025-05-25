package io.determann.shadow.internal.dsl;

import io.determann.shadow.api.dsl.Renderable;
import io.determann.shadow.api.dsl.annotation_usage.AnnotationUsageRenderable;
import io.determann.shadow.api.dsl.receiver.ReceiverAnnotateStep;
import io.determann.shadow.api.renderer.Renderer;
import io.determann.shadow.api.renderer.RenderingContext;
import io.determann.shadow.api.shadow.C_AnnotationUsage;
import io.determann.shadow.internal.renderer.RenderingContextWrapper;

import java.util.ArrayList;
import java.util.List;

import static io.determann.shadow.internal.dsl.DslSupport.*;

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
      return addArrayRenderer(new ReceiverDsl(this), annotation, receiverDsl -> receiverDsl.annotations::add);
   }

   @Override
   public ReceiverAnnotateStep annotate(C_AnnotationUsage... annotation)
   {
      return addArrayRenderer(new ReceiverDsl(this),
                              annotation,
                              (renderingContext, modifier) -> Renderer.render(modifier).declaration(renderingContext),
                              receiverDsl -> receiverDsl.annotations::add);
   }

   @Override
   public ReceiverAnnotateStep annotate(AnnotationUsageRenderable... annotation)
   {
      return addArray(new ReceiverDsl(this),
                      annotation,
                      receiverDsl -> receiverDsl.annotations::add);
   }

   @Override
   public String render(RenderingContext renderingContext)
   {
      if (!(renderingContext instanceof RenderingContextWrapper wrapper) || wrapper.getReceiverType() == null)
      {
         throw new IllegalStateException("A Receiver is dependent on its context. it can only be rendered as part of a instance method or inner class constructor");
      }

      StringBuilder sb = new StringBuilder();

      renderElement(sb, annotations, " ", renderingContext, ", ");

      sb.append(wrapper.getReceiverType());
      sb.append(' ');
      sb.append(wrapper.getReceiverType());
      sb.append(".this");
      return sb.toString();
   }
}
