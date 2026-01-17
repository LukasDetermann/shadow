package io.determann.shadow.internal.annotation_processing.annotationvalue;

import io.determann.shadow.api.annotation_processing.Ap;
import io.determann.shadow.api.annotation_processing.dsl.RenderingContext;
import io.determann.shadow.api.annotation_processing.dsl.annotation_usage.AnnotationUsageNameStep;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.ExecutableElement;
import java.util.*;

import static io.determann.shadow.api.annotation_processing.adapter.Adapters.adapt;
import static io.determann.shadow.api.annotation_processing.dsl.JavaDsl.annotationUsage;
import static io.determann.shadow.internal.annotation_processing.annotationvalue.AnnotationValueImpl.create;

public class AnnotationUsageImpl
      implements Ap.AnnotationUsage
{
   private final Ap.Context context;
   private final AnnotationMirror annotationMirror;

   public static List<Ap.AnnotationUsage> from(Ap.Context langModelContext,
                                               Collection<? extends AnnotationMirror> annotationMirrors)
   {
      return annotationMirrors.stream().map(annotationMirror -> from(langModelContext, annotationMirror)).toList();
   }

   static Ap.AnnotationUsage from(Ap.Context langModelContext, AnnotationMirror annotationMirror)
   {
      return new AnnotationUsageImpl(langModelContext, annotationMirror);
   }

   private AnnotationUsageImpl(Ap.Context context, AnnotationMirror annotationMirror)
   {
      this.context = context;
      this.annotationMirror = annotationMirror;
   }

   @Override
   public Map<Ap.Method, Ap.AnnotationValue> getValues()
   {
      Map<Ap.Method, Ap.AnnotationValue> result = new LinkedHashMap<>();

      Map<? extends ExecutableElement, ? extends javax.lang.model.element.AnnotationValue> withoutDefaults = annotationMirror.getElementValues();

      Map<? extends ExecutableElement, ? extends javax.lang.model.element.AnnotationValue> withDefaults =
            adapt(getApi()).toElements().getElementValuesWithDefaults(annotationMirror);

      for (Map.Entry<? extends ExecutableElement, ? extends javax.lang.model.element.AnnotationValue> entry : withDefaults.entrySet())
      {
         result.put((Ap.Method) adapt(getApi(), entry.getKey()),
                    create(context, entry.getValue(), !withoutDefaults.containsKey(entry.getKey())));
      }
      return result;
   }

   @Override
   public Ap.Annotation getAnnotation()
   {
      return (Ap.Annotation) adapt(getApi(), annotationMirror.getAnnotationType());
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

   public Ap.Context getApi()
   {
      return context;
   }

   @Override
   public String renderDeclaration(RenderingContext renderingContext)
   {
      AnnotationUsageNameStep nameStep = annotationUsage().type(getAnnotation());

      for (Map.Entry<? extends Ap.Method, ? extends Ap.AnnotationValue> entry : getValues().entrySet())
      {
         nameStep = nameStep.name(entry.getKey().getName()).value(entry.getValue());
      }
      return nameStep.renderDeclaration(renderingContext);
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
      if (!(other instanceof Ap.AnnotationUsage otherAnnotationUsage))
      {
         return false;
      }
      return Objects.equals(getAnnotation(), otherAnnotationUsage.getAnnotation()) &&
             Objects.equals(getValues(), otherAnnotationUsage.getValues());
   }
}
