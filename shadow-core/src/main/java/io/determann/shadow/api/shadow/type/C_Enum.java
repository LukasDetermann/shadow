package io.determann.shadow.api.shadow.type;

import io.determann.shadow.api.dsl.Dsl;
import io.determann.shadow.api.dsl.enum_.*;
import io.determann.shadow.api.renderer.RenderingContext;
import io.determann.shadow.api.shadow.modifier.C_StaticModifiable;

import static io.determann.shadow.api.Operations.*;
import static io.determann.shadow.api.Provider.requestOrEmpty;
import static io.determann.shadow.api.Provider.requestOrThrow;
import static io.determann.shadow.api.dsl.Dsl.class_;

public interface C_Enum
      extends C_Declared,
              C_StaticModifiable,
              EnumRenderable
{
   @Override
   default String renderDeclaration(RenderingContext renderingContext)
   {
      EnumAnnotateStep annotateStep = Dsl.enum_().package_(requestOrThrow(this, DECLARED_GET_PACKAGE));
      EnumModifierStep modifierStep = requestOrEmpty(this, ANNOTATIONABLE_GET_DIRECT_ANNOTATION_USAGES).map(annotateStep::annotate).orElse(annotateStep);
      EnumNameStep nameStep = requestOrEmpty(this, MODIFIABLE_GET_MODIFIERS).map(modifierStep::modifier).orElse(modifierStep);
      EnumImplementsStep implementsStep = nameStep.name(requestOrThrow(this, NAMEABLE_GET_NAME));
      EnumEnumConstantStep enumEnumConstantStep = requestOrEmpty(this, DECLARED_GET_DIRECT_INTERFACES).map(implementsStep::implements_).orElse(implementsStep);
      EnumBodyStep bodyStep = requestOrEmpty(this, ENUM_GET_ENUM_CONSTANTS).map(enumEnumConstantStep::enumConstant).orElse(enumEnumConstantStep);
      bodyStep = requestOrEmpty(this, DECLARED_GET_FIELDS).map(bodyStep::field).orElse(bodyStep);
      bodyStep = requestOrEmpty(this, DECLARED_GET_METHODS).map(bodyStep::method).orElse(bodyStep);
      bodyStep = requestOrEmpty(this, ENUM_GET_CONSTRUCTORS).map(bodyStep::constructor).orElse(bodyStep);
      return bodyStep.renderDeclaration(renderingContext);
   }

   @Override
   default String renderQualifiedName(RenderingContext renderingContext)
   {
      return requestOrThrow(this, QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME);
   }

   @Override
   default String renderType(RenderingContext renderingContext)
   {
      return class_().package_(requestOrThrow(this, DECLARED_GET_PACKAGE))
                     .name(requestOrThrow(this, NAMEABLE_GET_NAME))
                     .renderType(renderingContext);
   }

   @Override
   default String renderName(RenderingContext renderingContext)
   {
      return renderQualifiedName(renderingContext);
   }
}
