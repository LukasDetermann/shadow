package io.determann.shadow.internal.annotation_processing.annotationvalue;

import io.determann.shadow.api.C;
import io.determann.shadow.api.annotation_processing.AP;
import io.determann.shadow.api.query.Implementation;
import io.determann.shadow.api.query.Provider;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.ExecutableElement;
import java.util.*;

import static io.determann.shadow.api.annotation_processing.adapter.Adapters.adapt;
import static io.determann.shadow.api.query.Operations.ANNOTATION_USAGE_GET_ANNOTATION;
import static io.determann.shadow.api.query.Operations.ANNOTATION_USAGE_GET_VALUES;
import static io.determann.shadow.internal.annotation_processing.annotationvalue.AnnotationValueImpl.create;

public class AnnotationUsageImpl implements AP.AnnotationUsage
{
   private final AP.Context context;
   private final AnnotationMirror annotationMirror;

   public static List<AP.AnnotationUsage> from(AP.Context langModelContext,
                                               Collection<? extends AnnotationMirror> annotationMirrors)
   {
      return annotationMirrors.stream().map(annotationMirror -> from(langModelContext, annotationMirror)).toList();
   }

   static AP.AnnotationUsage from(AP.Context langModelContext, AnnotationMirror annotationMirror)
   {
      return new AnnotationUsageImpl(langModelContext, annotationMirror);
   }

   private AnnotationUsageImpl(AP.Context context, AnnotationMirror annotationMirror)
   {
      this.context = context;
      this.annotationMirror = annotationMirror;
   }

   @Override
   public Map<AP.Method, AP.AnnotationValue> getValues()
   {
      Map<AP.Method, AP.AnnotationValue> result = new LinkedHashMap<>();

      Map<? extends ExecutableElement, ? extends javax.lang.model.element.AnnotationValue> withoutDefaults = annotationMirror.getElementValues();

      Map<? extends ExecutableElement, ? extends javax.lang.model.element.AnnotationValue> withDefaults =
            adapt(getApi()).toElements().getElementValuesWithDefaults(annotationMirror);

      for (Map.Entry<? extends ExecutableElement, ? extends javax.lang.model.element.AnnotationValue> entry : withDefaults.entrySet())
      {
         result.put((AP.Method) adapt(getApi(), entry.getKey()),
                    create(context, entry.getValue(), !withoutDefaults.containsKey(entry.getKey())));
      }
      return result;
   }

   @Override
   public AP.Annotation getAnnotation()
   {
      return (AP.Annotation) adapt(getApi(), annotationMirror.getAnnotationType());
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

   public AP.Context getApi()
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
