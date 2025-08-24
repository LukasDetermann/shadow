package io.determann.shadow.api.dsl;

import io.determann.shadow.internal.dsl.RenderingContextBuilderImpl;

import java.util.Deque;
import java.util.List;
import java.util.function.Consumer;

import static java.util.Objects.requireNonNull;

public interface RenderingContext
{
   RenderingContext DEFAULT = renderingContextBuilder().withNamesWithoutNeedingImports().withIndentation(3).build();

   static RenderingContextBuilder renderingContextBuilder()
   {
      return new RenderingContextBuilderImpl();
   }

   /// creates a builder to modify values
   static RenderingContextBuilder renderingContextBuilder(RenderingContext context)
   {
      requireNonNull(context);
      return new RenderingContextBuilderImpl(context);
   }

   RenderingContextBuilder builder();

   /// Returns the objects surrounding the one being currently rendered
   Deque<Object> getSurrounding();

   /// [#getLineIndentation()] = " ".repeat([#getIndentationLevel()] * [#getIndentation()])
   ///
   /// @see #getLineIndentation()
   /// @see #getIndentationLevel()
   int getIndentation();

   /// [#getLineIndentation()] = " ".repeat([#getIndentationLevel()] * [#getIndentation()])
   ///
   /// @see #getIndentation()
   /// @see #getLineIndentation()
   int getIndentationLevel();

   /// [#getLineIndentation()] = " ".repeat([#getIndentationLevel()] * [#getIndentation()])
   ///
   /// @see #getIndentation()
   /// @see #getIndentationLevel()
   String getLineIndentation();

   void onNameRendered(Consumer<NameRenderedEvent> onNameRendered);

   List<Consumer<NameRenderedEvent>> getNameRenderedListeners();

   NameRenderer getNameRenderer();
}
