package io.determann.shadow.internal.annotation_processing.shadow.type;

import io.determann.shadow.api.C;
import io.determann.shadow.api.annotation_processing.Ap;
import io.determann.shadow.api.annotation_processing.adapter.Adapters;

import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.WildcardType;
import java.util.Objects;
import java.util.Optional;

import static io.determann.shadow.api.annotation_processing.adapter.Adapters.adapt;

public class WildcardImpl extends TypeImpl<WildcardType> implements Ap.Wildcard
{
   public WildcardImpl(Ap.Context context, WildcardType wildcardTypeMirror)
   {
      super(context, wildcardTypeMirror);
   }

   @Override
   public Optional<Ap.Type> getExtends()
   {
      TypeMirror extendsBound = getMirror().getExtendsBound();
      if (extendsBound == null)
      {
         return Optional.empty();
      }
      return Optional.of(adapt(getApi(), extendsBound));
   }

   @Override
   public Optional<Ap.Type> getSuper()
   {
      TypeMirror superBound = getMirror().getSuperBound();
      if (superBound == null)
      {
         return Optional.empty();
      }
      return Optional.of(adapt(getApi(), superBound));
   }

   @Override
   public boolean contains(C.Type type)
   {
      return adapt(getApi()).toTypes().contains(getMirror(), adapt((Ap.Declared) type).toDeclaredType());
   }

   @Override
   public Ap.Wildcard erasure()
   {
      return (Ap.Wildcard) Adapters.adapt(getApi(), adapt(getApi()).toTypes().erasure(getMirror()));
   }

   @Override
   public boolean equals(Object other)
   {
      return other instanceof Ap.Wildcard wildcard &&
             Objects.equals(getExtends(), wildcard.getExtends()) &&
             Objects.equals(getSuper(), wildcard.getSuper());
   }

   @Override
   public int hashCode()
   {
      return Objects.hash(getExtends(), getSuper());
   }

   @Override
   public String toString()
   {
      return "Wildcard{" +
             "extends=" + getExtends() +
             ", super=" + getSuper() +
             '}';
   }
}
