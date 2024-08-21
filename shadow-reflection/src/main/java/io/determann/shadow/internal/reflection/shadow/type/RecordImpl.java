package io.determann.shadow.internal.reflection.shadow.type;

import io.determann.shadow.api.reflection.ReflectionAdapter;
import io.determann.shadow.api.reflection.shadow.structure.RecordComponentReflection;
import io.determann.shadow.api.reflection.shadow.type.GenericReflection;
import io.determann.shadow.api.reflection.shadow.type.RecordReflection;
import io.determann.shadow.api.reflection.shadow.type.ShadowReflection;
import io.determann.shadow.api.shadow.type.Shadow;
import io.determann.shadow.implementation.support.api.shadow.type.RecordSupport;

import java.util.Arrays;
import java.util.List;

import static io.determann.shadow.api.Operations.SHADOW_GET_KIND;
import static io.determann.shadow.api.Provider.requestOrThrow;

public class RecordImpl extends DeclaredImpl implements RecordReflection
{
   private final List<ShadowReflection> genericShadows;

   public RecordImpl(Class<?> aClass, List<ShadowReflection> genericShadows)
   {
      super(aClass);
      this.genericShadows = genericShadows;
   }

   @Override
   public List<RecordComponentReflection> getRecordComponents()
   {
      return Arrays.stream(getaClass().getRecordComponents())
                   .map(ReflectionAdapter::generalize)
                   .toList();
   }

   @Override
   public List<ShadowReflection> getGenericTypes()
   {
      return genericShadows;
   }

   @Override
   public List<GenericReflection> getGenerics()
   {
      return Arrays.stream(getaClass().getTypeParameters()).map(ReflectionAdapter::generalize).map(GenericReflection.class::cast).toList();
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
