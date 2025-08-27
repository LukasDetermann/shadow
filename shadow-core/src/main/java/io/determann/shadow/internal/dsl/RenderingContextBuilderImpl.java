package io.determann.shadow.internal.dsl;

import io.determann.shadow.api.dsl.RenderingContext;
import io.determann.shadow.api.dsl.RenderingContextBuilder;

import java.util.ArrayDeque;
import java.util.Deque;

import static java.util.Objects.requireNonNull;

public class RenderingContextBuilderImpl
      implements RenderingContextBuilder
{
   private final Deque<Object> surrounding = new ArrayDeque<>();
   private int indentation;
   private int indentationLevel;
   private ImportRenderingContextImpl importContext = new ImportRenderingContextImpl();

   public RenderingContextBuilderImpl() {}

   public RenderingContextBuilderImpl(RenderingContext renderingContext)
   {
      this.importContext = ((RenderingContextImpl) renderingContext).getImportContext();
      this.surrounding.addAll(renderingContext.getSurrounding());
      this.indentation = renderingContext.getIndentation();
      this.indentationLevel = renderingContext.getIndentationLevel();
   }

   @Override
   public RenderingContextBuilder withSurrounding(Object surrounding)
   {
      requireNonNull(surrounding);
      this.surrounding.addFirst(surrounding);
      return this;
   }

   @Override
   public RenderingContextBuilder withIndentation(int indentation)
   {
      this.indentation = indentation;
      return this;
   }

   @Override
   public RenderingContextBuilder incrementIndentationLevel()
   {
      indentationLevel++;
      return this;
   }

   @Override
   public RenderingContextBuilder withoutAutomaticImports()
   {
      importContext.disableAutomaticImports();
      return this;
   }

   @Override
   public RenderingContextBuilder withNewImportContext()
   {
      importContext = new ImportRenderingContextImpl(importContext);
      return this;
   }

   public RenderingContext build()
   {
      return new RenderingContextImpl(importContext, surrounding, indentation, indentationLevel);
   }
}
