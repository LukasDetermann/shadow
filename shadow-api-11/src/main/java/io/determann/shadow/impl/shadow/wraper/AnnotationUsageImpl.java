package io.determann.shadow.impl.shadow.wraper;

import io.determann.shadow.api.ShadowApi;
import io.determann.shadow.api.shadow.Annotation;
import io.determann.shadow.api.shadow.AnnotationUsage;
import io.determann.shadow.api.shadow.Method;
import io.determann.shadow.api.wrapper.AnnotationValueTypeChooser;
import io.determann.shadow.impl.shadow.DeclaredImpl;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.ExecutableElement;
import java.util.*;

import static java.util.stream.Collectors.toUnmodifiableList;

public class AnnotationUsageImpl extends DeclaredImpl implements AnnotationUsage
{
   private final ShadowApi shadowApi;
   private final AnnotationMirror annotationMirror;

   public static List<AnnotationUsage> from(ShadowApi shadowApi,
                                            Collection<? extends AnnotationMirror> annotationMirrors)
   {
      return annotationMirrors.stream().map(annotationMirror -> from(shadowApi, annotationMirror)).collect(toUnmodifiableList());
   }

   static AnnotationUsage from(ShadowApi shadowApi, AnnotationMirror annotationMirror)
   {
      return new AnnotationUsageImpl(shadowApi, annotationMirror);
   }

   private AnnotationUsageImpl(ShadowApi shadowApi, AnnotationMirror annotationMirror)
   {
      super(shadowApi, annotationMirror.getAnnotationType());
      this.shadowApi = shadowApi;
      this.annotationMirror = annotationMirror;
   }

   @Override
   public Map<Method, AnnotationValueTypeChooser> getValues()
   {
      Map<Method, AnnotationValueTypeChooser> result = new LinkedHashMap<>();

      Map<? extends ExecutableElement, ? extends AnnotationValue> withoutDefaults = annotationMirror.getElementValues();

      Map<? extends ExecutableElement, ? extends AnnotationValue> withDefaults =
            shadowApi.getJdkApiContext().getProcessingEnv().getElementUtils().getElementValuesWithDefaults(annotationMirror);

      for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : withDefaults.entrySet())
      {
         result.put(shadowApi.getShadowFactory().shadowFromElement(entry.getKey()),
                    new AnnotationValueTypeChooserImpl(shadowApi, entry.getValue(), !withoutDefaults.containsKey(entry.getKey())));
      }
      return result;
   }

   @Override
   public Optional<AnnotationValueTypeChooser> getValue(String methodName)
   {
      return getValues().entrySet()
                        .stream()
                        .filter(entry -> entry.getKey().getSimpleName().equals(methodName))
                        .map(Map.Entry::getValue)
                        .findAny();
   }

   @Override
   public AnnotationValueTypeChooser getValueOrThrow(String methodName)
   {
      return getValue(methodName).orElseThrow();
   }

   @Override
   public Annotation getAnnotation()
   {
      return getApi().getShadowFactory().shadowFromElement(getElement());
   }

   @Override
   public String toString()
   {
      return annotationMirror.toString();
   }

   @Override
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
