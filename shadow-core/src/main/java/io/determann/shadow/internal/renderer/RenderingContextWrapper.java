package io.determann.shadow.internal.renderer;

import io.determann.shadow.api.renderer.NameRenderedEvent;
import io.determann.shadow.api.renderer.RenderingContext;
import io.determann.shadow.api.shadow.type.C_Declared;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class RenderingContextWrapper
      implements RenderingContext
{
   private final RenderingContext renderingContext;
   private boolean renderNestedGenerics = true;
   private String receiverType;

   public static RenderingContextWrapper wrap(RenderingContext renderingContext)
   {
      return new RenderingContextWrapper(renderingContext);
   }

   public RenderingContextWrapper(RenderingContext renderingContext) {this.renderingContext = renderingContext;}

   @Override
   public String renderName(C_Declared declared)
   {
      return renderingContext.renderName(declared);
   }

   @Override
   public void onNameRendered(Consumer<NameRenderedEvent> onNameRendered)
   {
      renderingContext.onNameRendered(onNameRendered);
   }

   @Override
   public Function<C_Declared, NameRenderedEvent> getNameRenderer()
   {
      return renderingContext.getNameRenderer();
   }

   @Override
   public List<Consumer<NameRenderedEvent>> getNameRenderedListeners()
   {
      return renderingContext.getNameRenderedListeners();
   }

   public boolean isRenderNestedGenerics()
   {
      return renderNestedGenerics;
   }

   public void setRenderNestedGenerics(boolean renderNestedGenerics)
   {
      this.renderNestedGenerics = renderNestedGenerics;
   }

   public String getReceiverType()
   {
      return receiverType;
   }

   public void setReceiverType(String receiverType)
   {
      this.receiverType = receiverType;
   }
}
