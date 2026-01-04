package io.determann.shadow.api.annotation_processing.dsl;

import org.jetbrains.annotations.Range;

public interface RenderingConfigurationBuilder
{
   /// @see RenderingConfiguration#getIndentation()
   RenderingConfigurationBuilder withIndentation(@Range(from = 0, to = Integer.MAX_VALUE) int indentation);

   /// disables the automatic imports
   ///
   /// @see RenderingContext#getImports()
   /// @see RenderingContext#renderName(String)
   /// @see RenderingContext#renderName(String, String)
   RenderingConfigurationBuilder withoutAutomaticImports();

   RenderingConfiguration build();
}