package io.determann.shadow.internal.renderer;

import io.determann.shadow.api.renderer.PackageRenderer;
import io.determann.shadow.api.renderer.RenderingContext;
import io.determann.shadow.api.shadow.Operations;
import io.determann.shadow.api.shadow.annotationusage.AnnotationUsage;
import io.determann.shadow.api.shadow.structure.Package;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static io.determann.shadow.api.shadow.Operations.PACKAGE_IS_UNNAMED;
import static io.determann.shadow.api.shadow.Operations.QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME;
import static io.determann.shadow.api.shadow.Provider.requestOrEmpty;
import static io.determann.shadow.api.shadow.Provider.requestOrThrow;

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

      //noinspection OptionalContainsCollection
      Optional<List<AnnotationUsage>> annotationUsages = requestOrEmpty(aPackage, Operations.ANNOTATIONABLE_GET_DIRECT_ANNOTATION_USAGES);
      if (!annotationUsages.map(List::isEmpty).orElse(true))
      {
         sb.append(annotationUsages.get()
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
