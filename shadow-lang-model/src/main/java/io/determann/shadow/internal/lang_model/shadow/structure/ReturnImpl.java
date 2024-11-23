package io.determann.shadow.internal.lang_model.shadow.structure;

import io.determann.shadow.api.Implementation;
import io.determann.shadow.api.Provider;
import io.determann.shadow.api.lang_model.LM_Adapter;
import io.determann.shadow.api.lang_model.LM_Context;
import io.determann.shadow.api.lang_model.shadow.LM_AnnotationUsage;
import io.determann.shadow.api.lang_model.shadow.structure.LM_Return;
import io.determann.shadow.api.lang_model.shadow.type.LM_Type;
import io.determann.shadow.api.shadow.structure.C_Return;

import javax.lang.model.type.TypeMirror;
import java.util.List;
import java.util.Objects;

import static io.determann.shadow.api.Operations.RETURN_GET_TYPE;

public class ReturnImpl implements LM_Return
{
   private final LM_Context context;
   private final TypeMirror typeMirror;

   public ReturnImpl(LM_Context context, TypeMirror typeMirror)
   {
      this.context = context;
      this.typeMirror = typeMirror;
   }

   @Override
   public List<LM_AnnotationUsage> getAnnotationUsages()
   {
      return getDirectAnnotationUsages();
   }

   @Override
   public List<LM_AnnotationUsage> getDirectAnnotationUsages()
   {
      return LM_Adapter.generalize(context, getTypeMirror().getAnnotationMirrors());
   }

   @Override
   public LM_Type getType()
   {
      return LM_Adapter.generalize(context, getTypeMirror());
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
      if (!(other instanceof C_Return otherReturn))
      {
         return false;
      }
      return Provider.requestOrEmpty(otherReturn, RETURN_GET_TYPE).map(type -> Objects.equals(type, getType())).orElse(false);
   }

   @Override
   public Implementation getImplementation()
   {
      return context.getImplementation();
   }
}
