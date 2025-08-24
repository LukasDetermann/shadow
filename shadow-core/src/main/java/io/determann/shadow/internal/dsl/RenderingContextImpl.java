package io.determann.shadow.internal.dsl;

import io.determann.shadow.api.dsl.NameRenderedEvent;
import io.determann.shadow.api.dsl.NameRenderer;
import io.determann.shadow.api.dsl.RenderingContext;
import io.determann.shadow.api.dsl.RenderingContextBuilder;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.function.Consumer;

import static java.util.Collections.unmodifiableList;

class RenderingContextImpl
      implements RenderingContext
{
   private final NameRenderer nameRenderer;
   private final List<Consumer<NameRenderedEvent>> nameRenderedListeners;
   private final Deque<Object> surrounding;
   private final int indentation;
   private final String lineIndentation;
   private final int indentationLevel;

   RenderingContextImpl(NameRenderer nameRenderer,
                        List<Consumer<NameRenderedEvent>> nameRenderedListeners,
                        Deque<Object> surrounding,
                        int indentation,
                        int indentationLevel)
   {
      this.nameRenderer = nameRenderer;
      this.nameRenderedListeners = nameRenderedListeners;
      this.surrounding = surrounding;
      this.indentation = indentation;
      this.indentationLevel = indentationLevel;
      this.lineIndentation = " ".repeat(indentation * indentationLevel);
   }

   @Override
   public RenderingContextBuilder builder()
   {
      return RenderingContext.renderingContextBuilder(this);
   }

   @Override
   public Deque<Object> getSurrounding()
   {
      return new ArrayDeque<>(surrounding);
   }

   @Override
   public int getIndentation()
   {
      return indentation;
   }

   @Override
   public int getIndentationLevel()
   {
      return indentationLevel;
   }

   @Override
   public String getLineIndentation()
   {
      return lineIndentation;
   }

   @Override
   public void onNameRendered(Consumer<NameRenderedEvent> onNameRendered)
   {
      nameRenderedListeners.add(onNameRendered);
   }

   public NameRenderer getNameRenderer()
   {
      return nameRenderer;
   }

   public List<Consumer<NameRenderedEvent>> getNameRenderedListeners()
   {
      return unmodifiableList(nameRenderedListeners);
   }
}