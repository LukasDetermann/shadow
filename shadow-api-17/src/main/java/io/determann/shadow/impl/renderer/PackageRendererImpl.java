package io.determann.shadow.impl.renderer;

import io.determann.shadow.api.renderer.PackageRenderer;
import io.determann.shadow.api.shadow.Package;
import io.determann.shadow.impl.ShadowApiImpl;

import java.util.stream.Collectors;

public class PackageRendererImpl implements PackageRenderer
{
   private final Context context;
   private final Package aPackage;

   public PackageRendererImpl(Package aPackage)
   {
      this.context = ((ShadowApiImpl) aPackage.getApi()).getRenderingContext();
      this.aPackage = aPackage;
   }

   public static String declaration(Context context, Package aPackage)
   {
      if (aPackage.isUnnamed())
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
      sb.append(aPackage.getQualifiedName());
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
