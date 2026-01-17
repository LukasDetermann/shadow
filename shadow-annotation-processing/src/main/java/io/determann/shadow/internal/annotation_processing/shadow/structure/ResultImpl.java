package io.determann.shadow.internal.annotation_processing.shadow.structure;

import io.determann.shadow.api.annotation_processing.Ap;
import io.determann.shadow.api.annotation_processing.adapter.Adapters;
import io.determann.shadow.api.annotation_processing.dsl.RenderingContext;

import javax.lang.model.type.TypeMirror;
import java.util.List;
import java.util.Objects;

import static io.determann.shadow.api.annotation_processing.dsl.JavaDsl.result;

public class ResultImpl
      implements Ap.Result
{
   private final Ap.Context context;
   private final TypeMirror typeMirror;

   ResultImpl(Ap.Context context, TypeMirror typeMirror)
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
      return Adapters.adapt(context, getTypeMirror().getAnnotationMirrors());
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
