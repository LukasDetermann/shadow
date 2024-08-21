package io.determann.shadow.internal.reflection.shadow.type;

import io.determann.shadow.api.reflection.R_Adapter;
import io.determann.shadow.api.reflection.shadow.structure.R_RecordComponent;
import io.determann.shadow.api.reflection.shadow.type.R_Generic;
import io.determann.shadow.api.reflection.shadow.type.R_Record;
import io.determann.shadow.api.reflection.shadow.type.R_Shadow;
import io.determann.shadow.api.shadow.type.C_Shadow;
import io.determann.shadow.implementation.support.api.shadow.type.RecordSupport;

import java.util.Arrays;
import java.util.List;

import static io.determann.shadow.api.Operations.SHADOW_GET_KIND;
import static io.determann.shadow.api.Provider.requestOrThrow;

public class RecordImpl extends DeclaredImpl implements R_Record
{
   private final List<R_Shadow> genericShadows;

   public RecordImpl(Class<?> aClass, List<R_Shadow> genericShadows)
   {
      super(aClass);
      this.genericShadows = genericShadows;
   }

   @Override
   public List<R_RecordComponent> getRecordComponents()
   {
      return Arrays.stream(getaClass().getRecordComponents())
                   .map(R_Adapter::generalize)
                   .toList();
   }

   @Override
   public List<R_Shadow> getGenericTypes()
   {
      return genericShadows;
   }

   @Override
   public List<R_Generic> getGenerics()
   {
      return Arrays.stream(getaClass().getTypeParameters()).map(R_Adapter::generalize).map(R_Generic.class::cast).toList();
   }

   @Override
   public boolean representsSameType(C_Shadow shadow)
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
