package io.determann.shadow.internal.reflection.shadow;

import io.determann.shadow.api.TypeKind;
import io.determann.shadow.api.reflection.ReflectionAdapter;
import io.determann.shadow.api.shadow.Class;
import io.determann.shadow.api.shadow.Module;
import io.determann.shadow.api.shadow.Package;
import io.determann.shadow.api.shadow.Record;
import io.determann.shadow.api.shadow.*;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static io.determann.shadow.api.converter.Converter.convert;

public class RecordComponentImpl implements RecordComponent
{
   private final java.lang.reflect.RecordComponent recordComponent;

   public RecordComponentImpl(java.lang.reflect.RecordComponent recordComponent)
   {
      this.recordComponent = recordComponent;
   }

   @Override
   public Module getModule()
   {
      return getRecord().getModule();
   }

   @Override
   public String getName()
   {
      return getRecordComponent().getName();
   }

   @Override
   public List<AnnotationUsage> getAnnotationUsages()
   {
      return Arrays.stream(getRecordComponent().getAnnotations()).map(ReflectionAdapter::getAnnotationUsage).toList();
   }

   @Override
   public List<AnnotationUsage> getDirectAnnotationUsages()
   {
      return Arrays.stream(getRecordComponent().getDeclaredAnnotations()).map(ReflectionAdapter::getAnnotationUsage).toList();
   }

   @Override
   public boolean isSubtypeOf(Shadow shadow)
   {
      if (getType() instanceof Primitive primitive)
      {
         return primitive.isSubtypeOf(shadow);
      }
      if (getType() instanceof Class aClass)
      {
         return aClass.isSubtypeOf(shadow);
      }
      if (getType() instanceof Array array)
      {
         return array.isSubtypeOf(shadow);
      }
      return false;
   }

   @Override
   public boolean isAssignableFrom(Shadow shadow)
   {
      if (getType() instanceof Primitive primitive)
      {
         return primitive.isAssignableFrom(shadow);
      }
      if (getType() instanceof Class aClass)
      {
         return aClass.isAssignableFrom(shadow);
      }
      return false;
   }

   @Override
   public Record getRecord()
   {
      return ReflectionAdapter.getShadow(getRecordComponent().getDeclaringRecord());
   }

   @Override
   public Shadow getType()
   {
      return ReflectionAdapter.getShadow(getRecordComponent().getType());
   }

   @Override
   public Method getGetter()
   {
      return ReflectionAdapter.getShadow(getRecordComponent().getAccessor());
   }

   @Override
   public Package getPackage()
   {
      return ReflectionAdapter.getShadow(getRecordComponent().getDeclaringRecord().getPackage());
   }

   @Override
   public TypeKind getTypeKind()
   {
      return TypeKind.RECORD_COMPONENT;
   }

   @Override
   public boolean representsSameType(Shadow shadow)
   {
      return shadow != null &&
             convert(shadow).toRecordComponent().map(recordComponent1 -> recordComponent1.getType().representsSameType(getType())).orElse(false);
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
      return Objects.equals(getName(), otherRecordComponent.getName()) &&
             Objects.equals(getType(), otherRecordComponent.getType());
   }

   public java.lang.reflect.RecordComponent getReflection()
   {
      return recordComponent;
   }
}
