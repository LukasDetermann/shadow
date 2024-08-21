package io.determann.shadow.api.lang_model.shadow;

import io.determann.shadow.api.lang_model.shadow.structure.LM_EnumConstant;
import io.determann.shadow.api.lang_model.shadow.type.LM_Shadow;
import io.determann.shadow.api.shadow.C_AnnotationValue;

import java.util.List;

public sealed interface LM_AnnotationValue extends C_AnnotationValue
{
   /**
    * is this the default value specified in the annotation?
    */
   boolean isDefault();

   Object getValue();

   non-sealed interface StringValue extends LM_AnnotationValue
   {
      @Override
      String getValue();
   }

   non-sealed interface BooleanValue extends LM_AnnotationValue
   {
      @Override
      Boolean getValue();
   }

   non-sealed interface ByteValue extends LM_AnnotationValue
   {
      @Override
      Byte getValue();
   }

   non-sealed interface ShortValue extends LM_AnnotationValue
   {
      @Override
      Short getValue();
   }

   non-sealed interface IntegerValue extends LM_AnnotationValue
   {
      @Override
      Integer getValue();
   }

   non-sealed interface LongValue extends LM_AnnotationValue
   {
      @Override
      Long getValue();
   }

   non-sealed interface CharacterValue extends LM_AnnotationValue
   {
      @Override
      Character getValue();
   }

   non-sealed interface FloatValue extends LM_AnnotationValue
   {
      @Override
      Float getValue();
   }

   non-sealed interface DoubleValue extends LM_AnnotationValue
   {
      @Override
      Double getValue();
   }

   non-sealed interface TypeValue extends LM_AnnotationValue
   {
      @Override
      LM_Shadow getValue();
   }

   non-sealed interface EnumConstantValue extends LM_AnnotationValue
   {
      @Override
      LM_EnumConstant getValue();
   }

   non-sealed interface AnnotationUsageValue extends LM_AnnotationValue
   {
      @Override
      LM_AnnotationUsage getValue();
   }

   non-sealed interface Values extends LM_AnnotationValue
   {
      @Override
      List<LM_AnnotationValue> getValue();
   }
}
