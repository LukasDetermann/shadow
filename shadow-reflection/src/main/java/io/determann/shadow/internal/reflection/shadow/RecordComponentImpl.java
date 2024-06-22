package io.determann.shadow.internal.reflection.shadow;

import io.determann.shadow.api.TypeKind;
import io.determann.shadow.api.reflection.ReflectionAdapter;
import io.determann.shadow.api.reflection.query.RecordComponentReflection;
import io.determann.shadow.api.shadow.Class;
import io.determann.shadow.api.shadow.Module;
import io.determann.shadow.api.shadow.Package;
import io.determann.shadow.api.shadow.Record;
import io.determann.shadow.api.shadow.*;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static io.determann.shadow.api.converter.Converter.convert;
import static io.determann.shadow.internal.reflection.ReflectionProvider.IMPLEMENTATION_NAME;
import static io.determann.shadow.meta_meta.Operations.*;
import static io.determann.shadow.meta_meta.Provider.request;
import static io.determann.shadow.meta_meta.Provider.requestOrThrow;

public class RecordComponentImpl implements RecordComponentReflection
{
   private final java.lang.reflect.RecordComponent recordComponent;

   public RecordComponentImpl(java.lang.reflect.RecordComponent recordComponent)
   {
      this.recordComponent = recordComponent;
   }

   @Override
   public Module getModule()
   {
      return requestOrThrow(getRecord(), MODULE_ENCLOSED_GET_MODULE);
   }

   @Override
   public String getName()
   {
      return getRecordComponent().getName();
   }

   @Override
   public List<AnnotationUsage> getAnnotationUsages()
   {
      return Arrays.stream(getRecordComponent().getAnnotations()).map(ReflectionAdapter::generalize).toList();
   }

   @Override
   public List<AnnotationUsage> getDirectAnnotationUsages()
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
   public Record getRecord()
   {
      return ReflectionAdapter.generalize(getRecordComponent().getDeclaringRecord());
   }

   @Override
   public Shadow getType()
   {
      return ReflectionAdapter.generalize(getRecordComponent().getType());
   }

   @Override
   public Method getGetter()
   {
      return ReflectionAdapter.generalize(getRecordComponent().getAccessor());
   }

   @Override
   public Package getPackage()
   {
      return ReflectionAdapter.generalize(getRecordComponent().getDeclaringRecord().getPackage());
   }

   @Override
   public TypeKind getKind()
   {
      return TypeKind.RECORD_COMPONENT;
   }

   @Override
   public boolean representsSameType(Shadow shadow)
   {
      return shadow != null &&
             convert(shadow).toRecordComponent()
                            .map(recordComponent1 -> requestOrThrow(requestOrThrow(recordComponent1, RECORD_COMPONENT_GET_TYPE),
                                                                    SHADOW_REPRESENTS_SAME_TYPE,
                                                                    getType()))
                            .orElse(false);
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
      return request(otherRecordComponent, NAMEABLE_NAME).map(name -> Objects.equals(getName(), name)).orElse(false) &&
             request(otherRecordComponent, RECORD_COMPONENT_GET_TYPE).map(shadow -> Objects.equals(shadow, getType())).orElse(false);
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
