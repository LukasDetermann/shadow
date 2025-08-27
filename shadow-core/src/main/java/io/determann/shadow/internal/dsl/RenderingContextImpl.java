package io.determann.shadow.internal.dsl;

import io.determann.shadow.api.dsl.RenderingContext;
import io.determann.shadow.api.dsl.RenderingContextBuilder;
import io.determann.shadow.api.dsl.import_.ImportRenderable;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

class RenderingContextImpl
      implements RenderingContext
{
   private final ImportRenderingContextImpl importContext;
   private final Deque<Object> surrounding;
   private final int indentation;
   private final String lineIndentation;
   private final int indentationLevel;

   RenderingContextImpl(ImportRenderingContextImpl importContext,
                        Deque<Object> surrounding,
                        int indentation,
                        int indentationLevel)
   {
      this.importContext = importContext;
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
   public List<ImportRenderable> getImports()
   {
      return importContext.getImports();
   }

   @Override
   public String renderName(@Nullable String packageName, String simpleName)
   {
      return importContext.renderName(packageName, simpleName);
   }

   ImportRenderingContextImpl getImportContext()
   {
      return importContext;
   }
}