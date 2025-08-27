package io.determann.shadow.api.dsl;

import io.determann.shadow.api.dsl.import_.ImportRenderable;
import io.determann.shadow.internal.dsl.RenderingContextBuilderImpl;
import org.jetbrains.annotations.Nullable;

import java.util.Deque;
import java.util.List;

import static java.util.Objects.requireNonNull;

public interface RenderingContext
{
   RenderingContext DEFAULT = renderingContextBuilder().withIndentation(3).build();

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

   List<ImportRenderable> getImports();

   /// @see RenderingContextBuilder#withNewImportContext()
   /// @see RenderingContextBuilder#withoutAutomaticImports()
   /// @see #getImports()
   /// @see #renderName(String)
   String renderName(@Nullable String packageName, String simpleName);

   /// @see RenderingContextBuilder#withNewImportContext()
   /// @see RenderingContextBuilder#withoutAutomaticImports()
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
