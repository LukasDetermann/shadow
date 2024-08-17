package io.determann.shadow.api.reflection.shadow;

import io.determann.shadow.api.reflection.shadow.structure.EnumConstantReflection;
import io.determann.shadow.api.reflection.shadow.type.ShadowReflection;
import io.determann.shadow.api.shadow.AnnotationValue;

import java.util.List;

public sealed interface AnnotationValueReflection extends AnnotationValue
{
   /**
    * is this the default value specified in the annotation?
    */
   boolean isDefault();

   Object getValue();

   non-sealed interface StringValue extends AnnotationValueReflection
   {
      @Override
      String getValue();
   }

   non-sealed interface BooleanValue extends AnnotationValueReflection
   {
      @Override
      Boolean getValue();
   }

   non-sealed interface ByteValue extends AnnotationValueReflection
   {
      @Override
      Byte getValue();
   }

   non-sealed interface ShortValue extends AnnotationValueReflection
   {
      @Override
      Short getValue();
   }

   non-sealed interface IntegerValue extends AnnotationValueReflection
   {
      @Override
      Integer getValue();
   }

   non-sealed interface LongValue extends AnnotationValueReflection
   {
      @Override
      Long getValue();
   }

   non-sealed interface CharacterValue extends AnnotationValueReflection
   {
      @Override
      Character getValue();
   }

   non-sealed interface FloatValue extends AnnotationValueReflection
   {
      @Override
      Float getValue();
   }

   non-sealed interface DoubleValue extends AnnotationValueReflection
   {
      @Override
      Double getValue();
   }

   non-sealed interface TypeValue extends AnnotationValueReflection
   {
      @Override
      ShadowReflection getValue();
   }

   non-sealed interface EnumConstantValue extends AnnotationValueReflection
   {
      @Override
      EnumConstantReflection getValue();
   }

   non-sealed interface AnnotationUsageValue extends AnnotationValueReflection
   {
      @Override
      AnnotationUsageReflection getValue();
   }

   non-sealed interface Values extends AnnotationValueReflection
   {
      @Override
      List<AnnotationValueReflection> getValue();
   }
}
