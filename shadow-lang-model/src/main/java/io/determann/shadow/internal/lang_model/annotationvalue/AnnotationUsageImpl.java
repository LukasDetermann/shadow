package io.determann.shadow.internal.lang_model.annotationvalue;

import io.determann.shadow.api.C;
import io.determann.shadow.api.lang_model.LM;
import io.determann.shadow.api.lang_model.adapter.Adapters;
import io.determann.shadow.api.query.Implementation;
import io.determann.shadow.api.query.Provider;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.ExecutableElement;
import java.util.*;

import static io.determann.shadow.api.lang_model.adapter.Adapters.adapt;
import static io.determann.shadow.api.query.Operations.ANNOTATION_USAGE_GET_ANNOTATION;
import static io.determann.shadow.api.query.Operations.ANNOTATION_USAGE_GET_VALUES;
import static io.determann.shadow.internal.lang_model.annotationvalue.AnnotationValueImpl.create;

public class AnnotationUsageImpl implements LM.AnnotationUsage
{
   private final LM.Context context;
   private final AnnotationMirror annotationMirror;

   public static List<LM.AnnotationUsage> from(LM.Context langModelContext,
                                               Collection<? extends AnnotationMirror> annotationMirrors)
   {
      return annotationMirrors.stream().map(annotationMirror -> from(langModelContext, annotationMirror)).toList();
   }

   static LM.AnnotationUsage from(LM.Context langModelContext, AnnotationMirror annotationMirror)
   {
      return new AnnotationUsageImpl(langModelContext, annotationMirror);
   }

   private AnnotationUsageImpl(LM.Context context, AnnotationMirror annotationMirror)
   {
      this.context = context;
      this.annotationMirror = annotationMirror;
   }

   @Override
   public Map<LM.Method, LM.AnnotationValue> getValues()
   {
      Map<LM.Method, LM.AnnotationValue> result = new LinkedHashMap<>();

      Map<? extends ExecutableElement, ? extends javax.lang.model.element.AnnotationValue> withoutDefaults = annotationMirror.getElementValues();

      Map<? extends ExecutableElement, ? extends javax.lang.model.element.AnnotationValue> withDefaults =
            adapt(getApi()).toElements().getElementValuesWithDefaults(annotationMirror);

      for (Map.Entry<? extends ExecutableElement, ? extends javax.lang.model.element.AnnotationValue> entry : withDefaults.entrySet())
      {
         result.put((LM.Method) adapt(getApi(), entry.getKey()),
                    create(context, entry.getValue(), !withoutDefaults.containsKey(entry.getKey())));
      }
      return result;
   }

   @Override
   public LM.Annotation getAnnotation()
   {
      return (LM.Annotation) Adapters.adapt(getApi(), annotationMirror.getAnnotationType());
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

   public LM.Context getApi()
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
      if (!(other instanceof C.AnnotationUsage otherAnnotationUsage))
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
