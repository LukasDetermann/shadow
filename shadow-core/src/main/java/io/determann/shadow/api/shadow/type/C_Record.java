package io.determann.shadow.api.shadow.type;

import io.determann.shadow.api.dsl.Dsl;
import io.determann.shadow.api.dsl.record.*;
import io.determann.shadow.api.renderer.RenderingContext;
import io.determann.shadow.api.shadow.C_Erasable;
import io.determann.shadow.api.shadow.modifier.C_FinalModifiable;
import io.determann.shadow.api.shadow.modifier.C_StaticModifiable;

import static io.determann.shadow.api.Operations.*;
import static io.determann.shadow.api.Provider.requestOrEmpty;
import static io.determann.shadow.api.Provider.requestOrThrow;
import static io.determann.shadow.api.dsl.Dsl.record;
import static java.util.Collections.emptyList;

public interface C_Record
      extends C_Declared,
              C_StaticModifiable,
              C_FinalModifiable,
              C_Erasable,
              RecordRenderable
{
   @Override
   default String renderDeclaration(RenderingContext renderingContext)
   {
      RecordAnnotateStep annotateStep = Dsl.record().package_(requestOrThrow(this, DECLARED_GET_PACKAGE));
      RecordModifierStep modifierStep = requestOrEmpty(this, ANNOTATIONABLE_GET_DIRECT_ANNOTATION_USAGES).map(annotateStep::annotate).orElse(annotateStep);
      RecordNameStep nameStep = requestOrEmpty(this, MODIFIABLE_GET_MODIFIERS).map(modifierStep::modifier).orElse(modifierStep);
      RecordRecordComponentStep recordComponentStep = nameStep.name(requestOrThrow(this, NAMEABLE_GET_NAME));
      RecordGenericStep genericStep = requestOrEmpty(this, RECORD_GET_RECORD_COMPONENTS).map(recordComponentStep::component).orElse(recordComponentStep);
      RecordImplementsStep implementsStep = requestOrEmpty(this, RECORD_GET_GENERICS).map(genericStep::generic).orElse(genericStep);
      RecordBodyStep bodyStep = requestOrEmpty(this, DECLARED_GET_DIRECT_INTERFACES).map(implementsStep::implements_).orElse(implementsStep);
      bodyStep = requestOrEmpty(this, DECLARED_GET_FIELDS).map(bodyStep::field).orElse(bodyStep);
      bodyStep = requestOrEmpty(this, DECLARED_GET_METHODS).map(bodyStep::method).orElse(bodyStep);
      bodyStep = requestOrEmpty(this, RECORD_GET_CONSTRUCTORS).map(bodyStep::constructor).orElse(bodyStep);
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
      return record().package_(requestOrThrow(this, DECLARED_GET_PACKAGE))
                     .name(requestOrThrow(this, NAMEABLE_GET_NAME))
                     .generic(requestOrEmpty(this, RECORD_GET_GENERICS).orElse(emptyList()))
                     .renderType(renderingContext);
   }

   @Override
   default String renderName(RenderingContext renderingContext)
   {
      return renderQualifiedName(renderingContext);
   }
}