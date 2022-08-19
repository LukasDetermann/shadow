package org.determann.shadow.impl.shadow;

import org.determann.shadow.api.ShadowApi;
import org.determann.shadow.api.TypeKind;
import org.determann.shadow.api.shadow.Shadow;

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
      return getApi().getJdkApiContext().types().isSameType(getMirror(), shadow.getMirror());
   }

   @Override
   public Shadow<TypeMirror> erasure()
   {
      return getApi().getShadowFactory().shadowFromType(getApi().getJdkApiContext().types().erasure(getMirror()));
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
