package io.determann.shadow.internal.reflection.shadow.type;

import io.determann.shadow.api.Implementation;
import io.determann.shadow.api.Provider;
import io.determann.shadow.api.reflection.R_Adapter;
import io.determann.shadow.api.reflection.shadow.type.R_Type;
import io.determann.shadow.api.reflection.shadow.type.R_Wildcard;
import io.determann.shadow.api.shadow.type.C_Declared;
import io.determann.shadow.api.shadow.type.C_Type;
import io.determann.shadow.api.shadow.type.C_Wildcard;
import io.determann.shadow.implementation.support.api.shadow.type.WildcardSupport;

import java.lang.reflect.WildcardType;
import java.util.Optional;

import static io.determann.shadow.api.Operations.*;
import static io.determann.shadow.api.Provider.requestOrThrow;
import static io.determann.shadow.api.reflection.R_Adapter.IMPLEMENTATION;


public class WildcardImpl implements R_Wildcard
{
   private final WildcardType wildcardType;

   public WildcardImpl(WildcardType wildcardType)
   {
      this.wildcardType = wildcardType;
   }

   @Override
   public boolean representsSameType(C_Type type)
   {
      if (!(type instanceof C_Wildcard wildcard))
      {
         return false;
      }
      Optional<C_Type> otherExtends = Provider.requestOrEmpty(wildcard, WILDCARD_GET_EXTENDS);
      Optional<C_Type> otherSuper = Provider.requestOrEmpty(wildcard, WILDCARD_GET_SUPER);


      if ((getExtends().isEmpty() && getSuper().isEmpty()) || (otherExtends.isEmpty() && otherSuper.isEmpty()))
      {
         return false;
      }
      return (getExtends().isPresent() && otherExtends.isPresent() && requestOrThrow(getExtends().get(),
                                                                                     TYPE_REPRESENTS_SAME_TYPE, otherExtends.get())) ||
             (getSuper().isPresent() && otherSuper.isPresent() && requestOrThrow(getSuper().get(), TYPE_REPRESENTS_SAME_TYPE, otherSuper.get()));
   }

   @Override
   public Optional<R_Type> getExtends()
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
         case 1 -> Optional.of(R_Adapter.generalize(upperBounds[0]));
         default -> Optional.of(new IntersectionImpl(upperBounds));
      };
   }

   @Override
   public Optional<R_Type> getSuper()
   {
      java.lang.reflect.Type[] lowerBounds = wildcardType.getLowerBounds();
      return switch (lowerBounds.length)
      {
         case 0 -> Optional.empty();
         case 1 -> Optional.of(R_Adapter.generalize(lowerBounds[0]));
         default -> Optional.of(new IntersectionImpl(lowerBounds));
      };
   }

   @Override
   public boolean contains(C_Type type)
   {
      return equals(type) ||
             (getExtends().isPresent() && (getExtends().get().equals(type) || isSubType(getExtends().get(), type))) ||
             (getSuper().isPresent() && (getSuper().get().equals(type) || isSubType(type, getSuper().get())));
   }

   private boolean isSubType(C_Type type, C_Type other)
   {
      return type instanceof C_Declared declared && requestOrThrow(declared, DECLARED_IS_SUBTYPE_OF, other);
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
   public Implementation getImplementation()
   {
      return IMPLEMENTATION;
   }
}
