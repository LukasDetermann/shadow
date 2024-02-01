package io.determann.shadow.impl.lang_model.shadow;

import io.determann.shadow.api.lang_model.LangModelContext;
import io.determann.shadow.api.lang_model.MirrorAdapter;
import io.determann.shadow.api.shadow.Shadow;
import io.determann.shadow.impl.lang_model.ApiHolder;

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
      return MirrorAdapter.getTypes(getApi()).isSameType(getMirror(), MirrorAdapter.getType(shadow));
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
