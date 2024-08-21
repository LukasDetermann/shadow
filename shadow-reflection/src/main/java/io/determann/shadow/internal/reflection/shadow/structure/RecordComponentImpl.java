package io.determann.shadow.internal.reflection.shadow.structure;

import io.determann.shadow.api.Provider;
import io.determann.shadow.api.reflection.R_Adapter;
import io.determann.shadow.api.reflection.shadow.R_AnnotationUsage;
import io.determann.shadow.api.reflection.shadow.structure.R_Method;
import io.determann.shadow.api.reflection.shadow.structure.R_Module;
import io.determann.shadow.api.reflection.shadow.structure.R_Package;
import io.determann.shadow.api.reflection.shadow.structure.R_RecordComponent;
import io.determann.shadow.api.reflection.shadow.type.R_Record;
import io.determann.shadow.api.reflection.shadow.type.R_Shadow;
import io.determann.shadow.api.shadow.structure.C_RecordComponent;
import io.determann.shadow.api.shadow.type.C_Array;
import io.determann.shadow.api.shadow.type.C_Class;
import io.determann.shadow.api.shadow.type.C_Primitive;
import io.determann.shadow.api.shadow.type.C_Shadow;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static io.determann.shadow.api.Operations.*;
import static io.determann.shadow.api.Provider.requestOrThrow;
import static io.determann.shadow.internal.reflection.ReflectionProvider.IMPLEMENTATION_NAME;

public class RecordComponentImpl implements R_RecordComponent
{
   private final java.lang.reflect.RecordComponent recordComponent;

   public RecordComponentImpl(java.lang.reflect.RecordComponent recordComponent)
   {
      this.recordComponent = recordComponent;
   }

   @Override
   public R_Module getModule()
   {
      return getRecord().getModule();
   }

   @Override
   public String getName()
   {
      return getRecordComponent().getName();
   }

   @Override
   public List<R_AnnotationUsage> getAnnotationUsages()
   {
      return Arrays.stream(getRecordComponent().getAnnotations()).map(R_Adapter::generalize).toList();
   }

   @Override
   public List<R_AnnotationUsage> getDirectAnnotationUsages()
   {
      return Arrays.stream(getRecordComponent().getDeclaredAnnotations()).map(R_Adapter::generalize).toList();
   }

   @Override
   public boolean isSubtypeOf(C_Shadow shadow)
   {
      if (getType() instanceof C_Primitive primitive)
      {
         return requestOrThrow(primitive, PRIMITIVE_IS_SUBTYPE_OF, shadow);
      }
      if (getType() instanceof C_Class aClass)
      {
         return requestOrThrow(aClass, DECLARED_IS_SUBTYPE_OF, shadow);
      }
      if (getType() instanceof C_Array array)
      {
         return requestOrThrow(array, ARRAY_IS_SUBTYPE_OF, shadow);
      }
      return false;
   }

   @Override
   public boolean isAssignableFrom(C_Shadow shadow)
   {
      if (getType() instanceof C_Primitive primitive)
      {
         return requestOrThrow(primitive, PRIMITIVE_IS_ASSIGNABLE_FROM, shadow);
      }
      if (getType() instanceof C_Class aClass)
      {
         return requestOrThrow(aClass, CLASS_IS_ASSIGNABLE_FROM, shadow);
      }
      return false;
   }

   @Override
   public R_Record getRecord()
   {
      return R_Adapter.generalize(getRecordComponent().getDeclaringRecord());
   }

   @Override
   public R_Shadow getType()
   {
      return R_Adapter.generalize(getRecordComponent().getType());
   }

   @Override
   public R_Method getGetter()
   {
      return R_Adapter.generalize(getRecordComponent().getAccessor());
   }

   @Override
   public R_Package getPackage()
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
      if (!(other instanceof C_RecordComponent otherRecordComponent))
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
