package io.determann.shadow.api.shadow.type;

import io.determann.shadow.api.dsl.Dsl;
import io.determann.shadow.api.dsl.interface_.*;
import io.determann.shadow.api.renderer.RenderingContext;
import io.determann.shadow.api.shadow.C_Erasable;
import io.determann.shadow.api.shadow.modifier.C_AbstractModifiable;
import io.determann.shadow.api.shadow.modifier.C_Sealable;
import io.determann.shadow.api.shadow.modifier.C_StaticModifiable;

import static io.determann.shadow.api.Operations.*;
import static io.determann.shadow.api.Provider.requestOrEmpty;
import static io.determann.shadow.api.Provider.requestOrThrow;
import static io.determann.shadow.api.dsl.Dsl.class_;
import static java.util.Collections.emptyList;

public interface C_Interface
      extends C_Declared,
              C_AbstractModifiable,
              C_StaticModifiable,
              C_Sealable,
              C_Erasable,
              InterfaceRenderable {
   @Override
   default String renderDeclaration(RenderingContext renderingContext)
   {
      InterfaceImportStep annotateStep = Dsl.interface_().package_(requestOrThrow(this, DECLARED_GET_PACKAGE));
      InterfaceModifierStep modifierStep = requestOrEmpty(this, ANNOTATIONABLE_GET_DIRECT_ANNOTATION_USAGES).map(annotateStep::annotate).orElse(annotateStep);
      InterfaceNameStep nameStep = requestOrEmpty(this, MODIFIABLE_GET_MODIFIERS).map(modifierStep::modifier).orElse(modifierStep);
      InterfaceGenericStep genericStep = nameStep.name(requestOrThrow(this, NAMEABLE_GET_NAME));
      InterfaceExtendsStep extendsStep = requestOrEmpty(this, INTERFACE_GET_GENERICS).map(genericStep::generic).orElse(genericStep);
      InterfacePermitsStep permitsStep = requestOrEmpty(this, DECLARED_GET_DIRECT_INTERFACES).map(extendsStep::extends_).orElse(extendsStep);
      InterfaceBodyStep bodyStep = requestOrEmpty(this, INTERFACE_GET_PERMITTED_SUB_TYPES).map(permitsStep::permits).orElse(permitsStep);
      bodyStep = requestOrEmpty(this, DECLARED_GET_FIELDS).map(bodyStep::field).orElse(bodyStep);
      bodyStep = requestOrEmpty(this, DECLARED_GET_METHODS).map(bodyStep::method).orElse(bodyStep);
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
                     .generic(requestOrEmpty(this, INTERFACE_GET_GENERICS).orElse(emptyList()))
                     .renderType(renderingContext);
   }

   @Override
   default String renderName(RenderingContext renderingContext)
   {
      return renderQualifiedName(renderingContext);
   }
}
