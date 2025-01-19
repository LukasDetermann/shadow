package io.determann.shadow.internal.lang_model.shadow.type;

import io.determann.shadow.api.Implementation;
import io.determann.shadow.api.lang_model.LM_Context;
import io.determann.shadow.api.lang_model.adapter.LM_Adapters;
import io.determann.shadow.api.lang_model.shadow.type.LM_Type;
import io.determann.shadow.api.shadow.type.C_Type;
import io.determann.shadow.internal.lang_model.ApiHolder;

import javax.lang.model.type.TypeMirror;

import static io.determann.shadow.api.lang_model.adapter.LM_Adapters.adapt;

public abstract class TypeImpl<MIRROR extends TypeMirror> implements ApiHolder
{
   private final LM_Context context;

   private final MIRROR typeMirror;

   protected TypeImpl(LM_Context context, MIRROR typeMirror)
   {
      this.context = context;
      this.typeMirror = typeMirror;
   }

   public boolean representsSameType(C_Type type)
   {
      return LM_Adapters.adapt(getApi()).toTypes().isSameType(getMirror(), adapt((LM_Type) type).toTypeMirror());
   }

   public MIRROR getMirror()
   {
      return typeMirror;
   }

   @Override
   public LM_Context getApi()
   {
      return context;
   }

   @Override
   public String toString()
   {
      return getMirror().toString();
   }

   public Implementation getImplementation()
   {
      return getApi().getImplementation();
   }
}
