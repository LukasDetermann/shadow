package io.determann.shadow.impl.reflection.shadow;

import io.determann.shadow.api.reflection.ReflectionAdapter;
import io.determann.shadow.api.shadow.Generic;
import io.determann.shadow.api.shadow.Record;
import io.determann.shadow.api.shadow.RecordComponent;
import io.determann.shadow.api.shadow.Shadow;

import java.util.Arrays;
import java.util.List;

public class RecordImpl extends DeclaredImpl implements Record
{
   private final List<Shadow> genericTypes;

   public RecordImpl(Class<?> aClass, List<Shadow> genericTypes)
   {
      super(aClass);
      this.genericTypes = genericTypes;
   }

   @Override
   public List<RecordComponent> getRecordComponents()
   {
      return Arrays.stream(getaClass().getRecordComponents())
                   .map(ReflectionAdapter::getShadow)
                   .toList();
   }

   @Override
   public List<Shadow> getGenericTypes()
   {
      return genericTypes;
   }

   @Override
   public List<Generic> getGenerics()
   {
      return Arrays.stream(getaClass().getTypeParameters()).map(ReflectionAdapter::getShadow).map(Generic.class::cast).toList();
   }

   @Override
   public boolean representsSameType(Shadow shadow)
   {
      return shadow != null && getTypeKind().equals(shadow.getTypeKind());
   }
}
