package io.determann.shadow.impl.reflection.shadow;

import io.determann.shadow.api.TypeKind;
import io.determann.shadow.api.converter.Converter;
import io.determann.shadow.api.reflection.ReflectionAdapter;
import io.determann.shadow.api.shadow.AnnotationUsage;
import io.determann.shadow.api.shadow.Generic;
import io.determann.shadow.api.shadow.Package;
import io.determann.shadow.api.shadow.Shadow;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.*;

import static io.determann.shadow.api.converter.Converter.convert;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

public class GenericImpl implements Generic
{
   private final TypeVariable<?> typeVariable;

   public GenericImpl(TypeVariable<?> typeVariable)
   {
      this.typeVariable = typeVariable;
   }

   @Override
   public String getName()
   {
      return getTypeVariable().getName();
   }

   @Override
   public List<AnnotationUsage> getAnnotationUsages()
   {
      return Arrays.stream(getTypeVariable().getAnnotations())
                   .map(ReflectionAdapter::getAnnotationUsage)
                   .collect(collectingAndThen(toList(), Collections::unmodifiableList));
   }

   @Override
   public List<AnnotationUsage> getDirectAnnotationUsages()
   {
      return Arrays.stream(getTypeVariable().getDeclaredAnnotations())
                   .map(ReflectionAdapter::getAnnotationUsage)
                   .collect(collectingAndThen(toList(), Collections::unmodifiableList));
   }

   @Override
   public Shadow getExtends()
   {
      Type[] bounds = getTypeVariable().getBounds();
      if (bounds.length == 1)
      {
         return ReflectionAdapter.getShadow(bounds[0]);
      }
      return new IntersectionImpl(bounds);
   }

   @Override
   public Optional<Shadow> getSuper()
   {
      return Optional.empty();
   }

   @Override
   public Shadow getEnclosing()
   {
      return ReflectionAdapter.getShadow(getTypeVariable().getGenericDeclaration());
   }

   @Override
   public Package getPackage()
   {
      switch (getEnclosing().getTypeKind())
      {
         case CLASS:
         case INTERFACE:
         case ENUM:
         case ANNOTATION:
            return convert(getEnclosing()).toDeclaredOrThrow().getPackage();
         case METHOD:
         case CONSTRUCTOR:
            return convert(getEnclosing()).toExecutableOrThrow().getPackage();
         case ENUM_CONSTANT:
         case FIELD:
         case PARAMETER:
            return convert(getEnclosing()).toVariableOrThrow().getPackage();
         case GENERIC:
            return convert(getEnclosing()).toGenericOrThrow().getPackage();
         default:
            throw new IllegalStateException();
      }
   }

   @Override
   public TypeKind getTypeKind()
   {
      return TypeKind.GENERIC;
   }

   @Override
   public boolean representsSameType(Shadow shadow)
   {
      return shadow != null &&
             Converter.convert(shadow)
                      .toGeneric()
                      .map(generic -> generic.getExtends().representsSameType(getExtends()) &&
                                      ((!generic.getSuper().isPresent() && !getSuper().isPresent()) ||
                                       (generic.getSuper().isPresent() && getSuper().isPresent()) &&
                                       generic.getSuper().get().representsSameType(getSuper().get())))
                      .orElse(false);
   }

   public TypeVariable<?> getTypeVariable()
   {
      return typeVariable;
   }

   @Override
   public int hashCode()
   {
      return Objects.hash(getName(),
                          getExtends(),
                          getSuper());
   }

   @Override
   public boolean equals(Object other)
   {
      if (other == this)
      {
         return true;
      }
      if (!(other instanceof Generic))
      {
         return false;
      }
      Generic otherGeneric = ((Generic) other);
      return Objects.equals(getName(), otherGeneric.getName()) &&
             Objects.equals(getExtends(), otherGeneric.getExtends()) &&
             Objects.equals(getSuper(), otherGeneric.getSuper());
   }

   public TypeVariable<?> getReflection()
   {
      return getTypeVariable();
   }
}