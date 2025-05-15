package io.determann.shadow.internal.dsl;

import io.determann.shadow.api.dsl.receiver.ReceiverAnnotateStep;
import io.determann.shadow.api.renderer.Renderer;
import io.determann.shadow.api.renderer.RenderingContext;
import io.determann.shadow.api.shadow.type.C_Annotation;
import io.determann.shadow.internal.renderer.RenderingContextWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static io.determann.shadow.internal.dsl.DslSupport.addArrayRenderer;
import static io.determann.shadow.internal.dsl.DslSupport.renderElement;

public class ReceiverDsl
      implements ReceiverAnnotateStep
{
   private final List<Function<RenderingContext, String>> annotations = new ArrayList<>();

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
   public ReceiverAnnotateStep annotate(C_Annotation... annotation)
   {
      return addArrayRenderer(new ReceiverDsl(this),
                              annotation,
                              (renderingContext, modifier) -> Renderer.render(modifier).declaration(renderingContext),
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
