package com.derivandi.internal.shadow.structure;

import com.derivandi.api.Ap;
import com.derivandi.api.adapter.Adapters;
import com.derivandi.api.dsl.RenderingContext;
import com.derivandi.api.processor.SimpleContext;

import javax.lang.model.type.TypeMirror;
import java.util.List;
import java.util.Objects;

import static com.derivandi.api.dsl.JavaDsl.result;

public class ResultImpl
      implements Ap.Result
{
   private final SimpleContext context;
   private final TypeMirror typeMirror;

   ResultImpl(SimpleContext context, TypeMirror typeMirror)
   {
      this.context = context;
      this.typeMirror = typeMirror;
   }

   @Override
   public List<Ap.AnnotationUsage> getAnnotationUsages()
   {
      return getDirectAnnotationUsages();
   }

   @Override
   public List<Ap.AnnotationUsage> getDirectAnnotationUsages()
   {
      return Adapters.adapt(context, getTypeMirror(), getTypeMirror().getAnnotationMirrors());
   }

   @Override
   public Ap.Type getType()
   {
      return Adapters.adapt(context, getTypeMirror());
   }

   public TypeMirror getTypeMirror()
   {
      return typeMirror;
   }

   @Override
   public String renderDeclaration(RenderingContext renderingContext)
   {
      return result().annotate(getDirectAnnotationUsages())
                     .type(getType())
                     .renderDeclaration(renderingContext);
   }

   @Override
   public int hashCode()
   {
      return Objects.hashCode(getType());
   }

   @Override
   public boolean equals(Object other)
   {
      if (other == this)
      {
         return true;
      }
      if (!(other instanceof Ap.Result otherReturn))
      {
         return false;
      }
      return Objects.equals(getType(), otherReturn.getType());
   }
}
