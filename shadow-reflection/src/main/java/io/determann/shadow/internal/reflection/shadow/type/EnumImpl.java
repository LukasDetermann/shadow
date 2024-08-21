package io.determann.shadow.internal.reflection.shadow.type;

import io.determann.shadow.api.reflection.R_Adapter;
import io.determann.shadow.api.reflection.shadow.structure.R_EnumConstant;
import io.determann.shadow.api.reflection.shadow.type.R_Enum;
import io.determann.shadow.api.shadow.type.C_Shadow;
import io.determann.shadow.implementation.support.api.shadow.type.EnumSupport;

import java.util.List;

import static java.util.Arrays.stream;

public class EnumImpl extends DeclaredImpl implements R_Enum
{
   public EnumImpl(Class<?> aClass)
   {
      super(aClass);
   }

   @Override
   public List<R_EnumConstant> getEumConstants()
   {
      return stream(getaClass().getEnumConstants())
            .map(java.lang.Enum.class::cast)
            .map(R_Adapter::generalize)
            .toList();
   }

   @Override
   public boolean representsSameType(C_Shadow shadow)
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
