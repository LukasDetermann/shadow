package io.determann.shadow.internal.dsl;

import io.determann.shadow.api.dsl.RenderingConfiguration;
import io.determann.shadow.api.dsl.RenderingContext;
import io.determann.shadow.api.dsl.import_.ImportRenderable;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

public class RenderingContextImpl
      implements RenderingContext
{
   private final RenderingConfiguration configuration;
   private ImportRenderingContextImpl importContext;
   private final Deque<Object> surrounding;
   private String lineIndentation;
   private int indentationLevel;

   public RenderingContextImpl(RenderingContext renderingContext)
   {
      RenderingContextImpl other = (RenderingContextImpl) renderingContext;

      this.configuration = other.configuration;
      this.importContext = other.importContext;
      this.surrounding = new ArrayDeque<>(other.surrounding);
      this.lineIndentation = other.lineIndentation;
      this.indentationLevel = other.indentationLevel;
   }

   public RenderingContextImpl(RenderingConfiguration configuration)
   {
      this.configuration = configuration;
      this.importContext = new ImportRenderingContextImpl(configuration);
      this.surrounding = new ArrayDeque<>();
      this.lineIndentation = "";
      this.indentationLevel = 0;
   }

   @Override
   public Deque<Object> getSurrounding()
   {
      return new ArrayDeque<>(surrounding);
   }

   @Override
   public void addSurrounding(Object surrounding)
   {
      this.surrounding.push(surrounding);
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
   public void incrementIndentationLevel()
   {
      indentationLevel++;
      lineIndentation = lineIndentation + " ".repeat(configuration.getIndentation());
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