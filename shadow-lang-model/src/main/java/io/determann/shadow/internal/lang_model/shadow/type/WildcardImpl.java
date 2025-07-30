package io.determann.shadow.internal.lang_model.shadow.type;

import io.determann.shadow.api.C;
import io.determann.shadow.api.lang_model.LM;
import io.determann.shadow.api.lang_model.adapter.Adapters;
import io.determann.shadow.implementation.support.api.shadow.type.WildcardSupport;

import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.WildcardType;
import java.util.Optional;

import static io.determann.shadow.api.lang_model.adapter.Adapters.adapt;

public class WildcardImpl extends TypeImpl<WildcardType> implements LM.Wildcard
{
   public WildcardImpl(LM.Context context, WildcardType wildcardTypeMirror)
   {
      super(context, wildcardTypeMirror);
   }

   @Override
   public Optional<LM.Type> getExtends()
   {
      TypeMirror extendsBound = getMirror().getExtendsBound();
      if (extendsBound == null)
      {
         return Optional.empty();
      }
      return Optional.of(Adapters.adapt(getApi(), extendsBound));
   }

   @Override
   public Optional<LM.Type> getSuper()
   {
      TypeMirror superBound = getMirror().getSuperBound();
      if (superBound == null)
      {
         return Optional.empty();
      }
      return Optional.of(Adapters.adapt(getApi(), superBound));
   }

   @Override
   public boolean contains(C.Type type)
   {
      return adapt(getApi()).toTypes().contains(getMirror(), adapt((LM.Declared) type).toDeclaredType());
   }

   @Override
   public LM.Wildcard erasure()
   {
      return (LM.Wildcard) Adapters.adapt(getApi(), adapt(getApi()).toTypes().erasure(getMirror()));
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
