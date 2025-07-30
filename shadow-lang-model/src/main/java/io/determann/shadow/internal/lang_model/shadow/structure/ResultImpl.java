package io.determann.shadow.internal.lang_model.shadow.structure;

import io.determann.shadow.api.C;
import io.determann.shadow.api.lang_model.LM;
import io.determann.shadow.api.lang_model.adapter.Adapters;
import io.determann.shadow.api.query.Implementation;
import io.determann.shadow.api.query.Provider;

import javax.lang.model.type.TypeMirror;
import java.util.List;
import java.util.Objects;

import static io.determann.shadow.api.query.Operations.RESULT_GET_TYPE;

public class ResultImpl
      implements LM.Result
{
   private final LM.Context context;
   private final TypeMirror typeMirror;

   public ResultImpl(LM.Context context, TypeMirror typeMirror)
   {
      this.context = context;
      this.typeMirror = typeMirror;
   }

   @Override
   public List<LM.AnnotationUsage> getAnnotationUsages()
   {
      return getDirectAnnotationUsages();
   }

   @Override
   public List<LM.AnnotationUsage> getDirectAnnotationUsages()
   {
      return Adapters.adapt(context, getTypeMirror().getAnnotationMirrors());
   }

   @Override
   public LM.Type getType()
   {
      return Adapters.adapt(context, getTypeMirror());
   }

   public TypeMirror getTypeMirror()
   {
      return typeMirror;
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
      if (!(other instanceof C.Result otherReturn))
      {
         return false;
      }
      return Provider.requestOrEmpty(otherReturn, RESULT_GET_TYPE).map(type -> Objects.equals(type, getType())).orElse(false);
   }

   @Override
   public Implementation getImplementation()
   {
      return context.getImplementation();
   }
}
