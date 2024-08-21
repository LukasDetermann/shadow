package io.determann.shadow.internal.lang_model.shadow.type;

import io.determann.shadow.api.lang_model.LM_Adapter;
import io.determann.shadow.api.lang_model.LM_Context;
import io.determann.shadow.api.lang_model.shadow.type.LM_Declared;
import io.determann.shadow.api.lang_model.shadow.type.LM_Shadow;
import io.determann.shadow.api.lang_model.shadow.type.LM_Wildcard;
import io.determann.shadow.api.shadow.C_TypeKind;
import io.determann.shadow.api.shadow.type.C_Shadow;
import io.determann.shadow.api.shadow.type.C_Wildcard;
import io.determann.shadow.implementation.support.api.shadow.type.WildcardSupport;

import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.WildcardType;
import java.util.Optional;

public class WildcardImpl extends ShadowImpl<WildcardType> implements C_Wildcard,
                                                                      LM_Wildcard
{
   public WildcardImpl(LM_Context context, WildcardType wildcardTypeMirror)
   {
      super(context, wildcardTypeMirror);
   }

   @Override
   public C_TypeKind getKind()
   {
      return C_TypeKind.WILDCARD;
   }

   @Override
   public Optional<LM_Shadow> getExtends()
   {
      TypeMirror extendsBound = getMirror().getExtendsBound();
      if (extendsBound == null)
      {
         return Optional.empty();
      }
      return Optional.of(LM_Adapter.generalize(getApi(), extendsBound));
   }

   @Override
   public Optional<LM_Shadow> getSuper()
   {
      TypeMirror superBound = getMirror().getSuperBound();
      if (superBound == null)
      {
         return Optional.empty();
      }
      return Optional.of(LM_Adapter.generalize(getApi(), superBound));
   }

   @Override
   public boolean contains(C_Shadow shadow)
   {
      return LM_Adapter.getTypes(getApi()).contains(getMirror(), LM_Adapter.particularType((LM_Declared) shadow));
   }

   @Override
   public int hashCode()
   {
      return WildcardSupport.hashCode(this);
   }

   @Override
   public boolean equals(Object other)
   {
      return WildcardSupport.equals(this, other);
   }

   @Override
   public String toString()
   {
      return WildcardSupport.toString(this);
   }
}
