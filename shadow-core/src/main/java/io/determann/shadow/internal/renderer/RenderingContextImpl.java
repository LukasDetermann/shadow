package io.determann.shadow.internal.renderer;

import io.determann.shadow.api.renderer.NameRenderedEvent;
import io.determann.shadow.api.renderer.NameRenderer;
import io.determann.shadow.api.renderer.RenderingContext;
import io.determann.shadow.api.renderer.RenderingContextBuilder;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.function.Consumer;

import static java.util.Collections.unmodifiableList;
import static java.util.Objects.requireNonNull;

public class RenderingContextImpl
      implements RenderingContext
{
   private final NameRenderer nameRenderer;
   private final List<Consumer<NameRenderedEvent>> nameRenderedListeners;
   private final Deque<Object> surrounding;

   RenderingContextImpl(NameRenderer nameRenderer,
                        List<Consumer<NameRenderedEvent>> nameRenderedListeners,
                        Deque<Object> surrounding)
   {
      this.nameRenderer = nameRenderer;
      this.nameRenderedListeners = nameRenderedListeners;
      this.surrounding = surrounding;
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
   public <TYPE> TYPE getSurrounding(Class<TYPE> typeClass)
   {
      return getSurrounding(0, typeClass);
   }

   @Override
   public <TYPE> TYPE getSurrounding(int level, Class<TYPE> typeClass)
   {
      requireNonNull(typeClass);
      int counter = 0;
      for (Object object : surrounding)
      {
         if (typeClass.isInstance(object))
         {
            if (level == counter)
            {
               //noinspection unchecked
               return ((TYPE) object);
            }
            counter++;
         }
      }
      throw new IllegalStateException("Surrounding " + typeClass.getName() + " at level " + level + " not found");
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