package io.determann.shadow.internal.reflection.shadow.structure;

import io.determann.shadow.api.C;
import io.determann.shadow.api.query.Implementation;
import io.determann.shadow.api.query.Provider;
import io.determann.shadow.api.reflection.Adapter;
import io.determann.shadow.api.reflection.R;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static io.determann.shadow.api.query.Operations.*;
import static io.determann.shadow.api.query.Provider.requestOrThrow;
import static io.determann.shadow.api.reflection.Adapter.IMPLEMENTATION;

public class RecordComponentImpl implements R.RecordComponent
{
   private final java.lang.reflect.RecordComponent recordComponent;

   public RecordComponentImpl(java.lang.reflect.RecordComponent recordComponent)
   {
      this.recordComponent = recordComponent;
   }

   @Override
   public R.Module getModule()
   {
      return getRecord().getModule();
   }

   @Override
   public String getName()
   {
      return getRecordComponent().getName();
   }

   @Override
   public List<R.AnnotationUsage> getAnnotationUsages()
   {
      return Arrays.stream(getRecordComponent().getAnnotations()).map(Adapter::generalize).toList();
   }

   @Override
   public List<R.AnnotationUsage> getDirectAnnotationUsages()
   {
      return Arrays.stream(getRecordComponent().getDeclaredAnnotations()).map(Adapter::generalize).toList();
   }

   @Override
   public boolean isSubtypeOf(C.Type type)
   {
      if (getType() instanceof C.Primitive primitive)
      {
         return requestOrThrow(primitive, PRIMITIVE_IS_SUBTYPE_OF, type);
      }
      if (getType() instanceof C.Class aClass)
      {
         return requestOrThrow(aClass, DECLARED_IS_SUBTYPE_OF, type);
      }
      if (getType() instanceof C.Array array)
      {
         return requestOrThrow(array, ARRAY_IS_SUBTYPE_OF, type);
      }
      return false;
   }

   @Override
   public boolean isAssignableFrom(C.Type type)
   {
      if (getType() instanceof C.Primitive primitive)
      {
         return requestOrThrow(primitive, PRIMITIVE_IS_ASSIGNABLE_FROM, type);
      }
      if (getType() instanceof C.Class aClass)
      {
         return requestOrThrow(aClass, CLASS_IS_ASSIGNABLE_FROM, type);
      }
      return false;
   }

   @Override
   public R.Record getRecord()
   {
      return Adapter.generalize(getRecordComponent().getDeclaringRecord());
   }

   @Override
   public R.Type getType()
   {
      return Adapter.generalize(getRecordComponent().getType());
   }

   @Override
   public R.Method getGetter()
   {
      return Adapter.generalize(getRecordComponent().getAccessor());
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
      if (!(other instanceof C.RecordComponent otherRecordComponent))
      {
         return false;
      }
      return Provider.requestOrEmpty(otherRecordComponent, NAMEABLE_GET_NAME).map(name -> Objects.equals(getName(), name)).orElse(false) &&
             Provider.requestOrEmpty(otherRecordComponent, RECORD_COMPONENT_GET_TYPE).map(type -> Objects.equals(type, getType())).orElse(false);
   }

   public java.lang.reflect.RecordComponent getReflection()
   {
      return recordComponent;
   }


   @Override
   public Implementation getImplementation()
   {
      return IMPLEMENTATION;
   }
}
