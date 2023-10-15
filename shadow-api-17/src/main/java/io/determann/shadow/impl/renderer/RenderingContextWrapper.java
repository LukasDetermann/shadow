package io.determann.shadow.impl.renderer;

import io.determann.shadow.api.renderer.NameRenderedEvent;
import io.determann.shadow.api.renderer.RenderingContext;
import io.determann.shadow.api.shadow.Declared;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class RenderingContextWrapper implements RenderingContext
{
   private final RenderingContext renderingContext;
   private boolean renderNestedGenerics = true;

   public RenderingContextWrapper(RenderingContext renderingContext) {this.renderingContext = renderingContext;}

   @Override
   public String renderName(Declared declared)
   {
      return renderingContext.renderName(declared);
   }

   @Override
   public void onNameRendered(Consumer<NameRenderedEvent> onNameRendered)
   {
      renderingContext.onNameRendered(onNameRendered);
   }

   @Override
   public Function<Declared, NameRenderedEvent> getNameRenderer()
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
}
