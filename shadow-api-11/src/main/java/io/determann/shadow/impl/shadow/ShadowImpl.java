package io.determann.shadow.impl.shadow;

import io.determann.shadow.api.ShadowApi;
import io.determann.shadow.api.TypeKind;
import io.determann.shadow.api.shadow.Shadow;

import javax.lang.model.type.TypeMirror;

public abstract class ShadowImpl<MIRROR extends TypeMirror> implements Shadow<MIRROR>
{
   private final ShadowApi shadowApi;

   private final MIRROR typeMirror;

   protected ShadowImpl(ShadowApi shadowApi, MIRROR typeMirror)
   {
      this.shadowApi = shadowApi;
      this.typeMirror = typeMirror;
   }

   @Override
   public boolean isTypeKind(TypeKind typeKind)
   {
      return getTypeKind().equals(typeKind);
   }

   @Override
   public boolean representsSameType(Shadow<? extends TypeMirror> shadow)
   {
      return getApi().getJdkApiContext().processingEnv().getTypeUtils().isSameType(getMirror(), shadow.getMirror());
   }

   @Override
   public MIRROR getMirror()
   {
      return typeMirror;
   }

   @Override
   public ShadowApi getApi()
   {
      return shadowApi;
   }

   @Override
   public String toString()
   {
      return getMirror().toString();
   }
}
