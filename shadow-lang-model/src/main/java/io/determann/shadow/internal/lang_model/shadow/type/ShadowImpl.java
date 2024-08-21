package io.determann.shadow.internal.lang_model.shadow.type;

import io.determann.shadow.api.lang_model.LM_Adapter;
import io.determann.shadow.api.lang_model.LM_Context;
import io.determann.shadow.api.lang_model.shadow.type.LM_Shadow;
import io.determann.shadow.api.shadow.type.C_Shadow;
import io.determann.shadow.internal.lang_model.ApiHolder;

import javax.lang.model.type.TypeMirror;

import static io.determann.shadow.internal.lang_model.LangModelProvider.IMPLEMENTATION_NAME;

public abstract class ShadowImpl<MIRROR extends TypeMirror> implements ApiHolder,
                                                                       LM_Shadow
{
   private final LM_Context context;

   private final MIRROR typeMirror;

   protected ShadowImpl(LM_Context context, MIRROR typeMirror)
   {
      this.context = context;
      this.typeMirror = typeMirror;
   }

   @Override
   public boolean representsSameType(C_Shadow shadow)
   {
      return LM_Adapter.getTypes(getApi()).isSameType(getMirror(), LM_Adapter.particularType((LM_Shadow) shadow));
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

   @Override
   public String getImplementationName()
   {
      return IMPLEMENTATION_NAME;
   }
}
