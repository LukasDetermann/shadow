package io.determann.shadow.api.annotation_processing.dsl;

import io.determann.shadow.internal.annotation_processing.dsl.RenderingConfigurationBuilderImpl;

/// a [RenderingConfiguration] can be used to create [RenderingContext]s
///
/// @see RenderingContext#createRenderingContext(RenderingConfiguration)
public interface RenderingConfiguration
{
   /// Default [RenderingConfiguration]. As new features are added this will be updated.
   RenderingConfiguration DEFAULT_CONFIGURATION = renderingConfigurationBuilder().withIndentation(3).build();

   /// creates a new [RenderingConfiguration]
   static RenderingConfigurationBuilder renderingConfigurationBuilder()
   {
      return new RenderingConfigurationBuilderImpl();
   }

   /// creates a copy of the supplied [RenderingConfiguration]
   static RenderingConfigurationBuilder renderingConfigurationBuilder(RenderingConfiguration configuration)
   {
      return new RenderingConfigurationBuilderImpl(configuration);
   }

   /// @see RenderingContext#getLineIndentation()
   /// @see RenderingContext#getIndentationLevel()
   int getIndentation();

   boolean isAutoImportEnabled();
}