package io.determann.shadow.internal.lang_model.shadow.type;

import io.determann.shadow.api.lang_model.LM_Context;
import io.determann.shadow.api.lang_model.adapter.LM_Adapters;
import io.determann.shadow.api.lang_model.shadow.type.LM_Declared;
import io.determann.shadow.api.lang_model.shadow.type.LM_Type;
import io.determann.shadow.api.lang_model.shadow.type.LM_Wildcard;
import io.determann.shadow.api.shadow.type.C_Type;
import io.determann.shadow.implementation.support.api.shadow.type.WildcardSupport;

import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.WildcardType;
import java.util.Optional;

import static io.determann.shadow.api.lang_model.adapter.LM_Adapters.adapt;

public class WildcardImpl extends TypeImpl<WildcardType> implements LM_Wildcard
{
   public WildcardImpl(LM_Context context, WildcardType wildcardTypeMirror)
   {
      super(context, wildcardTypeMirror);
   }

   @Override
   public Optional<LM_Type> getExtends()
   {
      TypeMirror extendsBound = getMirror().getExtendsBound();
      if (extendsBound == null)
      {
         return Optional.empty();
      }
      return Optional.of(LM_Adapters.adapt(getApi(), extendsBound));
   }

   @Override
   public Optional<LM_Type> getSuper()
   {
      TypeMirror superBound = getMirror().getSuperBound();
      if (superBound == null)
      {
         return Optional.empty();
      }
      return Optional.of(LM_Adapters.adapt(getApi(), superBound));
   }

   @Override
   public boolean contains(C_Type type)
   {
      return adapt(getApi()).toTypes().contains(getMirror(), adapt((LM_Declared) type).toDeclaredType());
   }

   @Override
   public LM_Wildcard erasure()
   {
      return (LM_Wildcard) LM_Adapters.adapt(getApi(), adapt(getApi()).toTypes().erasure(getMirror()));
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
