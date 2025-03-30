package io.determann.shadow.internal.renderer;

import io.determann.shadow.api.renderer.PackageRenderer;
import io.determann.shadow.api.renderer.RenderingContext;
import io.determann.shadow.api.shadow.structure.C_Package;

import static io.determann.shadow.api.Operations.PACKAGE_IS_UNNAMED;
import static io.determann.shadow.api.Operations.QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME;
import static io.determann.shadow.api.Provider.requestOrThrow;

public class PackageRendererImpl implements PackageRenderer
{
   private final C_Package aPackage;

   public PackageRendererImpl(C_Package aPackage)
   {
      this.aPackage = aPackage;
   }

   public static String declaration(RenderingContextWrapper context, C_Package aPackage)
   {
      if (requestOrThrow(aPackage, PACKAGE_IS_UNNAMED))
      {
         throw new IllegalArgumentException("unnamed package");
      }

      return RenderingSupport.annotations(context, aPackage, '\n') +
             "package " +
             requestOrThrow(aPackage, QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME) +
             ';' +
             '\n';
   }

   @Override
   public String declaration(RenderingContext renderingContext)
   {
      return declaration(new RenderingContextWrapper(renderingContext), aPackage);
   }
}
