package io.determann.shadow.internal.lang_model.annotationvalue;

import io.determann.shadow.api.annotationvalue.AnnotationValue;
import io.determann.shadow.api.lang_model.LangModelAdapter;
import io.determann.shadow.api.lang_model.LangModelContext;
import io.determann.shadow.api.shadow.Annotation;
import io.determann.shadow.api.shadow.AnnotationUsage;
import io.determann.shadow.api.shadow.Method;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.ExecutableElement;
import java.util.*;

public class AnnotationUsageImpl implements AnnotationUsage
{
   private final LangModelContext context;
   private final AnnotationMirror annotationMirror;

   public static List<AnnotationUsage> from(LangModelContext langModelContext,
                                            Collection<? extends AnnotationMirror> annotationMirrors)
   {
      return annotationMirrors.stream().map(annotationMirror -> from(langModelContext, annotationMirror)).toList();
   }

   static AnnotationUsage from(LangModelContext langModelContext, AnnotationMirror annotationMirror)
   {
      return new AnnotationUsageImpl(langModelContext, annotationMirror);
   }

   private AnnotationUsageImpl(LangModelContext context, AnnotationMirror annotationMirror)
   {
      this.context = context;
      this.annotationMirror = annotationMirror;
   }

   @Override
   public Map<Method, AnnotationValue> getValues()
   {
      Map<Method, AnnotationValue> result = new LinkedHashMap<>();

      Map<? extends ExecutableElement, ? extends javax.lang.model.element.AnnotationValue> withoutDefaults = annotationMirror.getElementValues();

      Map<? extends ExecutableElement, ? extends javax.lang.model.element.AnnotationValue> withDefaults =
            LangModelAdapter.getElements(getApi()).getElementValuesWithDefaults(annotationMirror);

      for (Map.Entry<? extends ExecutableElement, ? extends javax.lang.model.element.AnnotationValue> entry : withDefaults.entrySet())
      {
         result.put((Method) LangModelAdapter.generalize(getApi(), entry.getKey()),
                    new AnnotationValueImpl(context, entry.getValue(), !withoutDefaults.containsKey(entry.getKey())));
      }
      return result;
   }

   @Override
   public Annotation getAnnotation()
   {
      return LangModelAdapter.generalize(getApi(), annotationMirror.getAnnotationType());
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

   public LangModelContext getApi()
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
      if (!(other instanceof AnnotationUsage otherAnnotationUsage))
      {
         return false;
      }

      return Objects.equals(getAnnotation(), otherAnnotationUsage.getAnnotation()) &&
             Objects.equals(getValues(), otherAnnotationUsage.getValues());
   }
}
