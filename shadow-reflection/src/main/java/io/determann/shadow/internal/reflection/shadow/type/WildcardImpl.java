package io.determann.shadow.internal.reflection.shadow.type;

import io.determann.shadow.api.reflection.ReflectionAdapter;
import io.determann.shadow.api.reflection.shadow.type.ShadowReflection;
import io.determann.shadow.api.reflection.shadow.type.WildcardReflection;
import io.determann.shadow.api.shadow.Provider;
import io.determann.shadow.api.shadow.TypeKind;
import io.determann.shadow.api.shadow.type.Shadow;
import io.determann.shadow.api.shadow.type.Wildcard;
import io.determann.shadow.implementation.support.api.shadow.type.WildcardSupport;

import java.lang.reflect.WildcardType;
import java.util.Optional;

import static io.determann.shadow.api.converter.Converter.convert;
import static io.determann.shadow.api.shadow.Operations.*;
import static io.determann.shadow.api.shadow.Provider.requestOrThrow;
import static io.determann.shadow.internal.reflection.ReflectionProvider.IMPLEMENTATION_NAME;


public class WildcardImpl implements Wildcard,
                                     WildcardReflection,
                                     ShadowReflection
{
   private final WildcardType wildcardType;

   public WildcardImpl(WildcardType wildcardType)
   {
      this.wildcardType = wildcardType;
   }

   @Override
   public TypeKind getKind()
   {
      return TypeKind.WILDCARD;
   }

   @Override
   public boolean representsSameType(Shadow shadow)
   {
      Optional<Wildcard> possibleWildcard = convert(shadow).toWildcard();
      if (possibleWildcard.isEmpty())
      {
         return false;
      }
      Wildcard wildcard = possibleWildcard.get();
      Optional<Shadow> otherExtends = Provider.requestOrEmpty(wildcard, WILDCARD_GET_EXTENDS);
      Optional<Shadow> otherSuper = Provider.requestOrEmpty(wildcard, WILDCARD_GET_SUPER);


      if ((getExtends().isEmpty() && getSuper().isEmpty()) || (otherExtends.isEmpty() && otherSuper.isEmpty()))
      {
         return false;
      }
      return (getExtends().isPresent() && otherExtends.isPresent() && requestOrThrow(getExtends().get(), SHADOW_REPRESENTS_SAME_TYPE, otherExtends.get())) ||
             (getSuper().isPresent() && otherSuper.isPresent() && requestOrThrow(getSuper().get(), SHADOW_REPRESENTS_SAME_TYPE, otherSuper.get()));
   }

   @Override
   public Optional<Shadow> getExtends()
   {
      java.lang.reflect.Type[] upperBounds = wildcardType.getUpperBounds();
      //? extends Object -> ? : drop the extends Object
      //the upperBounds are never empty. the lower bounds can be. if the upper bound is only Object. ignore it
      if (upperBounds.length == 1 && upperBounds[0].equals(Object.class))
      {
         return Optional.empty();
      }
      return switch (upperBounds.length)
      {
         case 0 -> Optional.empty();
         case 1 -> Optional.of(ReflectionAdapter.generalize(upperBounds[0]));
         default -> Optional.of(new IntersectionImpl(upperBounds));
      };
   }

   @Override
   public Optional<Shadow> getSuper()
   {
      java.lang.reflect.Type[] lowerBounds = wildcardType.getLowerBounds();
      return switch (lowerBounds.length)
      {
         case 0 -> Optional.empty();
         case 1 -> Optional.of(ReflectionAdapter.generalize(lowerBounds[0]));
         default -> Optional.of(new IntersectionImpl(lowerBounds));
      };
   }

   @Override
   public boolean contains(Shadow shadow)
   {
      return equals(shadow) ||
             (getExtends().isPresent() && (getExtends().get().equals(shadow) || isSubType(getExtends().get(), shadow))) ||
             (getSuper().isPresent() && (getSuper().get().equals(shadow) || isSubType(shadow, getSuper().get())));
   }

   private boolean isSubType(Shadow shadow, Shadow other)
   {
      return convert(shadow).toDeclared().map(declared -> requestOrThrow(declared, DECLARED_IS_SUBTYPE_OF, other)).orElse(false);
   }

   public WildcardType getWildcardType()
   {
      return wildcardType;
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

   public WildcardType getReflection()
   {
      return wildcardType;
   }

   @Override
   public String getImplementationName()
   {
      return IMPLEMENTATION_NAME;
   }
}
