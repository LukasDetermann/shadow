package io.determann.shadow.api.lang_model.shadow;

import io.determann.shadow.api.shadow.AnnotationUsage;
import io.determann.shadow.api.shadow.AnnotationValue;
import io.determann.shadow.api.shadow.structure.EnumConstant;
import io.determann.shadow.api.shadow.type.Shadow;

import java.util.List;

public sealed interface AnnotationValueLangModel extends AnnotationValue
{
   /**
    * is this the default value specified in the annotation?
    */
   boolean isDefault();

   Object getValue();

   non-sealed interface StringValue extends AnnotationValueLangModel
   {
      @Override
      String getValue();
   }

   non-sealed interface BooleanValue extends AnnotationValueLangModel
   {
      @Override
      Boolean getValue();
   }

   non-sealed interface ByteValue extends AnnotationValueLangModel
   {
      @Override
      Byte getValue();
   }

   non-sealed interface ShortValue extends AnnotationValueLangModel
   {
      @Override
      Short getValue();
   }

   non-sealed interface IntegerValue extends AnnotationValueLangModel
   {
      @Override
      Integer getValue();
   }

   non-sealed interface LongValue extends AnnotationValueLangModel
   {
      @Override
      Long getValue();
   }

   non-sealed interface CharacterValue extends AnnotationValueLangModel
   {
      @Override
      Character getValue();
   }

   non-sealed interface FloatValue extends AnnotationValueLangModel
   {
      @Override
      Float getValue();
   }

   non-sealed interface DoubleValue extends AnnotationValueLangModel
   {
      @Override
      Double getValue();
   }

   non-sealed interface TypeValue extends AnnotationValueLangModel
   {
      @Override
      Shadow getValue();
   }

   non-sealed interface EnumConstantValue extends AnnotationValueLangModel
   {
      @Override
      EnumConstant getValue();
   }

   non-sealed interface AnnotationUsageValue extends AnnotationValueLangModel
   {
      @Override
      AnnotationUsage getValue();
   }

   non-sealed interface Values extends AnnotationValueLangModel
   {
      @Override
      List<AnnotationValueLangModel> getValue();
   }
}
