package io.determann.shadow.impl.shadow;

import io.determann.shadow.api.MirrorAdapter;
import io.determann.shadow.api.ShadowApi;
import io.determann.shadow.api.TypeKind;
import io.determann.shadow.api.shadow.Shadow;
import io.determann.shadow.api.shadow.Wildcard;

import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.WildcardType;
import java.util.Objects;
import java.util.Optional;

public class WildcardImpl extends ShadowImpl<WildcardType> implements Wildcard
{
   public WildcardImpl(ShadowApi shadowApi, WildcardType wildcardTypeMirror)
   {
      super(shadowApi, wildcardTypeMirror);
   }

   @Override
   public TypeKind getTypeKind()
   {
      return TypeKind.WILDCARD;
   }

   @Override
   public Optional<Shadow> getExtends()
   {
      TypeMirror extendsBound = getMirror().getExtendsBound();
      if (extendsBound == null)
      {
         return Optional.empty();
      }
      return Optional.of(MirrorAdapter.getShadow(getApi(), extendsBound));
   }

   @Override
   public Optional<Shadow> getSuper()
   {
      TypeMirror superBound = getMirror().getSuperBound();
      if (superBound == null)
      {
         return Optional.empty();
      }
      return Optional.of(MirrorAdapter.getShadow(getApi(), superBound));
   }

   @Override
   public boolean contains(Shadow shadow)
   {
      return getApi().getJdkApiContext().getProcessingEnv().getTypeUtils().contains(getMirror(), MirrorAdapter.getType(shadow));
   }

   @Override
   public Shadow erasure()
   {
      return MirrorAdapter.getShadow(getApi(), getApi().getJdkApiContext().getProcessingEnv().getTypeUtils().erasure(getMirror()));
   }

   @Override
   public int hashCode()
   {
      return Objects.hash(getExtends(),
                          getSuper());
   }

   @Override
   public boolean equals(Object other)
   {
      if (other == this)
      {
         return true;
      }
      if (other == null || !getClass().equals(other.getClass()))
      {
         return false;
      }
      WildcardImpl otherWildcard = (WildcardImpl) other;
      return Objects.equals(getExtends(), otherWildcard.getExtends()) &&
             Objects.equals(getSuper(), otherWildcard.getSuper());
   }
}
