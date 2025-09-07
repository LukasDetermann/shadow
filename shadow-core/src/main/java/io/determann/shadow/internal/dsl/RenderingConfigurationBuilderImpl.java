package io.determann.shadow.internal.dsl;

import io.determann.shadow.api.dsl.RenderingConfiguration;
import io.determann.shadow.api.dsl.RenderingConfigurationBuilder;

public class RenderingConfigurationBuilderImpl
      implements RenderingConfigurationBuilder
{
   private int indentation;
   private boolean automaticImport = true;

   public RenderingConfigurationBuilderImpl()
   {
   }

   public RenderingConfigurationBuilderImpl(RenderingConfiguration configuration)
   {
      this.automaticImport = configuration.isAutoImportEnabled();
      this.indentation = configuration.getIndentation();
   }

   @Override
   public RenderingConfigurationBuilder withIndentation(int indentation)
   {
      if (indentation < 0)
      {
         throw new IllegalArgumentException();
      }
      this.indentation = indentation;
      return this;
   }

   @Override
   public RenderingConfigurationBuilder withoutAutomaticImports()
   {
      this.automaticImport = false;
      return this;
   }

   @Override
   public RenderingConfiguration build()
   {
      return new RenderingConfigurationImpl(indentation, automaticImport);
   }
}
