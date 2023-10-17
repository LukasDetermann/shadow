package io.determann.shadow.impl.annotation_processing.annotationvalue;

import io.determann.shadow.api.MirrorAdapter;
import io.determann.shadow.api.annotation_processing.AnnotationProcessingContext;
import io.determann.shadow.api.annotationvalue.AnnotationValue;
import io.determann.shadow.api.shadow.Annotation;
import io.determann.shadow.api.shadow.AnnotationUsage;
import io.determann.shadow.api.shadow.Method;
import io.determann.shadow.impl.annotation_processing.shadow.DeclaredImpl;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.ExecutableElement;
import java.util.*;

import static java.util.stream.Collectors.toUnmodifiableList;

public class AnnotationUsageImpl extends DeclaredImpl implements AnnotationUsage
{
   private final AnnotationProcessingContext annotationProcessingContext;
   private final AnnotationMirror annotationMirror;

   public static List<AnnotationUsage> from(AnnotationProcessingContext annotationProcessingContext,
                                            Collection<? extends AnnotationMirror> annotationMirrors)
   {
      return annotationMirrors.stream().map(annotationMirror -> from(annotationProcessingContext, annotationMirror)).collect(toUnmodifiableList());
   }

   static AnnotationUsage from(AnnotationProcessingContext annotationProcessingContext, AnnotationMirror annotationMirror)
   {
      return new AnnotationUsageImpl(annotationProcessingContext, annotationMirror);
   }

   private AnnotationUsageImpl(AnnotationProcessingContext annotationProcessingContext, AnnotationMirror annotationMirror)
   {
      super(annotationProcessingContext, annotationMirror.getAnnotationType());
      this.annotationProcessingContext = annotationProcessingContext;
      this.annotationMirror = annotationMirror;
   }

   @Override
   public Map<Method, AnnotationValue> getValues()
   {
      Map<Method, AnnotationValue> result = new LinkedHashMap<>();

      Map<? extends ExecutableElement, ? extends javax.lang.model.element.AnnotationValue> withoutDefaults = annotationMirror.getElementValues();

      Map<? extends ExecutableElement, ? extends javax.lang.model.element.AnnotationValue> withDefaults =
            MirrorAdapter.getProcessingEnv(getApi()).getElementUtils().getElementValuesWithDefaults(annotationMirror);

      for (Map.Entry<? extends ExecutableElement, ? extends javax.lang.model.element.AnnotationValue> entry : withDefaults.entrySet())
      {
         result.put(MirrorAdapter.getShadow(getApi(), entry.getKey()),
                    new AnnotationValueImpl(annotationProcessingContext, entry.getValue(), !withoutDefaults.containsKey(entry.getKey())));
      }
      return result;
   }

   @Override
   public Annotation getAnnotation()
   {
      return MirrorAdapter.getShadow(getApi(), getElement());
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
      return Objects.hash(getTypeKind(),
                          getQualifiedName(),
                          getModule(),
                          getValues());
   }

   @Override
   public boolean equals(Object other)
   {
      if (other == this)
      {
         return true;
      }
      if (other == null || !getClass().equals(other.getClass()))
      {
         return false;
      }
      AnnotationUsageImpl otherAnnotationUsage = (AnnotationUsageImpl) other;
      return Objects.equals(getQualifiedName(), otherAnnotationUsage.getQualifiedName()) &&
             Objects.equals(getTypeKind(), otherAnnotationUsage.getTypeKind()) &&
             Objects.equals(getModule(), otherAnnotationUsage.getModule()) &&
             Objects.equals(getValues(), otherAnnotationUsage.getValues());
   }
}
