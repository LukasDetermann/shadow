package io.determann.shadow.internal.reflection.shadow.structure;

import io.determann.shadow.api.reflection.ReflectionAdapter;
import io.determann.shadow.api.reflection.shadow.AnnotationUsageReflection;
import io.determann.shadow.api.reflection.shadow.structure.MethodReflection;
import io.determann.shadow.api.reflection.shadow.structure.ModuleReflection;
import io.determann.shadow.api.reflection.shadow.structure.PackageReflection;
import io.determann.shadow.api.reflection.shadow.structure.RecordComponentReflection;
import io.determann.shadow.api.reflection.shadow.type.RecordReflection;
import io.determann.shadow.api.reflection.shadow.type.ShadowReflection;
import io.determann.shadow.api.shadow.Provider;
import io.determann.shadow.api.shadow.structure.RecordComponent;
import io.determann.shadow.api.shadow.type.Array;
import io.determann.shadow.api.shadow.type.Class;
import io.determann.shadow.api.shadow.type.Primitive;
import io.determann.shadow.api.shadow.type.Shadow;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static io.determann.shadow.api.shadow.Operations.*;
import static io.determann.shadow.api.shadow.Provider.requestOrThrow;
import static io.determann.shadow.internal.reflection.ReflectionProvider.IMPLEMENTATION_NAME;

public class RecordComponentImpl implements RecordComponentReflection
{
   private final java.lang.reflect.RecordComponent recordComponent;

   public RecordComponentImpl(java.lang.reflect.RecordComponent recordComponent)
   {
      this.recordComponent = recordComponent;
   }

   @Override
   public ModuleReflection getModule()
   {
      return getRecord().getModule();
   }

   @Override
   public String getName()
   {
      return getRecordComponent().getName();
   }

   @Override
   public List<AnnotationUsageReflection> getAnnotationUsages()
   {
      return Arrays.stream(getRecordComponent().getAnnotations()).map(ReflectionAdapter::generalize).toList();
   }

   @Override
   public List<AnnotationUsageReflection> getDirectAnnotationUsages()
   {
      return Arrays.stream(getRecordComponent().getDeclaredAnnotations()).map(ReflectionAdapter::generalize).toList();
   }

   @Override
   public boolean isSubtypeOf(Shadow shadow)
   {
      if (getType() instanceof Primitive primitive)
      {
         return requestOrThrow(primitive, PRIMITIVE_IS_SUBTYPE_OF, shadow);
      }
      if (getType() instanceof Class aClass)
      {
         return requestOrThrow(aClass, DECLARED_IS_SUBTYPE_OF, shadow);
      }
      if (getType() instanceof Array array)
      {
         return requestOrThrow(array, ARRAY_IS_SUBTYPE_OF, shadow);
      }
      return false;
   }

   @Override
   public boolean isAssignableFrom(Shadow shadow)
   {
      if (getType() instanceof Primitive primitive)
      {
         return requestOrThrow(primitive, PRIMITIVE_IS_ASSIGNABLE_FROM, shadow);
      }
      if (getType() instanceof Class aClass)
      {
         return requestOrThrow(aClass, CLASS_IS_ASSIGNABLE_FROM, shadow);
      }
      return false;
   }

   @Override
   public RecordReflection getRecord()
   {
      return ReflectionAdapter.generalize(getRecordComponent().getDeclaringRecord());
   }

   @Override
   public ShadowReflection getType()
   {
      return ReflectionAdapter.generalize(getRecordComponent().getType());
   }

   @Override
   public MethodReflection getGetter()
   {
      return ReflectionAdapter.generalize(getRecordComponent().getAccessor());
   }

   @Override
   public PackageReflection getPackage()
   {
      return getRecord().getPackage();
   }

   public java.lang.reflect.RecordComponent getRecordComponent()
   {
      return recordComponent;
   }

   @Override
   public int hashCode()
   {
      return Objects.hash(getName(), getType());
   }

   @Override
   public boolean equals(Object other)
   {
      if (other == this)
      {
         return true;
      }
      if (!(other instanceof RecordComponent otherRecordComponent))
      {
         return false;
      }
      return Provider.requestOrEmpty(otherRecordComponent, NAMEABLE_GET_NAME).map(name -> Objects.equals(getName(), name)).orElse(false) &&
             Provider.requestOrEmpty(otherRecordComponent, RECORD_COMPONENT_GET_TYPE).map(shadow -> Objects.equals(shadow, getType())).orElse(false);
   }

   public java.lang.reflect.RecordComponent getReflection()
   {
      return recordComponent;
   }


   @Override
   public String getImplementationName()
   {
      return IMPLEMENTATION_NAME;
   }
}
