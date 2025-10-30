package io.determann.shadow.internal.reflection.shadow.type;

import io.determann.shadow.api.C;
import io.determann.shadow.api.reflection.Adapter;
import io.determann.shadow.api.reflection.R;
import io.determann.shadow.implementation.support.api.shadow.type.RecordSupport;

import java.util.Arrays;
import java.util.List;

public class RecordImpl extends DeclaredImpl implements R.Record
{
   private final List<R.Type> genericTypes;

   public RecordImpl(Class<?> aClass, List<R.Type> genericTypes)
   {
      super(aClass);
      this.genericTypes = genericTypes;
   }

   @Override
   public List<R.RecordComponent> getRecordComponents()
   {
      return Arrays.stream(getaClass().getRecordComponents())
                   .map(Adapter::generalize)
                   .toList();
   }

   @Override
   public List<R.Type> getGenericUsages()
   {
      return genericTypes;
   }

   @Override
   public List<R.Generic> getGenericDeclarations()
   {
      return Arrays.stream(getaClass().getTypeParameters()).map(Adapter::generalize).map(R.Generic.class::cast).toList();
   }

   @Override
   public boolean representsSameType(C.Type type)
   {
      return type instanceof C.Record;
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
