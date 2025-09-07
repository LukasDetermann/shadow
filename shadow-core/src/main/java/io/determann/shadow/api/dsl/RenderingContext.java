package io.determann.shadow.api.dsl;

import io.determann.shadow.api.dsl.import_.ImportRenderable;
import io.determann.shadow.internal.dsl.RenderingContextImpl;
import org.jetbrains.annotations.Nullable;

import java.util.Deque;
import java.util.List;

import static io.determann.shadow.api.dsl.RenderingConfiguration.DEFAULT_CONFIGURATION;
import static java.util.Objects.requireNonNull;

/// Mutable Context that contains all information needed for rendering.
/// Each call to any `#render()` method should get a new instance of this context
///
/// @see #createRenderingContext()
/// @see #createRenderingContext(RenderingConfiguration)
/// @see #createRenderingContext(RenderingContext)
public interface RenderingContext
{
   /// creates a [RenderingContext] with [RenderingConfiguration#DEFAULT_CONFIGURATION]
   static RenderingContext createRenderingContext()
   {
      return createRenderingContext(DEFAULT_CONFIGURATION);
   }

   /// creates a [RenderingContext] using the [RenderingConfiguration]
   static RenderingContext createRenderingContext(RenderingConfiguration configuration)
   {
      return new RenderingContextImpl(configuration);
   }

   /// creates a copy of the supplied [RenderingContext]
   static RenderingContext createRenderingContext(RenderingContext renderingContext)
   {
      return new RenderingContextImpl(renderingContext);
   }

   /// Returns the objects surrounding the one being currently rendered
   Deque<Object> getSurrounding();

   void addSurrounding(Object surrounding);

   /// [#getLineIndentation()] = " ".repeat([#getIndentationLevel()] * [RenderingConfiguration#getIndentation()])
   ///
   /// @see RenderingConfiguration#getIndentation()
   /// @see #getLineIndentation()
   int getIndentationLevel();

   /// [#getLineIndentation()] = " ".repeat([#getIndentationLevel()] * [RenderingConfiguration#getIndentation()])
   ///
   /// @see RenderingConfiguration#getIndentation()
   /// @see #getIndentationLevel()
   String getLineIndentation();

   /// Should be called by the element owning the code block. e.g. class, method but not field
   ///
   /// @see RenderingContext#getIndentationLevel()
   void incrementIndentationLevel();

   List<ImportRenderable> getImports();

   /// @see RenderingConfigurationBuilder#withoutAutomaticImports()
   /// @see #getImports()
   /// @see #renderName(String)
   String renderName(@Nullable String packageName, String simpleName);

   /// @see RenderingConfigurationBuilder#withoutAutomaticImports()
   /// @see #getImports()
   /// @see #renderName(String, String)
   default String renderName(String qualifiedName)
   {
      requireNonNull(qualifiedName);
      int lastIndexOf = qualifiedName.lastIndexOf('.');
      if (lastIndexOf == -1)
      {
         return renderName(null, qualifiedName);
      }
      String packageName = qualifiedName.substring(0, lastIndexOf);
      String simpleName = qualifiedName.substring(lastIndexOf + 1);
      return renderName(packageName, simpleName);
   }
}