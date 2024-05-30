package io.determann.shadow.internal.reflection.shadow;

import io.determann.shadow.api.reflection.ReflectionAdapter;
import io.determann.shadow.api.shadow.Generic;
import io.determann.shadow.api.shadow.Record;
import io.determann.shadow.api.shadow.RecordComponent;
import io.determann.shadow.api.shadow.Shadow;

import java.util.Arrays;
import java.util.List;

import static io.determann.shadow.meta_meta.Operations.SHADOW_GET_KIND;
import static io.determann.shadow.meta_meta.Provider.requestOrThrow;

public class RecordImpl extends DeclaredImpl implements Record
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
}
