package io.determann.shadow.api.shadow.type;

import io.determann.shadow.api.dsl.annotation.AnnotationRenderable;
import io.determann.shadow.api.renderer.RenderingContext;
import io.determann.shadow.api.shadow.modifier.C_StaticModifiable;

import static io.determann.shadow.api.Operations.*;
import static io.determann.shadow.api.Provider.requestOrEmpty;
import static io.determann.shadow.api.Provider.requestOrThrow;
import static io.determann.shadow.api.dsl.Dsl.annotation;
import static java.util.Collections.emptyList;
import static java.util.Collections.emptySet;

public interface C_Annotation
      extends C_Declared,
              C_StaticModifiable,
              AnnotationRenderable
{
   @Override
   default String renderDeclaration(RenderingContext renderingContext)
   {
      return annotation().package_(requestOrThrow(this, DECLARED_GET_PACKAGE))
                         .annotate(requestOrEmpty(this, ANNOTATIONABLE_GET_DIRECT_ANNOTATION_USAGES).orElse(emptyList()))
                         .modifier(requestOrEmpty(this, MODIFIABLE_GET_MODIFIERS).orElse(emptySet()))
                         .name(requestOrThrow(this, NAMEABLE_GET_NAME))
                         .field(requestOrEmpty(this, DECLARED_GET_FIELDS).orElse(emptyList()))
                         .method(requestOrEmpty(this, DECLARED_GET_METHODS).orElse(emptyList()))
                         .renderDeclaration(renderingContext);
   }

   @Override
   default String renderType(RenderingContext renderingContext)
   {
      return renderQualifiedName(renderingContext);
   }

   @Override
   default String renderName(RenderingContext renderingContext)
   {
      return renderQualifiedName(renderingContext);
   }

   @Override
   default String renderQualifiedName(RenderingContext renderingContext)
   {
      return requestOrThrow(this, QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME);
   }
}