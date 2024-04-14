package io.determann.shadow.internal.reflection.shadow;

import io.determann.shadow.api.TypeKind;
import io.determann.shadow.api.reflection.ReflectionAdapter;
import io.determann.shadow.api.shadow.Shadow;
import io.determann.shadow.api.shadow.Wildcard;

import java.lang.reflect.WildcardType;
import java.util.Objects;
import java.util.Optional;

import static io.determann.shadow.api.converter.Converter.convert;


public class WildcardImpl implements Wildcard
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

      if ((getExtends().isEmpty() && getSuper().isEmpty()) || (wildcard.getExtends().isEmpty() && wildcard.getSuper().isEmpty()))
      {
         return false;
      }
      return (getExtends().isPresent() && wildcard.getExtends().isPresent() && getExtends().get().representsSameType(wildcard.getExtends().get())) ||
             (getSuper().isPresent() && wildcard.getSuper().isPresent() && getSuper().get().representsSameType(wildcard.getSuper().get()));
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
      return convert(shadow).toDeclared().map(declared -> declared.isSubtypeOf(other)).orElse(false);
   }

   public WildcardType getWildcardType()
   {
      return wildcardType;
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
      if (!(other instanceof Wildcard otherWildcard))
      {
         return false;
      }
      return Objects.equals(getExtends(), otherWildcard.getExtends()) &&
             Objects.equals(getSuper(), otherWildcard.getSuper());
   }

   public WildcardType getReflection()
   {
      return wildcardType;
   }
}
