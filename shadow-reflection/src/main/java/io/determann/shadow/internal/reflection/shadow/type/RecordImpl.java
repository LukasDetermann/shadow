package io.determann.shadow.internal.reflection.shadow.type;

import io.determann.shadow.api.reflection.ReflectionAdapter;
import io.determann.shadow.api.reflection.shadow.type.RecordReflection;
import io.determann.shadow.api.shadow.structure.RecordComponent;
import io.determann.shadow.api.shadow.type.Generic;
import io.determann.shadow.api.shadow.type.Shadow;
import io.determann.shadow.implementation.support.api.shadow.type.RecordSupport;

import java.util.Arrays;
import java.util.List;

import static io.determann.shadow.api.shadow.Operations.SHADOW_GET_KIND;
import static io.determann.shadow.api.shadow.Provider.requestOrThrow;

public class RecordImpl extends DeclaredImpl implements RecordReflection
{
   private final List<Shadow> genericShadows;

   public RecordImpl(Class<?> aClass, List<Shadow> genericShadows)
   {
      super(aClass);
      this.genericShadows = genericShadows;
   }

   @Override
   public List<RecordComponent> getRecordComponents()
   {
      return Arrays.stream(getaClass().getRecordComponents())
                   .map(ReflectionAdapter::generalize)
                   .toList();
   }

   @Override
   public List<Shadow> getGenericTypes()
   {
      return genericShadows;
   }

   @Override
   public List<Generic> getGenerics()
   {
      return Arrays.stream(getaClass().getTypeParameters()).map(ReflectionAdapter::generalize).map(Generic.class::cast).toList();
   }

   @Override
   public boolean representsSameType(Shadow shadow)
   {
      return shadow != null && getKind().equals(requestOrThrow(shadow, SHADOW_GET_KIND));
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
