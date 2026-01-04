package io.determann.shadow.internal.annotation_processing.shadow.type;

import io.determann.shadow.api.annotation_processing.Ap;
import io.determann.shadow.api.annotation_processing.dsl.RenderingContext;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;

import static io.determann.shadow.api.annotation_processing.dsl.Dsl.annotation;
import static io.determann.shadow.api.annotation_processing.dsl.Dsl.innerAnnotation;

public class AnnotationImpl extends DeclaredImpl implements Ap.Annotation
{
   public AnnotationImpl(Ap.Context context, DeclaredType declaredTypeMirror)
   {
      super(context, declaredTypeMirror);
   }

   public AnnotationImpl(Ap.Context context, TypeElement typeElement)
   {
      super(context, typeElement);
   }

   @Override
   public String renderDeclaration(RenderingContext renderingContext)
   {
      return (switch (getNesting())
              {
                 case OUTER -> annotation().package_(getPackage());
                 case INNER -> innerAnnotation().outer(getSurrounding().orElseThrow());
              }).annotate(getDirectAnnotationUsages())
                .modifier(getModifiers())
                .name(getName())
                .field(getFields())
                .method(getMethods())
                .renderDeclaration(renderingContext);
   }

   @Override
   public String renderType(RenderingContext renderingContext)
   {
      return (switch (getNesting())
              {
                 case OUTER -> annotation().package_(getPackage());
                 case INNER -> innerAnnotation().outer(getSurrounding().orElseThrow());
              }).name(getName())
                .renderType(renderingContext);
   }

   @Override
   public String renderName(RenderingContext renderingContext)
   {
      return renderingContext.renderName(getQualifiedName());
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
   public boolean equals(Object other)
   {
      return equals(Ap.Annotation.class, other);
   }

   @Override
   public String toString()
   {
      return toString("Annotation");
   }
}
