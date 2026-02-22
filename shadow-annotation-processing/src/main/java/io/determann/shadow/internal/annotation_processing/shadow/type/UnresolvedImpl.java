package io.determann.shadow.internal.annotation_processing.shadow.type;

import io.determann.shadow.api.annotation_processing.Ap;
import io.determann.shadow.api.annotation_processing.dsl.RenderingContext;

import javax.lang.model.type.ErrorType;

public class UnresolvedImpl
      extends DeclaredImpl
      implements Ap.Unresolved
{
   public UnresolvedImpl(Ap.Context context, ErrorType declaredTypeMirror)
   {
      super(context, declaredTypeMirror);
   }

   @Override
   public String renderDeclaration(RenderingContext renderingContext)
   {
      throw new IllegalStateException("Can not render the declaration of an Unresolved Type");
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
   public String renderName(RenderingContext renderingContext)
   {
      return renderingContext.renderName(renderQualifiedName(renderingContext));
   }

   @Override
   public String renderType(RenderingContext renderingContext)
   {
      return renderName(renderingContext);
   }

   @Override
   public boolean equals(Object other)
   {
      return equals(Ap.Unresolved.class, other);
   }

   @Override
   public String toString()
   {
      return toString("Unresolved");
   }
}