package io.determann.shadow.internal.reflection.shadow.type;

import io.determann.shadow.api.reflection.ReflectionAdapter;
import io.determann.shadow.api.reflection.shadow.type.EnumReflection;
import io.determann.shadow.api.shadow.structure.EnumConstant;
import io.determann.shadow.api.shadow.type.Shadow;
import io.determann.shadow.implementation.support.api.shadow.type.EnumSupport;

import java.util.List;

import static java.util.Arrays.stream;

public class EnumImpl extends DeclaredImpl implements EnumReflection
{
   public EnumImpl(Class<?> aClass)
   {
      super(aClass);
   }

   @Override
   public List<EnumConstant> getEumConstants()
   {
      return stream(getaClass().getEnumConstants())
            .map(java.lang.Enum.class::cast)
            .map(ReflectionAdapter::generalize)
            .toList();
   }

   @Override
   public boolean representsSameType(Shadow shadow)
   {
      return EnumSupport.representsSameType(this, shadow);
   }

   @Override
   public boolean equals(Object other)
   {
      return EnumSupport.equals(this, other);
   }

   @Override
   public int hashCode()
   {
      return EnumSupport.hashCode(this);
   }

   @Override
   public String toString()
   {
      return EnumSupport.toString(this);
   }
}
