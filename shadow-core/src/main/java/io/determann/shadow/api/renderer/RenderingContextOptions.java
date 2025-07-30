package io.determann.shadow.api.renderer;

import io.determann.shadow.api.dsl.Dsl;

public interface RenderingContextOptions
{
   /// renders a generic usage. especially useful for Recursive generics
   ///
   /// @see Dsl#generic()
   RenderingContextOption<Boolean> GENERIC_USAGE = new RenderingContextOption<>();

   /// sets the type of the receiver
   RenderingContextOption<String> RECEIVER_TYPE = new RenderingContextOption<>();
}
