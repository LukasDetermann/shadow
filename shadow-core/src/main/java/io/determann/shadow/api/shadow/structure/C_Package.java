package io.determann.shadow.api.shadow.structure;

import io.determann.shadow.api.dsl.package_.PackageInfoRenderable;
import io.determann.shadow.api.dsl.package_.PackageRenderable;
import io.determann.shadow.api.renderer.RenderingContext;
import io.determann.shadow.api.shadow.*;

import static io.determann.shadow.api.Operations.ANNOTATIONABLE_GET_DIRECT_ANNOTATION_USAGES;
import static io.determann.shadow.api.Operations.QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME;
import static io.determann.shadow.api.Provider.requestOrEmpty;
import static io.determann.shadow.api.Provider.requestOrThrow;
import static io.determann.shadow.api.dsl.Dsl.packageInfo;
import static java.util.Collections.emptyList;

public interface C_Package
      extends C_Nameable,
              C_QualifiedNameable,
              C_Annotationable,
              C_ModuleEnclosed,
              C_Documented,
              PackageInfoRenderable,
              PackageRenderable
{
   @Override
   default String renderPackageInfo(RenderingContext renderingContext)
   {
      return packageInfo().annotate(requestOrEmpty(this, ANNOTATIONABLE_GET_DIRECT_ANNOTATION_USAGES).orElse(emptyList()))
                          .name(requestOrThrow(this, QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME))
                          .renderPackageInfo(renderingContext);
   }

   @Override
   default String renderQualifiedName(RenderingContext renderingContext)
   {
      return requestOrThrow(this, QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME);
   }
}
