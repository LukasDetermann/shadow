package io.determann.shadow.internal.annotation_processing.dsl;


import io.determann.shadow.api.annotation_processing.dsl.RenderingConfiguration;

public class RenderingConfigurationImpl
      implements RenderingConfiguration
{
   private final int indentation;
   private final boolean automaticImport;

   RenderingConfigurationImpl(int indentation, boolean automaticImport)
   {
      this.indentation = indentation;
      this.automaticImport = automaticImport;
   }

   @Override
   public int getIndentation()
   {
      return indentation;
   }

   @Override
   public boolean isAutoImportEnabled()
   {
      return automaticImport;
   }
}
