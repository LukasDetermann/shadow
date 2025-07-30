package io.determann.shadow.internal.reflection.shadow.type;

import io.determann.shadow.api.C;
import io.determann.shadow.api.reflection.Adapter;
import io.determann.shadow.api.reflection.R;
import io.determann.shadow.implementation.support.api.shadow.type.EnumSupport;

import java.util.List;

import static java.util.Arrays.stream;

public class EnumImpl extends DeclaredImpl implements R.Enum
{
   public EnumImpl(Class<?> aClass)
   {
      super(aClass);
   }

   @Override
   public List<R.EnumConstant> getEumConstants()
   {
      return stream(getaClass().getEnumConstants())
            .map(java.lang.Enum.class::cast)
            .map(Adapter::generalize)
            .toList();
   }

   @Override
   public boolean representsSameType(C.Type type)
   {
      return EnumSupport.representsSameType(this, type);
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
