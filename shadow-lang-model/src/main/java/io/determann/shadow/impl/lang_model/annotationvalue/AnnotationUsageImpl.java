package io.determann.shadow.impl.lang_model.annotationvalue;

import io.determann.shadow.api.annotationvalue.AnnotationValue;
import io.determann.shadow.api.lang_model.LangModelAdapter;
import io.determann.shadow.api.lang_model.LangModelContext;
import io.determann.shadow.api.shadow.Annotation;
import io.determann.shadow.api.shadow.AnnotationUsage;
import io.determann.shadow.api.shadow.Method;
import io.determann.shadow.impl.lang_model.shadow.DeclaredImpl;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.ExecutableElement;
import java.util.*;

public class AnnotationUsageImpl extends DeclaredImpl implements AnnotationUsage
{
   private final LangModelContext context;
   private final AnnotationMirror annotationMirror;

   public static List<AnnotationUsage> from(LangModelContext annotationProcessingContext,
                                            Collection<? extends AnnotationMirror> annotationMirrors)
   {
      return annotationMirrors.stream().map(annotationMirror -> from(annotationProcessingContext, annotationMirror)).toList();
   }

   static AnnotationUsage from(LangModelContext annotationProcessingContext, AnnotationMirror annotationMirror)
   {
      return new AnnotationUsageImpl(annotationProcessingContext, annotationMirror);
   }

   private AnnotationUsageImpl(LangModelContext context, AnnotationMirror annotationMirror)
   {
      super(context, annotationMirror.getAnnotationType());
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
         result.put(LangModelAdapter.getShadow(getApi(), entry.getKey()),
                    new AnnotationValueImpl(context, entry.getValue(), !withoutDefaults.containsKey(entry.getKey())));
      }
      return result;
   }

   @Override
   public Annotation getAnnotation()
   {
      return LangModelAdapter.getShadow(getApi(), getElement());
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
