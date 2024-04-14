package io.determann.shadow.internal.lang_model.shadow;

import io.determann.shadow.api.lang_model.LangModelAdapter;
import io.determann.shadow.api.lang_model.LangModelContext;
import io.determann.shadow.api.shadow.Shadow;
import io.determann.shadow.internal.lang_model.ApiHolder;

import javax.lang.model.type.TypeMirror;

public abstract class ShadowImpl<MIRROR extends TypeMirror> implements Shadow,
                                                                       ApiHolder
{
   private final LangModelContext context;

   private final MIRROR typeMirror;

   protected ShadowImpl(LangModelContext context, MIRROR typeMirror)
   {
      this.context = context;
      this.typeMirror = typeMirror;
   }

   @Override
   public boolean representsSameType(Shadow shadow)
   {
      return LangModelAdapter.getTypes(getApi()).isSameType(getMirror(), LangModelAdapter.particularType(shadow));
   }

   public MIRROR getMirror()
   {
      return typeMirror;
   }

   @Override
   public LangModelContext getApi()
   {
      return context;
   }

   @Override
   public String toString()
   {
      return getMirror().toString();
   }
}
