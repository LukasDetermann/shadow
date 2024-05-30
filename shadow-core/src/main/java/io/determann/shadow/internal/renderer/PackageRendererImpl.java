package io.determann.shadow.internal.renderer;

import io.determann.shadow.api.renderer.PackageRenderer;
import io.determann.shadow.api.renderer.RenderingContext;
import io.determann.shadow.api.shadow.Package;

import java.util.stream.Collectors;

import static io.determann.shadow.meta_meta.Operations.PACKAGE_IS_UNNAMED;
import static io.determann.shadow.meta_meta.Operations.QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME;
import static io.determann.shadow.meta_meta.Provider.requestOrThrow;

public class PackageRendererImpl implements PackageRenderer
{
   private final RenderingContextWrapper context;
   private final Package aPackage;

   public PackageRendererImpl(RenderingContext renderingContext, Package aPackage)
   {
      this.context = new RenderingContextWrapper(renderingContext);
      this.aPackage = aPackage;
   }

   public static String declaration(RenderingContextWrapper context, Package aPackage)
   {
      if (requestOrThrow(aPackage, PACKAGE_IS_UNNAMED))
      {
         throw new IllegalArgumentException("unnamed package");
      }
      StringBuilder sb = new StringBuilder();

      if (!aPackage.getDirectAnnotationUsages().isEmpty())
      {
         sb.append(aPackage.getDirectAnnotationUsages()
                           .stream()
                           .map(usage -> AnnotationUsageRendererImpl.usage(context, usage) + "\n")
                           .collect(Collectors.joining()));
      }
      sb.append("package ");
      sb.append(requestOrThrow(aPackage, QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME));
      sb.append(';');
      sb.append('\n');
      return sb.toString();
   }

   @Override
   public String declaration()
   {
      return declaration(context, aPackage);
   }
}
