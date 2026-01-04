package io.determann.shadow.internal.annotation_processing.shadow.type;

import io.determann.shadow.api.annotation_processing.Ap;
import io.determann.shadow.api.annotation_processing.adapter.Adapters;
import io.determann.shadow.api.annotation_processing.dsl.RenderingContext;

import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import java.util.List;

import static io.determann.shadow.api.annotation_processing.dsl.Dsl.enum_;
import static io.determann.shadow.api.annotation_processing.dsl.Dsl.innerEnum;

public class EnumImpl
      extends DeclaredImpl
      implements Ap.Enum
{
   public EnumImpl(Ap.Context context, DeclaredType declaredTypeMirror)
   {
      super(context, declaredTypeMirror);
   }

   public EnumImpl(Ap.Context context, TypeElement typeElement)
   {
      super(context, typeElement);
   }

   @Override
   public List<Ap.EnumConstant> getEnumConstants()
   {
      return getElement().getEnclosedElements()
                         .stream()
                         .filter(element -> ElementKind.ENUM_CONSTANT.equals(element.getKind()))
                         .map(VariableElement.class::cast)
                         .map(variableElement -> Adapters.adapt(getApi(), variableElement))
                         .map(Ap.EnumConstant.class::cast)
                         .toList();
   }

   @Override
   public String renderDeclaration(RenderingContext renderingContext)
   {
      return (switch (getNesting())
              {
                 case OUTER -> enum_().package_(getPackage());
                 case INNER -> innerEnum().outer(getSurrounding().orElseThrow());
              }).annotate(getDirectAnnotationUsages())
                .modifier(getModifiers())
                .name(getName())
                .implements_(getDirectInterfaces())
                .enumConstant(getEnumConstants())
                .field(getFields())
                .method(getMethods())
                .constructor(getConstructors())
                .renderDeclaration(renderingContext);
   }

   @Override
   public String renderQualifiedName(RenderingContext renderingContext)
   {
      return getQualifiedName();
   }

   @Override
   public String renderSimpleName(RenderingContext renderingContext)
   {
      return getName();
   }

   @Override
   public String renderType(RenderingContext renderingContext)
   {
      return (switch (getNesting())
              {
                 case OUTER -> enum_().package_(getPackage());
                 case INNER -> innerEnum().outer(getSurrounding().orElseThrow());
              }).name(getName())
                .renderType(renderingContext);
   }

   @Override
   public String renderName(RenderingContext renderingContext)
   {
      return renderingContext.renderName(renderQualifiedName(renderingContext));
   }

   @Override
   public boolean equals(Object other)
   {
      return equals(Ap.Enum.class, other);
   }

   @Override
   public String toString()
   {
      return toString("Enum");
   }
}
