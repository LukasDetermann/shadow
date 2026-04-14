package com.derivandi.internal.dsl;


import com.derivandi.api.dsl.RenderingConfiguration;

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
