package io.determann.shadow.internal.lang_model.shadow.type;

import io.determann.shadow.api.C;
import io.determann.shadow.api.lang_model.LM;
import io.determann.shadow.api.lang_model.adapter.Adapters;
import io.determann.shadow.api.query.Implementation;
import io.determann.shadow.internal.lang_model.ApiHolder;

import javax.lang.model.type.TypeMirror;

import static io.determann.shadow.api.lang_model.adapter.Adapters.adapt;

public abstract class TypeImpl<MIRROR extends TypeMirror> implements ApiHolder
{
   private final LM.Context context;

   private final MIRROR typeMirror;

   protected TypeImpl(LM.Context context, MIRROR typeMirror)
   {
      this.context = context;
      this.typeMirror = typeMirror;
   }

   public boolean representsSameType(C.Type type)
   {
      return Adapters.adapt(getApi()).toTypes().isSameType(getMirror(), adapt((LM.Type) type).toTypeMirror());
   }

   public MIRROR getMirror()
   {
      return typeMirror;
   }

   @Override
   public LM.Context getApi()
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
