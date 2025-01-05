package io.determann.shadow.internal.lang_model.annotationvalue;

import io.determann.shadow.api.Implementation;
import io.determann.shadow.api.Provider;
import io.determann.shadow.api.lang_model.LM_Context;
import io.determann.shadow.api.lang_model.adapter.LM_Adapters;
import io.determann.shadow.api.lang_model.shadow.LM_AnnotationUsage;
import io.determann.shadow.api.lang_model.shadow.LM_AnnotationValue;
import io.determann.shadow.api.lang_model.shadow.structure.LM_Method;
import io.determann.shadow.api.lang_model.shadow.type.LM_Annotation;
import io.determann.shadow.api.shadow.C_AnnotationUsage;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.ExecutableElement;
import java.util.*;

import static io.determann.shadow.api.Operations.ANNOTATION_USAGE_GET_ANNOTATION;
import static io.determann.shadow.api.Operations.ANNOTATION_USAGE_GET_VALUES;
import static io.determann.shadow.api.lang_model.adapter.LM_Adapters.adapt;
import static io.determann.shadow.internal.lang_model.annotationvalue.AnnotationValueImpl.create;

public class AnnotationUsageImpl implements LM_AnnotationUsage
{
   private final LM_Context context;
   private final AnnotationMirror annotationMirror;

   public static List<LM_AnnotationUsage> from(LM_Context langModelContext,
                                               Collection<? extends AnnotationMirror> annotationMirrors)
   {
      return annotationMirrors.stream().map(annotationMirror -> from(langModelContext, annotationMirror)).toList();
   }

   static LM_AnnotationUsage from(LM_Context langModelContext, AnnotationMirror annotationMirror)
   {
      return new AnnotationUsageImpl(langModelContext, annotationMirror);
   }

   private AnnotationUsageImpl(LM_Context context, AnnotationMirror annotationMirror)
   {
      this.context = context;
      this.annotationMirror = annotationMirror;
   }

   @Override
   public Map<LM_Method, LM_AnnotationValue> getValues()
   {
      Map<LM_Method, LM_AnnotationValue> result = new LinkedHashMap<>();

      Map<? extends ExecutableElement, ? extends javax.lang.model.element.AnnotationValue> withoutDefaults = annotationMirror.getElementValues();

      Map<? extends ExecutableElement, ? extends javax.lang.model.element.AnnotationValue> withDefaults =
            adapt(getApi()).toElements().getElementValuesWithDefaults(annotationMirror);

      for (Map.Entry<? extends ExecutableElement, ? extends javax.lang.model.element.AnnotationValue> entry : withDefaults.entrySet())
      {
         result.put((LM_Method) adapt(getApi(), entry.getKey()),
                    create(context, entry.getValue(), !withoutDefaults.containsKey(entry.getKey())));
      }
      return result;
   }

   @Override
   public LM_Annotation getAnnotation()
   {
      return (LM_Annotation) LM_Adapters.adapt(getApi(), annotationMirror.getAnnotationType());
   }

   @Override
   public String toString()
   {
      return annotationMirror.toString();
   }

   public AnnotationMirror getAnnotationMirror()
   {
      return annotationMirror;
   }

   public LM_Context getApi()
   {
      return context;
   }

   @Override
   public int hashCode()
   {
      return Objects.hash(getAnnotation(),
                          getValues());
   }

   @Override
   public boolean equals(Object other)
   {
      if (other == this)
      {
         return true;
      }
      if (!(other instanceof C_AnnotationUsage otherAnnotationUsage))
      {
         return false;
      }
      return Provider.requestOrEmpty(otherAnnotationUsage, ANNOTATION_USAGE_GET_ANNOTATION).map(name -> Objects.equals(getAnnotation(), name)).orElse(false) &&
             Provider.requestOrEmpty(otherAnnotationUsage, ANNOTATION_USAGE_GET_VALUES).map(values -> Objects.equals(getValues(), values)).orElse(false);
   }

   @Override
   public Implementation getImplementation()
   {
      return getApi().getImplementation();
   }
}
