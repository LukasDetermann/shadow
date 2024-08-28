package io.determann.shadow.internal.reflection.shadow.type;

import io.determann.shadow.api.reflection.R_Adapter;
import io.determann.shadow.api.reflection.shadow.structure.R_RecordComponent;
import io.determann.shadow.api.reflection.shadow.type.R_Generic;
import io.determann.shadow.api.reflection.shadow.type.R_Record;
import io.determann.shadow.api.reflection.shadow.type.R_Type;
import io.determann.shadow.api.shadow.type.C_Record;
import io.determann.shadow.api.shadow.type.C_Type;
import io.determann.shadow.implementation.support.api.shadow.type.RecordSupport;

import java.util.Arrays;
import java.util.List;

public class RecordImpl extends DeclaredImpl implements R_Record
{
   private final List<R_Type> genericTypes;

   public RecordImpl(Class<?> aClass, List<R_Type> genericTypes)
   {
      super(aClass);
      this.genericTypes = genericTypes;
   }

   @Override
   public List<R_RecordComponent> getRecordComponents()
   {
      return Arrays.stream(getaClass().getRecordComponents())
                   .map(R_Adapter::generalize)
                   .toList();
   }

   @Override
   public List<R_Type> getGenericTypes()
   {
      return genericTypes;
   }

   @Override
   public List<R_Generic> getGenerics()
   {
      return Arrays.stream(getaClass().getTypeParameters()).map(R_Adapter::generalize).map(R_Generic.class::cast).toList();
   }

   @Override
   public boolean representsSameType(C_Type type)
   {
      return type instanceof C_Record;
   }

   @Override
   public boolean equals(Object other)
   {
      return RecordSupport.equals(this, other);
   }

   @Override
   public int hashCode()
   {
      return RecordSupport.hashCode(this);
   }

   @Override
   public String toString()
   {
      return RecordSupport.toString(this);
   }
}
