package io.determann.shadow.api.dsl;

public interface RenderingContextBuilder
{
   RenderingContextBuilder withSurrounding(Object surrounding);

   /// @see RenderingContext#getIndentation()
   RenderingContextBuilder withIndentation(int indentation);

   /// Should be called by the element owning the code block. e.g. class, method but not field
   ///
   /// @see RenderingContext#getIndentationLevel()
   RenderingContextBuilder incrementIndentationLevel();

   /// disables the automatic imports for used types
   ///
   /// @see #withNewImportContext()
   /// @see RenderingContext#getImports()
   /// @see RenderingContext#renderName(String)
   /// @see RenderingContext#renderName(String, String)
   RenderingContextBuilder withoutAutomaticImports();

   /// starts a new import context
   ///
   /// @see #withoutAutomaticImports()
   /// @see RenderingContext#getImports()
   /// @see RenderingContext#renderName(String)
   /// @see RenderingContext#renderName(String, String)
   RenderingContextBuilder withNewImportContext();

   RenderingContext build();
}
