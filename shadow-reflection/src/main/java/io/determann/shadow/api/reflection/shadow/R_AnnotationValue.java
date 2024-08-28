package io.determann.shadow.api.reflection.shadow;

import io.determann.shadow.api.reflection.shadow.structure.R_EnumConstant;
import io.determann.shadow.api.reflection.shadow.type.R_Type;
import io.determann.shadow.api.shadow.C_AnnotationValue;

import java.util.List;

public sealed interface R_AnnotationValue extends C_AnnotationValue
{
   /**
    * is this the default value specified in the annotation?
    */
   boolean isDefault();

   Object getValue();

   non-sealed interface StringValue extends R_AnnotationValue
   {
      @Override
      String getValue();
   }

   non-sealed interface BooleanValue extends R_AnnotationValue
   {
      @Override
      Boolean getValue();
   }

   non-sealed interface ByteValue extends R_AnnotationValue
   {
      @Override
      Byte getValue();
   }

   non-sealed interface ShortValue extends R_AnnotationValue
   {
      @Override
      Short getValue();
   }

   non-sealed interface IntegerValue extends R_AnnotationValue
   {
      @Override
      Integer getValue();
   }

   non-sealed interface LongValue extends R_AnnotationValue
   {
      @Override
      Long getValue();
   }

   non-sealed interface CharacterValue extends R_AnnotationValue
   {
      @Override
      Character getValue();
   }

   non-sealed interface FloatValue extends R_AnnotationValue
   {
      @Override
      Float getValue();
   }

   non-sealed interface DoubleValue extends R_AnnotationValue
   {
      @Override
      Double getValue();
   }

   non-sealed interface TypeValue extends R_AnnotationValue
   {
      @Override
      R_Type getValue();
   }

   non-sealed interface EnumConstantValue extends R_AnnotationValue
   {
      @Override
      R_EnumConstant getValue();
   }

   non-sealed interface AnnotationUsageValue extends R_AnnotationValue
   {
      @Override
      R_AnnotationUsage getValue();
   }

   non-sealed interface Values extends R_AnnotationValue
   {
      @Override
      List<R_AnnotationValue> getValue();
   }
}
