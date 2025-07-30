package io.determann.shadow.api.shadow.structure;

import io.determann.shadow.api.dsl.module.ModuleInfoRenderable;
import io.determann.shadow.api.dsl.module.ModuleNameRenderable;
import io.determann.shadow.api.renderer.RenderingContext;
import io.determann.shadow.api.shadow.C_Annotationable;
import io.determann.shadow.api.shadow.C_Documented;
import io.determann.shadow.api.shadow.C_Nameable;
import io.determann.shadow.api.shadow.C_QualifiedNameable;
import io.determann.shadow.api.shadow.directive.*;

import java.util.List;

import static io.determann.shadow.api.Operations.*;
import static io.determann.shadow.api.Provider.requestOrEmpty;
import static io.determann.shadow.api.Provider.requestOrThrow;
import static io.determann.shadow.api.dsl.Dsl.moduleInfo;
import static java.util.Collections.emptyList;

public interface C_Module
      extends C_Nameable,
              C_QualifiedNameable,
              C_Annotationable,
              C_Documented,
              ModuleInfoRenderable,
              ModuleNameRenderable
{
   @Override
   default String renderModuleInfo(RenderingContext renderingContext)
   {
      List<? extends C_Directive> directives = requestOrEmpty(this, MODULE_GET_DIRECTIVES).orElse(emptyList());

      return moduleInfo().annotate(requestOrEmpty(this, ANNOTATIONABLE_GET_DIRECT_ANNOTATION_USAGES).orElse(emptyList()))
                         .name(requestOrThrow(this, NAMEABLE_GET_NAME))
                         .requires(directives.stream().filter(C_Requires.class::isInstance).map(C_Requires.class::cast).toList())
                         .exports(directives.stream().filter(C_Exports.class::isInstance).map(C_Exports.class::cast).toList())
                         .opens(directives.stream().filter(C_Opens.class::isInstance).map(C_Opens.class::cast).toList())
                         .uses(directives.stream().filter(C_Uses.class::isInstance).map(C_Uses.class::cast).toList())
                         .provides(directives.stream().filter(C_Provides.class::isInstance).map(C_Provides.class::cast).toList())
                         .renderModuleInfo(renderingContext);
   }

   @Override
   default String renderQualifiedName(RenderingContext renderingContext)
   {
      return requestOrThrow(this, QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME);
   }
}
